package com.lemoulinstudio.gfa.core.gfx;

public class LcdGen2 extends Lcd {

  private int[][] bgLine = new int[4][xScreenSize];
  private int[][] sprLine = new int[4][xScreenSize];

  public void drawLine(int y) {
    if (y >= yScreenSize)
      return;

    int graphicMode = ioMem.getGfxMode();

    boolean isBG0Enabled = ioMem.isBGEnabled(0);
    boolean isBG1Enabled = ioMem.isBGEnabled(1);
    boolean isBG2Enabled = ioMem.isBGEnabled(2);
    boolean isBG3Enabled = ioMem.isBGEnabled(3);

    // Fill the required background buffers.
    switch (graphicMode) {
      case 0:
          if (isBG0Enabled) drawBGTextModeLine(0, y, bgLine[0]);
          if (isBG1Enabled) drawBGTextModeLine(1, y, bgLine[1]);
          if (isBG2Enabled) drawBGTextModeLine(2, y, bgLine[2]);
          if (isBG3Enabled) drawBGTextModeLine(3, y, bgLine[3]);
        break;

      case 1:
          if (isBG0Enabled) drawBGTextModeLine(0, y, bgLine[0]);
          if (isBG1Enabled) drawBGTextModeLine(1, y, bgLine[1]);
          if (isBG2Enabled) drawBGRotScalModeLine(2, y, bgLine[2]);
        break;

      case 2:
          if (isBG2Enabled) drawBGRotScalModeLine(2, y, bgLine[2]);
          if (isBG3Enabled) drawBGRotScalModeLine(3, y, bgLine[3]);
        break;

      case 3:
          if (isBG2Enabled) drawMode3Line(y, bgLine[2]);
        break;

      case 4:
          if (isBG2Enabled) drawMode4Line(y, bgLine[2]);
        break;

      case 5:
          if (isBG2Enabled) drawMode5Line(y, bgLine[2]);
        break;
    }

    boolean isSPREnabled = ioMem.isSpriteEnabled();
    if (isSPREnabled) {
      for (int priority = 0; priority < 4; priority++)
        drawSprites(y, priority, sprLine[priority]);
    }
    
    int priorityBG0 = ioMem.getBGPriority(0);
    int priorityBG1 = ioMem.getBGPriority(1);
    int priorityBG2 = ioMem.getBGPriority(2);
    int priorityBG3 = ioMem.getBGPriority(3);

    // Mix the backgrounds together.
    for (int priority = 3; priority >= 0; priority--) {
      if (isBG3Enabled && (priorityBG3 == priority)) ; // Background
      if (isBG2Enabled && (priorityBG2 == priority)) ; // ...
      if (isBG1Enabled && (priorityBG1 == priority)) ; // ...
      if (isBG0Enabled && (priorityBG0 == priority)) ; // Foreground
      if (isSPREnabled) ; // Only sprites with this priority
    }

    // After the draw of the last screen line, update the display.
    if (y == 159) updatePixels();
  }

  private void drawUnicolorBackground(int y, int[] pixel) {
    int backgroundColor = color15BitsTo24Bits(pMem.hardwareAccessLoadHalfWord(0));
    for (int xScr = 0; xScr < xScreenSize; xScr++)
      pixel[xScr] = backgroundColor;
  }

