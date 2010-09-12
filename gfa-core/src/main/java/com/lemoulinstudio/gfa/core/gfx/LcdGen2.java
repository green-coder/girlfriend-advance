package com.lemoulinstudio.gfa.core.gfx;

import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32.WindowMode;
import java.awt.image.DirectColorModel;
import java.util.Arrays;

public class LcdGen2 extends Lcd {

  private final int killedFragment = (5 << 16); // priority 5, under the backdrop.

  private int[][] bgLine = new int[4][xScreenSize];
  private int[] objLine = new int[xScreenSize];
  private boolean[] objWindowLine = new boolean[xScreenSize];

  public LcdGen2() {
    super(new DirectColorModel(32, 0x001f, 0x3e0, 0x7c00));
  }

  public void drawLine(int yScr) {
    if (yScr >= yScreenSize)
      return;

    int modeLayers = 0;

    // Fill the enabled line buffers.
    switch (ioMem.getGfxMode()) {
      case 0: {
        if (ioMem.isBGEnabled(0)) drawBGTextModeLine(0, yScr, bgLine[0]);
        if (ioMem.isBGEnabled(1)) drawBGTextModeLine(1, yScr, bgLine[1]);
        if (ioMem.isBGEnabled(2)) drawBGTextModeLine(2, yScr, bgLine[2]);
        if (ioMem.isBGEnabled(3)) drawBGTextModeLine(3, yScr, bgLine[3]);
        modeLayers = 0x1f; // bg 0, 1, 2, 3 + obj
        break;
      }

      case 1: {
        if (ioMem.isBGEnabled(0)) drawBGTextModeLine(0, yScr, bgLine[0]);
        if (ioMem.isBGEnabled(1)) drawBGTextModeLine(1, yScr, bgLine[1]);
        if (ioMem.isBGEnabled(2)) drawBGRotScalModeLine(2, yScr, bgLine[2]);
        modeLayers = 0x17; // bg 0, 1, 2 + obj
        break;
      }

      case 2: {
        if (ioMem.isBGEnabled(2)) drawBGRotScalModeLine(2, yScr, bgLine[2]);
        if (ioMem.isBGEnabled(3)) drawBGRotScalModeLine(3, yScr, bgLine[3]);
        modeLayers = 0x1c; // bg 2, 3 + obj
        break;
      }

      case 3: {
        if (ioMem.isBGEnabled(2)) drawMode3Line(yScr, bgLine[2]);
        modeLayers = 0x14; // bg 2 + obj
        break;
      }

      case 4: {
        if (ioMem.isBGEnabled(2)) drawMode4Line(yScr, bgLine[2]);
        modeLayers = 0x14; // bg 2 + obj
        break;
      }

      case 5: {
        if (ioMem.isBGEnabled(2)) drawMode5Line(yScr, bgLine[2]);
        modeLayers = 0x14; // bg 2 + obj
        break;
      }
    }

    int backdropColor = getBackdropColor();

    boolean objEnabled = ioMem.isObjEnabled();
    if (objEnabled) drawObj(yScr, objLine);
    
    boolean objWindowEnabled = objEnabled && ioMem.isObjWindowEnabled();
    if (objWindowEnabled) drawObjWindow(yScr, objWindowLine);

    boolean window0Enabled = ioMem.isWindow0Enabled();
    boolean window1Enabled = ioMem.isWindow1Enabled();
    boolean windowOutEnabled = window0Enabled || window1Enabled || objWindowEnabled;

    int win0XMin = ioMem.getWindowXMin(0);
    int win0XSup = ioMem.getWindowXSup(0);
    int win0YMin = ioMem.getWindowYMin(0);
    int win0YSup = ioMem.getWindowYSup(0);

    int win1XMin = ioMem.getWindowXMin(1);
    int win1XSup = ioMem.getWindowXSup(1);
    int win1YMin = ioMem.getWindowYMin(1);
    int win1YSup = ioMem.getWindowYSup(1);

    int masterLayers = modeLayers & ioMem.getEnabledLayers();
    final int objLayerBit = 0x10;
    final int bg3LayerBit = 0x08;
    final int bg2LayerBit = 0x04;
    final int bg1LayerBit = 0x02;
    final int bg0LayerBit = 0x01;
    
    // Mix the colors from the line buffers.
    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      int layers = masterLayers;
      
      // Check which region this screenxel belongs to.
      if (window0Enabled &&
              (yScr >= win0YMin) && (yScr < win0YSup) &&
              (xScr >= win0XMin) && (xScr < win0XSup))
            layers &= ioMem.getWindowLayers(WindowMode.InWindow0);
      else if (window1Enabled &&
              (yScr >= win1YMin) && (yScr < win1YSup) &&
              (xScr >= win1XMin) && (xScr < win1XSup))
            layers &= ioMem.getWindowLayers(WindowMode.InWindow1);
      else if (objWindowEnabled &&
              objWindowLine[xScr])
            layers &= ioMem.getWindowLayers(WindowMode.InObjWindow);
      else if (windowOutEnabled)
            layers &= ioMem.getWindowLayers(WindowMode.OutsideWindows);

      // Find the top-most pixel.
      int topMostPixelColor = backdropColor;
      int topMostPixelPriority = topMostPixelColor >>> 16; // i.e. lowest priority.

      if ((layers & objLayerBit) != 0) {
        int priority = objLine[xScr] >>> 16;
        if (priority < topMostPixelPriority) {
          topMostPixelColor = objLine[xScr];
          topMostPixelPriority = topMostPixelColor >>> 16;
        }
      }

      for (int layer = 0; layer < 4; layer++) {
        if ((layers & (1 << layer)) != 0) {
          int priority = bgLine[layer][xScr] >>> 16;
          if (priority < topMostPixelPriority) {
            topMostPixelColor = bgLine[layer][xScr];
            topMostPixelPriority = topMostPixelColor >>> 16;
          }
        }
      }

      rawPixels[xScr + yScr * xScreenSize] = topMostPixelColor;
    }

    // After the draw of the last screen line, update the display.
    if (yScr == 159) updatePixels();
  }

  private int getBackdropColor() {
    int priority = 4;
    short color15 = pMem.hardwareAccessLoadHalfWord(0);
    return ((priority << 16) | (color15 & 0xffff));
  }

  private void drawBGTextModeLine(int bgNumber, int yScr, int[] pixel) {
    int priority                 = ioMem.getBGPriority(bgNumber);
    int xScroll                  = ioMem.getBGSCX(bgNumber);
    int yScroll                  = ioMem.getBGSCY(bgNumber);
    int mapAddress               = ioMem.getBGTileMapAddress(bgNumber);
    int dataAddress              = ioMem.getBGTileDataAddress(bgNumber);
    int xNumberOfTile            = ioMem.getBGXNumberOfTile(bgNumber);
    int yNumberOfTile            = ioMem.getBGYNumberOfTile(bgNumber);
    int xNumberOfTileMask        = xNumberOfTile - 1;
    int yNumberOfTileMask        = yNumberOfTile - 1;
    boolean is256Color           = ioMem.isBG_256Color(bgNumber);
    boolean isTileDataUpsideDown = (xNumberOfTile != 32);
    boolean mosaicEnabled        = ioMem.isBGMosaicEnabled(bgNumber);
    int xMosaic                  = ioMem.getMosaicBGXSize();
    int yMosaic                  = ioMem.getMosaicBGYSize();
    
    int y = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);
    
    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      // handle the mosaic effect
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      
      int tileX = (x + xScroll) >>> 3; // the x coordinate of the tile where to find the pixel.
      int tileY = (y + yScroll) >>> 3; // The y coordinate of the tile where to find the pixel.
      
      // handle the wraparound effect.
      tileX &= xNumberOfTileMask;
      tileY &= yNumberOfTileMask;
      
      int offsetToAdd = 0;
      if (isTileDataUpsideDown) {
	  if (tileX >= 32) offsetToAdd += 0x00000800;
	  if (tileY >= 32) offsetToAdd += 0x00001000;
	  tileX &= 0x0000001f;
	  tileY &= 0x0000001f;
      }
      
      int tileDataPosInMem = ((tileX + tileY * 32) << 1) + offsetToAdd;
      short tileData = vMem.hardwareAccessLoadHalfWord(0x0000ffff &
						       (mapAddress + tileDataPosInMem));
      
      int xSubTile = (x + xScroll) & 0x07;
      int ySubTile = (y + yScroll) & 0x07;
      
      if ((tileData & 0x0400) != 0) xSubTile = 7 - xSubTile; // handle horizontal tile flip
      if ((tileData & 0x0800) != 0) ySubTile = 7 - ySubTile; // handle vertical tile flip
      
      int tileNumber = tileData & 0x000003ff;
      
      if (is256Color) { // Tile are encoded in a 1 byte per pixel format
	int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(0x0000ffff &
							      (dataAddress +
							       tileNumber * 8*8 +
							       xSubTile + ySubTile * 8));
	if (color8 == 0) {
          pixel[xScr] = killedFragment;
        }
        else { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  pixel[xScr] = getPixelFragment(priority, color15);
	}
      }
      else { // Tiles are encoded in a 4 bits per pixel format
	int paletteNumber = (tileData & 0x0000f000) >> 12;
	int color4 = 0x000000ff & vMem.hardwareAccessLoadByte(0x0000ffff &
							      (dataAddress +
							       tileNumber * 8*8/2 +
							       ((xSubTile + ySubTile * 8) >> 1)));
	if ((xSubTile & 0x01) != 0) color4 >>>= 4;
	else color4 &= 0x0f;
        
	if (color4 == 0) {
          pixel[xScr] = killedFragment;
        }
        else { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord((paletteNumber * 16 + color4) << 1);
	  pixel[xScr] = getPixelFragment(priority, color15);
	}
      }
      
    }
  }

  private void drawBGRotScalModeLine(int bgNumber, int yScr, int[] pixel) {
    int priority          = ioMem.getBGPriority(bgNumber);
    int mapAddress        = ioMem.getBGTileMapAddress(bgNumber);
    int dataAddress       = ioMem.getBGTileDataAddress(bgNumber);
    int numberOfTile      = ioMem.getBGRotScalNumberOfTile(bgNumber);
    int numberOfTileMask  = numberOfTile - 1;
    boolean isWrapAround  = ioMem.isBGRotScalWrapAround(bgNumber);
    
    // 20 bits for integers & 8 bits for decimals
    if (yScr == 0) {
      bgXOrigin[bgNumber - 2] = ioMem.getBGRotScalXOrigin(bgNumber);
      bgYOrigin[bgNumber - 2] = ioMem.getBGRotScalYOrigin(bgNumber);
    }
    
    // 8 bits for integers & 8 bits for decimals
    int pa = ioMem.getBGRotScalPA(bgNumber);
    int pb = ioMem.getBGRotScalPB(bgNumber);
    int pc = ioMem.getBGRotScalPC(bgNumber);
    int pd = ioMem.getBGRotScalPD(bgNumber);
    
    int xCurrentPos = bgXOrigin[bgNumber - 2];
    int yCurrentPos = bgYOrigin[bgNumber - 2];
    
    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      // Determinate the source of pixel to display
      int xTile = (xCurrentPos >> 8) >>> 3;
      int yTile = (yCurrentPos >> 8) >>> 3;
      
      if ((isWrapAround) ||
	  ((xTile >= 0) && (xTile < numberOfTile) &&
	   (yTile >= 0) && (yTile < numberOfTile))) {
	// handle the wraparound effect.
	xTile &= numberOfTileMask;
	yTile &= numberOfTileMask;
	
	int xSubTile = (xCurrentPos >> 8) & 0x07;
	int ySubTile = (yCurrentPos >> 8) & 0x07;
	
	int tileNumber = 0x000000ff &
	    vMem.hardwareAccessLoadByte(0x0000ffff &
					(mapAddress + xTile + yTile * numberOfTile));
	
	int color8 = 0x000000ff &
	    vMem.hardwareAccessLoadByte(0x0000ffff &
					(dataAddress +
					 tileNumber * 8*8 +
					 xSubTile + ySubTile * 8));
	if (color8 == 0) {
          pixel[xScr] = killedFragment;
        }
        else { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  pixel[xScr] = getPixelFragment(priority, color15);
	}
      }
      else {
        pixel[xScr] = killedFragment;
      }
      
      xCurrentPos += pa;
      yCurrentPos += pc;
    }
    
    // Update the origin point for the next horizontal line
    bgXOrigin[bgNumber - 2] += pb;
    bgYOrigin[bgNumber - 2] += pd;
  }

  private void drawMode3Line(int yScr, int[] pixel) {
    int bgNumber = 2;
    int priority = ioMem.getBGPriority(bgNumber);

    // 20 bits for integers & 8 bits for decimals
    if (yScr == 0) {
      bgXOrigin[bgNumber - 2] = ioMem.getBGRotScalXOrigin(bgNumber);
      bgYOrigin[bgNumber - 2] = ioMem.getBGRotScalYOrigin(bgNumber);
    }

    // 8 bits for integers & 8 bits for decimals
    int pa = ioMem.getBGRotScalPA(bgNumber);
    int pb = ioMem.getBGRotScalPB(bgNumber);
    int pc = ioMem.getBGRotScalPC(bgNumber);
    int pd = ioMem.getBGRotScalPD(bgNumber);

    int xCurrentPos = bgXOrigin[bgNumber - 2];
    int yCurrentPos = bgYOrigin[bgNumber - 2];
    
    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      int x = (xCurrentPos >> 8);
      int y = (yCurrentPos >> 8);

      if ((x >= 0) && (x < xScreenSize) && (y >= 0) && (y < yScreenSize)) {
        short color16 = vMem.hardwareAccessLoadHalfWord((x + y * xScreenSize) << 1);
        pixel[xScr] = getPixelFragment(priority, color16);
      }
     else {
        pixel[xScr] = killedFragment;
     }

      xCurrentPos += pa;
      yCurrentPos += pc;
    }

    // Update the origin point for the next horizontal line
    bgXOrigin[bgNumber - 2] += pb;
    bgYOrigin[bgNumber - 2] += pd;
  }

  private void drawMode4Line(int yScr, int[] pixel) {
    int bgNumber = 2;
    int priority = ioMem.getBGPriority(bgNumber);

    // Specifies which frame the hardware has to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);

    // 20 bits for integers & 8 bits for decimals
    if (yScr == 0) {
      bgXOrigin[bgNumber - 2] = ioMem.getBGRotScalXOrigin(bgNumber);
      bgYOrigin[bgNumber - 2] = ioMem.getBGRotScalYOrigin(bgNumber);
    }

    // 8 bits for integers & 8 bits for decimals
    int pa = ioMem.getBGRotScalPA(bgNumber);
    int pb = ioMem.getBGRotScalPB(bgNumber);
    int pc = ioMem.getBGRotScalPC(bgNumber);
    int pd = ioMem.getBGRotScalPD(bgNumber);

    int xCurrentPos = bgXOrigin[bgNumber - 2];
    int yCurrentPos = bgYOrigin[bgNumber - 2];

    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      int x = (xCurrentPos >> 8);
      int y = (yCurrentPos >> 8);

      if ((x >= 0) && (x < xScreenSize) && (y >= 0) && (y < yScreenSize)) {
        int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(frameIndex + (x + y * xScreenSize));
        short color16 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
        pixel[xScr] = getPixelFragment(priority, color16);
      }
      else {
        pixel[xScr] = killedFragment;
      }
      
      xCurrentPos += pa;
      yCurrentPos += pc;
    }

    // Update the origin point for the next horizontal line
    bgXOrigin[bgNumber - 2] += pb;
    bgYOrigin[bgNumber - 2] += pd;
  }

  private void drawMode5Line(int yScr, int[] pixel) {
    // In this mode, there is only 128 horizontal lines.
    if (yScr >= 128) return;

    int bgNumber = 2;
    int priority          = ioMem.getBGPriority(bgNumber);

    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(bgNumber);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    // Says which frame the hardware have to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);

    for (int xScr = 0; xScr < 160; xScr++) {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      short color16 = vMem.hardwareAccessLoadHalfWord(frameIndex + ((x + y * 160) << 1));
      int color24 = getPixelFragment(priority, color16);
      pixel[xScr] = color24;
    }
  }

  private void drawObj(int yScr, int[] pixel) {
    final int tileDataAddress = 0x00010000;
    final int paletteAddress = 0x00000200;
    final int nbObj = 128;

    // Clear the pixels.
    Arrays.fill(pixel, killedFragment);
    
    for (int priority = 3; priority >= 0; priority--) {
      for (int i = nbObj - 1; i >= 0 ; i--) {
        if (!sMem.isObjWindow(i) && (priority == sMem.getObjPriority(i))) {
          // It is the same priority, so let's draw the sprite ...
          boolean rotScalEnabled    = sMem.isRotScalEnabled(i);
          boolean doubleSizeEnabled = sMem.isDoubleSizeEnabled(i);
          int xNbTile               = sMem.getObjXTile(i);
          int yNbTile               = sMem.getObjYTile(i);
          int xSize                 = xNbTile * 8;
          int ySize                 = yNbTile * 8;
          int paletteNumber         = sMem.getPal16Number(i);
          int xPos                  = sMem.getObjXPos(i);
          int yPos                  = sMem.getObjYPos(i);
          boolean mosaicEnabled     = sMem.isMosaicEnabled(i);
          int xMosaic               = ioMem.getMosaicOBJXSize();
          int yMosaic               = ioMem.getMosaicOBJYSize();
          int tileBaseNumber        = sMem.getTileNumber(i);

          if (rotScalEnabled) {
            boolean is256Color = sMem.is256Color(i);
            int tileNumberYIncr = (ioMem.is1DMappingMode() ?
                                   (is256Color ? xNbTile * 2 : xNbTile)
                                   : 32);

            int xRotCenter = xSize / 2;
            int yRotCenter = ySize / 2;

            // Test if we have to double the size of the render field.
            if (doubleSizeEnabled) {
              xSize *= 2;
              ySize *= 2;
            }

            if (yPos + ySize >= 256) yPos -= 256;

            // (xScr, yScr) is the coordinate in the screen.
            if ((yScr >= yPos) && (yScr < yPos + ySize)) {
              // (x, y) is the coordinate in the sprite.
              int y = yScr - yPos;
              if (mosaicEnabled) y -= (y % yMosaic);

              if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0x0000fffe;
              int rotScalIndex = sMem.getRotScalIndex(i);
              int pa = sMem.getPA(rotScalIndex);
              int pb = sMem.getPB(rotScalIndex);
              int pc = sMem.getPC(rotScalIndex);
              int pd = sMem.getPD(rotScalIndex);
              int xRotCenter2 = xSize / 2;
              int yRotCenter2 = ySize / 2;

              int xScrStart = Math.max(0, xPos);
              int xScrEnd = Math.min(xScreenSize, xPos + xSize);
              for (int xScr = xScrStart; xScr < xScrEnd; xScr++) {
                int x = xScr - xPos;
                if (mosaicEnabled) x -= (x % xMosaic);
                int x2 = (x - xRotCenter2) << 8;
                int y2 = (y - yRotCenter2) << 8;
                // (x3, y3) is the coordinate in the texture of the sprite.
                int x3 = ((pa * x2 + pb * y2) >> 16) + xRotCenter;
                int y3 = ((pc * x2 + pd * y2) >> 16) + yRotCenter;

                // Test if the pixel to render is inside the sprite texture.
                if ((x3 >= 0) && (x3 < xNbTile * 8) && (y3 >= 0) && (y3 < yNbTile * 8)) {
                  int xTile = x3 >>> 3;
                  int yTile = y3 >>> 3;
                  int xSubTile = x3 & 0x07;
                  int ySubTile = y3 & 0x07;

                  if (is256Color) {
                    int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
                    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                    tileNumber * 8*8/2 +
                                                                    xSubTile + ySubTile * 8);
                    // if color is zero, transparent
                    if (color8 != 0) {
                      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
                      pixel[xScr] = getPixelFragment(priority, color15);
                    }
                  }
                  else {
                    int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);
                    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                    tileNumber * 8*8/2 +
                                                                    (xSubTile >>> 1) + ySubTile * 4);
                    int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
                    // if color is zero, transparent
                    if (color4 != 0) {
                      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
                                                                      ((paletteNumber * 16) << 1)+
                                                                      (color4 << 1));
                      pixel[xScr] = getPixelFragment(priority, color15);
                    }
                  }
                }
              }
            }
          }
          else {
            if (!doubleSizeEnabled) {
              boolean is256Color = sMem.is256Color(i);
              int tileNumberYIncr = (ioMem.is1DMappingMode() ?
                                     (is256Color ? xNbTile * 2 : xNbTile)
                                     : 32);
              boolean hFlip = sMem.isHFlipEnabled(i);
              boolean vFlip = sMem.isVFlipEnabled(i);

              if (yPos >= yScreenSize) yPos -= 256;

              if ((yScr >= yPos) && (yScr < yPos + yNbTile * 8)) {
                int y = yScr - yPos;
                if (mosaicEnabled) y -= (y % yMosaic);
                if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0xfffe;

                int xScrStart = Math.max(0, xPos);
                int xScrEnd = Math.min(xScreenSize, xPos + xNbTile * 8);
                for (int xScr = xScrStart; xScr < xScrEnd; xScr++) {
                  int x = xScr - xPos;
                  if (mosaicEnabled) x -= (x % xMosaic);

                  int x2 = (hFlip ? xNbTile * 8 - 1 - x : x); // handle horizontal tile flip
                  int y2 = (vFlip ? yNbTile * 8 - 1 - y : y); // handle vertical tile flip

                  int xTile = x2 >>> 3;
                  int yTile = y2 >>> 3;
                  int xSubTile = x2 & 0x07;
                  int ySubTile = y2 & 0x07;

                  if (is256Color) {
                    int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
                    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                    tileNumber * 8*8/2 +
                                                                    xSubTile + ySubTile * 8);
                    // if color is zero, transparent
                    if (color8 != 0) {
                      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
                      pixel[xScr] = getPixelFragment(priority, color15);
                    }
                  }
                  else {
                    int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);

                    // To prevent VRAM Overflow ..
                    //tileNumber &= 0x000001ff;

                    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                    tileNumber * 8*8/2 +
                                                                    (xSubTile >>> 1) + ySubTile * 4);
                    int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
                    // if color is zero, transparent
                    if (color4 != 0) {
                      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
                                                                      ((paletteNumber * 16) << 1)+
                                                                      (color4 << 1));
                      pixel[xScr] = getPixelFragment(priority, color15);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private void drawObjWindow(int yScr, boolean[] objWindow) {
    final int tileDataAddress = 0x00010000;
    final int nbObj = 128;

    // Clear the flags.
    Arrays.fill(objWindow, false);

    for (int i = 0; i < nbObj; i++) {
      if (sMem.isObjWindow(i)) {
        // It is the same priority, so let's draw the sprite ...
        boolean rotScalEnabled    = sMem.isRotScalEnabled(i);
        boolean doubleSizeEnabled = sMem.isDoubleSizeEnabled(i);
        int xNbTile               = sMem.getObjXTile(i);
        int yNbTile               = sMem.getObjYTile(i);
        int xSize                 = xNbTile * 8;
        int ySize                 = yNbTile * 8;
        int xPos                  = sMem.getObjXPos(i);
        int yPos                  = sMem.getObjYPos(i);
        boolean mosaicEnabled     = sMem.isMosaicEnabled(i);
        int xMosaic               = ioMem.getMosaicOBJXSize();
        int yMosaic               = ioMem.getMosaicOBJYSize();
        int tileBaseNumber        = sMem.getTileNumber(i);

        if (rotScalEnabled) {
          boolean is256Color = sMem.is256Color(i);
          int tileNumberYIncr = (ioMem.is1DMappingMode() ?
                                 (is256Color ? xNbTile * 2 : xNbTile)
                                 : 32);

          int xRotCenter = xSize / 2;
          int yRotCenter = ySize / 2;

          // Test if we have to double the size of the render field.
          if (doubleSizeEnabled) {
            xSize *= 2;
            ySize *= 2;
          }

          if (yPos + ySize >= 256) yPos -= 256;

          // (xScr, yScr) is the coordinate in the screen.
          if ((yScr >= yPos) && (yScr < yPos + ySize)) {
            // (x, y) is the coordinate in the sprite.
            int y = yScr - yPos;
            if (mosaicEnabled) y -= (y % yMosaic);

            if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0x0000fffe;
            int rotScalIndex = sMem.getRotScalIndex(i);
            int pa = sMem.getPA(rotScalIndex);
            int pb = sMem.getPB(rotScalIndex);
            int pc = sMem.getPC(rotScalIndex);
            int pd = sMem.getPD(rotScalIndex);
            int xRotCenter2 = xSize / 2;
            int yRotCenter2 = ySize / 2;

            int xScrStart = Math.max(0, xPos);
            int xScrEnd = Math.min(xScreenSize, xPos + xSize);
            for (int xScr = xScrStart; xScr < xScrEnd; xScr++) {
              int x = xScr - xPos;
              if (mosaicEnabled) x -= (x % xMosaic);
              int x2 = (x - xRotCenter2) << 8;
              int y2 = (y - yRotCenter2) << 8;
              // (x3, y3) is the coordinate in the texture of the sprite.
              int x3 = ((pa * x2 + pb * y2) >> 16) + xRotCenter;
              int y3 = ((pc * x2 + pd * y2) >> 16) + yRotCenter;

              // Test if the pixel to render is inside the sprite texture.
              if ((x3 >= 0) && (x3 < xNbTile * 8) && (y3 >= 0) && (y3 < yNbTile * 8)) {
                int xTile = x3 >>> 3;
                int yTile = y3 >>> 3;
                int xSubTile = x3 & 0x07;
                int ySubTile = y3 & 0x07;

                if (is256Color) {
                  int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
                  int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                  tileNumber * 8*8/2 +
                                                                  xSubTile + ySubTile * 8);
                  // if color is zero, transparent
                  if (color8 != 0)
                    objWindow[xScr] = true;
                }
                else {
                  int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);
                  int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                  tileNumber * 8*8/2 +
                                                                  (xSubTile >>> 1) + ySubTile * 4);
                  int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
                  // if color is zero, transparent
                  if (color4 != 0)
                    objWindow[xScr] = true;
                }
              }
            }
          }
        }
        else {
          if (!doubleSizeEnabled) {
            boolean is256Color = sMem.is256Color(i);
            int tileNumberYIncr = (ioMem.is1DMappingMode() ?
                                   (is256Color ? xNbTile * 2 : xNbTile)
                                   : 32);
            boolean hFlip = sMem.isHFlipEnabled(i);
            boolean vFlip = sMem.isVFlipEnabled(i);

            if (yPos >= yScreenSize) yPos -= 256;

            if ((yScr >= yPos) && (yScr < yPos + yNbTile * 8)) {
              int y = yScr - yPos;
              if (mosaicEnabled) y -= (y % yMosaic);
              if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0xfffe;

              int xScrStart = Math.max(0, xPos);
              int xScrEnd = Math.min(xScreenSize, xPos + xNbTile * 8);
              for (int xScr = xScrStart; xScr < xScrEnd; xScr++) {
                int x = xScr - xPos;
                if (mosaicEnabled) x -= (x % xMosaic);

                int x2 = (hFlip ? xNbTile * 8 - 1 - x : x); // handle horizontal tile flip
                int y2 = (vFlip ? yNbTile * 8 - 1 - y : y); // handle vertical tile flip

                int xTile = x2 >>> 3;
                int yTile = y2 >>> 3;
                int xSubTile = x2 & 0x07;
                int ySubTile = y2 & 0x07;

                if (is256Color) {
                  int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
                  int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                  tileNumber * 8*8/2 +
                                                                  xSubTile + ySubTile * 8);
                  // if color is zero, transparent
                  if (color8 != 0)
                    objWindow[xScr] = true;
                }
                else {
                  int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);

                  // To prevent VRAM Overflow ..
                  //tileNumber &= 0x000001ff;

                  int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                  tileNumber * 8*8/2 +
                                                                  (xSubTile >>> 1) + ySubTile * 4);
                  int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
                  // if color is zero, transparent
                  if (color4 != 0)
                    objWindow[xScr] = true;
                }
              }
            }
          }
        }
      }
    }
  }
  
  private int getPixelFragment(int priority, short color) {
    return ((priority << 16) | (color & 0xffff));
  }

}