  private void drawBGTextModeLine(int bgNumber, int yScr, int[] pixel) {
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
          pixel[xScr] = 0;
        }
        else { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  pixel[xScr] = color15BitsTo24Bits(color15);
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
          pixel[xScr] = 0;
        }
        else { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord((paletteNumber * 16 + color4) << 1);
	  pixel[xScr] = color15BitsTo24Bits(color15);
	}
      }
      
    }
  }

  private void drawBGRotScalModeLine(int bgNumber, int y, int[] pixel) {
    int mapAddress        = ioMem.getBGTileMapAddress(bgNumber);
    int dataAddress       = ioMem.getBGTileDataAddress(bgNumber);
    int numberOfTile      = ioMem.getBGRotScalNumberOfTile(bgNumber);
    int numberOfTileMask  = numberOfTile - 1;
    boolean isWrapAround  = ioMem.isBGRotScalWrapAround(bgNumber);
    
    // 20 bits for integers & 8 bits for decimals
    if (y == 0) {
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
    
    for (int x = 0; x < xScreenSize; x++) {
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
	if (color8 != 0) { // if color is zero, transparent
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  pixel[x + y * xScreenSize] = color15BitsTo24Bits(color15);
	}
      }
      
      xCurrentPos += pa;
      yCurrentPos += pc;
    }
    
    // Update the origin point for the next horizontal line
    bgXOrigin[bgNumber - 2] += pb;
    bgYOrigin[bgNumber - 2] += pd;
  }

  /**
   * Draw the line y of the screen the mode 3 way.
   * This mode is a 16bbp bitmap mode.
   * Each pixel is encoded in a half word.
   * Only 1 frame buffer can be used in this mode
   * since it use all the video memory space.
   */
  private void drawMode3Line(int yScr, int[] pixel) {
    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      short color16 = vMem.hardwareAccessLoadHalfWord((x + y * xScreenSize) << 1);
      pixel[xScr] = color15BitsTo24Bits(color16);
    }
  }

  /**
   * Draw the line y of the screen the mode 4 way.
   * This mode is a palette-based 8bbp bitmap mode.
   * Each pixel is encoded in a byte.
   * In this mode, the amount of memory
   * enable the developer to use 2 frame buffer.
   */
  private void drawMode4Line(int yScr, int[] pixel) {
    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    // Say which frame the hardware have to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);

    for (int xScr = 0; xScr < xScreenSize; xScr++) {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(frameIndex + (x + y * xScreenSize));
      short color16 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
      pixel[xScr] = color15BitsTo24Bits(color16);
    }
  }

  private void drawMode5Line(int yScr, int[] pixel) {
    // In this mode, there is only 128 horizontal lines.
    if (yScr >= 128) return;

    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    // Says which frame the hardware have to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);

    for (int xScr = 0; xScr < 160; xScr++) {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      short color16 = vMem.hardwareAccessLoadHalfWord(frameIndex + ((x + y * 160) << 1));
      int color24 = color15BitsTo24Bits(color16);
      pixel[xScr] = color24;
    }
  }

  private void drawSprites(int yScr, int priority, int[] pixel) {
    final int tileDataAddress = 0x00010000;
    int nbSprite = 128;
    for (int i = nbSprite - 1; i >= 0 ; i--) {
      if (priority == sMem.getSpritePriority(i)) {
        // It is the same priority, so let's draw the sprite ...
	boolean rotScalEnabled    = sMem.isRotScalEnabled(i);
	boolean doubleSizeEnabled = sMem.isDoubleSizeEnabled(i);
        int xNbTile               = sMem.getSpriteNumberXTile(i);
        int yNbTile               = sMem.getSpriteNumberYTile(i);
	int xSize                 = xNbTile * 8;
	int ySize                 = yNbTile * 8;
        int paletteAddress        = 0x00000200;
	int paletteNumber         = sMem.getPal16Number(i);
        int xPos                  = sMem.getSpriteXPos(i);
        int yPos                  = sMem.getSpriteYPos(i);
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
                  if (color8 != 0) { // if color is zero, transparent
                    short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
                    pixel[xScr] = color15BitsTo24Bits(color15);
                  }
                }
                else {
                  int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);
                  int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
                                                                  tileNumber * 8*8/2 +
                                                                  (xSubTile >>> 1) + ySubTile * 4);
                  int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
                  if (color4 != 0) { // if color is zero, transparent
                    short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
                                                                    ((paletteNumber * 16) << 1)+
                                                                    (color4 << 1));
                    pixel[xScr] = color15BitsTo24Bits(color15);
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
                  if (color8 != 0) { // if color is zero, transparent
                    short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
                    pixel[xScr] = color15BitsTo24Bits(color15);
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
                  if (color4 != 0) { // if color is zero, transparent
                    short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
                                                                    ((paletteNumber * 16) << 1)+
                                                                    (color4 << 1));
                    pixel[xScr] = color15BitsTo24Bits(color15);
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
