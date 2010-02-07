package gfa.gfx;

import gfa.memory.*;
import gfa.util.*;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;
import java.awt.image.*;
import java.util.List;

public class Lcd
  implements ImageProducer
{
  protected int[] rawPixels;
  protected DirectColorModel model;
  protected List<ImageConsumer> consumers;
  protected Image image;

  public final int xScreenSize = 240;
  public final int yScreenSize = 160;

  protected IORegisterSpace_8_16_32   ioMem; // registers
  protected MemoryManagementUnit_16_32 pMem; // palette
  protected MemoryManagementUnit_16_32 vMem; // video
  protected ObjectAttributMemory_16_32 sMem; // sprite

  public Lcd()
  {
    rawPixels = new int[xScreenSize * yScreenSize];
    model = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
    image = Toolkit.getDefaultToolkit().createImage(this);
    consumers = new Vector<ImageConsumer>();
  }

  public Image getImage()
  {
    return image;
  }
  
  public void addConsumer(ImageConsumer ic)
  {
    ic.setDimensions(xScreenSize, yScreenSize);
    ic.setHints(ic.TOPDOWNLEFTRIGHT |
		ic.COMPLETESCANLINES |
		ic.SINGLEPASS |
		ic.SINGLEFRAME);
    ic.setColorModel(model);
    consumers.add(ic);
  }

  public boolean isConsumer(ImageConsumer ic)
  {
    return consumers.contains(ic);
  }

  public void removeConsumer(ImageConsumer ic)
  {
    consumers.remove(ic);
  }

  public void startProduction(ImageConsumer ic)
  {
    addConsumer(ic);
  }

  public void requestTopDownLeftRightResend(ImageConsumer ic)
  {
    // Do nothing.
    // No need to resend data since images are sent frame per frame :
    // the next sent image will be the next frame.
  }

  public void updatePixels()
  {
    //System.out.println("consumers.size() = " + consumers.size());
    for (int i = 0; i < consumers.size(); i++)
    {
      ImageConsumer ic = consumers.get(i);
      ic.setPixels(0, 0, xScreenSize, yScreenSize, model, rawPixels, 0, xScreenSize);
      ic.imageComplete(ic.SINGLEFRAMEDONE);
    }
  }

  public void connectToMemory(GfaMMU memory)
  {
    ioMem = (IORegisterSpace_8_16_32)    memory.getMemoryBank(0x04); // registers
    pMem  = (MemoryManagementUnit_16_32) memory.getMemoryBank(0x05); // palette
    vMem  = (MemoryManagementUnit_16_32) memory.getMemoryBank(0x06); // video
    sMem  = (ObjectAttributMemory_16_32) memory.getMemoryBank(0x07); // sprite
  }

  public void drawLine(int y)
  {
    if (y < yScreenSize)
    {
      //if (y == 159) System.out.println("Mode " + ioMem.getGfxMode());
      switch(ioMem.getGfxMode())
      {
        case 0: drawMode0Line(y); break;
        case 1: drawMode1Line(y); break;
        case 2: drawMode2Line(y); break;
        case 3: drawMode3Line(y); break;
        case 4: drawMode4Line(y); break;
        case 5: drawMode5Line(y); break;
        default: // unknown video mode, draw nothing.
      }
      
      // After the draw of the last screen line, update the display.
      if (y == 159) updatePixels();
    }
  }

  /**
   * Draw the line y of the screen in the mode 0 way.
   * This mode is a 8x8 tile-based mode.
   * It have 4 backgrounds which can independantly
   * scroll on the x and y axis.
   * Each one can be enabled or disabled of rendering.
   */
  protected void drawMode0Line(int y)
  {
    boolean isBG0Enabled = ioMem.isBGEnabled(0);
    boolean isBG1Enabled = ioMem.isBGEnabled(1);
    boolean isBG2Enabled = ioMem.isBGEnabled(2);
    boolean isBG3Enabled = ioMem.isBGEnabled(3);
    boolean isSPREnabled = ioMem.isSpriteEnabled();
    
    int priorityBG0 = ioMem.getBGPriority(0);
    int priorityBG1 = ioMem.getBGPriority(1);
    int priorityBG2 = ioMem.getBGPriority(2);
    int priorityBG3 = ioMem.getBGPriority(3);
    
    drawBackground(y);
    
    for (int priority = 3; priority >= 0; priority--)
    {
      if (isBG3Enabled && (priorityBG3 == priority)) drawBGTextModeLine(3, y); // Background
      if (isBG2Enabled && (priorityBG2 == priority)) drawBGTextModeLine(2, y); // ...
      if (isBG1Enabled && (priorityBG1 == priority)) drawBGTextModeLine(1, y); // ...
      if (isBG0Enabled && (priorityBG0 == priority)) drawBGTextModeLine(0, y); // Foreground
      if (isSPREnabled) drawSprites(y, priority); // Only sprites with this priority
    }
  }

  protected void drawMode1Line(int y)
  {
    boolean isBG0Enabled = ioMem.isBGEnabled(0);
    boolean isBG1Enabled = ioMem.isBGEnabled(1);
    boolean isBG2Enabled = ioMem.isBGEnabled(2);
    boolean isSPREnabled = ioMem.isSpriteEnabled();

    int priorityBG0 = ioMem.getBGPriority(0);
    int priorityBG1 = ioMem.getBGPriority(1);
    int priorityBG2 = ioMem.getBGPriority(2);
    
    drawBackground(y);
    
    for (int priority = 3; priority >= 0; priority--)
    {
      if (isBG2Enabled && (priorityBG2 == priority)) drawBG2RotScalModeLine(y);
      if (isBG1Enabled && (priorityBG1 == priority)) drawBGTextModeLine(1, y);
      if (isBG0Enabled && (priorityBG0 == priority)) drawBGTextModeLine(0, y);
      if (isSPREnabled) drawSprites(y, priority);
    }
  }

  protected void drawMode2Line(int y)
  {
    boolean isBG2Enabled = ioMem.isBGEnabled(2);
    boolean isBG3Enabled = ioMem.isBGEnabled(3);
    boolean isSPREnabled = ioMem.isSpriteEnabled();
    
    int priorityBG2 = ioMem.getBGPriority(2);
    int priorityBG3 = ioMem.getBGPriority(3);
    
    drawBackground(y);
    
    for (int priority = 3; priority >= 0; priority--)
    {
      if (isBG3Enabled && (priorityBG3 == priority)) drawBG3RotScalModeLine(y);
      if (isBG2Enabled && (priorityBG2 == priority)) drawBG2RotScalModeLine(y);
      if (isSPREnabled) drawSprites(y, priority);
    }
  }

  /**
   * Draw the line y of the screen in the mode 3 way.
   * This mode is a 16bbp bitmap mode.
   * Each pixel is encoded in a half word.
   * Only 1 frame buffer can be use in this mode
   * since it use all the video memory space.
   */
  protected void drawMode3Line(int yScr)
  {
    boolean isBG2Enabled  = ioMem.isBGEnabled(2);
    if (!isBG2Enabled) return;
    
    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);
    
    for (int xScr = 0; xScr < xScreenSize; xScr++)
    {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      short color16 = vMem.hardwareAccessLoadHalfWord((x + y * xScreenSize) << 1);
      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color16);
    }

    if (ioMem.isSpriteEnabled())
      for (int priority = 3; priority >= 0; priority--)
	  drawSprites(y, priority);
  }

  /**
   * Draw the line y of the screen in the mode 4 way.
   * This mode is a paletted 8bbp bitmap mode.
   * Each pixel is encoded in a byte.
   * In this mode, the amount of memory
   * able the developper to use 2 frame buffer.
   */
  protected void drawMode4Line(int yScr)
  {
    boolean isBG2Enabled  = ioMem.isBGEnabled(2);
    if (!isBG2Enabled) return;
    
    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    // Say which frame the hardware have to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);
    
    for (int xScr = 0; xScr < xScreenSize; xScr++)
    {
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(frameIndex + (x + y * xScreenSize));
      short color16 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color16);
    }
    
    if (ioMem.isSpriteEnabled())
      for (int priority = 3; priority >= 0; priority--)
	  drawSprites(y, priority);
  }

  protected void drawMode5Line(int yScr)
  {
    boolean isBG2Enabled  = ioMem.isBGEnabled(2);
    if (!isBG2Enabled) return;
    
    // In this mode, there is only 128 horizontal lines.
    if (yScr >= 128) return;
    
    // handle the mosaic effect
    boolean mosaicEnabled = ioMem.isBGMosaicEnabled(2);
    int xMosaic           = ioMem.getMosaicBGXSize();
    int yMosaic           = ioMem.getMosaicBGYSize();
    int y                 = (mosaicEnabled ? yScr - (yScr % yMosaic) : yScr);

    // Say which frame the hardware have to display.
    int frameIndex = (ioMem.isFrame1Mode() ? 0x0000a000 : 0);
    
    boolean isDoubleLine = ((yScr % 4) == 3); // shoul we double the line ?
    yScr = (yScr * 5) / 4;
    int dst = yScr * xScreenSize;
    
    for (int xScr = 0; xScr < 160; xScr++)
    {
      int x = (mosaicEnabled ? xScr - (xScr % yMosaic) : xScr);
      short color16 = vMem.hardwareAccessLoadHalfWord(frameIndex + ((x + y * 160) << 1));
      int color24 = color15BitsTo24Bits(color16);
      rawPixels[dst] = color24;
      if (isDoubleLine) // double this pixel on the vertical axis.
	rawPixels[dst + xScreenSize] = color24;
      dst++;
      
      if ((xScr % 2) == 1) // double this pixel on the horizontal axis.
      {
	rawPixels[dst] = color24;
	if (isDoubleLine) // double this pixel on the vetical axis.
	  rawPixels[dst + xScreenSize] = color24;
	dst++;
      }

    }
    
    if (ioMem.isSpriteEnabled())
      for (int priority = 3; priority >= 0; priority--)
        drawSprites(y, priority);
  }
  
  protected void drawBackground(int y)
  {
    int backgroundColor = color15BitsTo24Bits(pMem.hardwareAccessLoadHalfWord(0));
    int beginPixel = y * xScreenSize;
    int endPixel = (y + 1) * xScreenSize;
    for (int i = beginPixel; i < endPixel; i++)
      rawPixels[i] = backgroundColor;
  }

  protected void drawBGTextModeLine(int bgNumber, int yScr)
  {
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
    
    for (int xScr = 0; xScr < xScreenSize; xScr++)
    {
      // This routine acts like a raycasting algorithm.
      // It might be slow, but I DON'T CARE !! :-)
      // Mame rulezz.
      
      // handle the mosaic effect
      int x = (mosaicEnabled ? xScr - (xScr % xMosaic) : xScr);
      
      int tileX = (x + xScroll) >>> 3; // the x coordinate of the tile where to find the pixel.
      int tileY = (y + yScroll) >>> 3; // The y coordinate of the tile where to find the pixel.
      
      // handle the wraparound effect.
      tileX &= xNumberOfTileMask;
      tileY &= yNumberOfTileMask;
      
      int offsetToAdd = 0;
      if (isTileDataUpsideDown)
      {
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
      
      if (is256Color) // Tile are encoded in a 1 byte per pixel format
      {
	int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(0x0000ffff &
							      (dataAddress +
							       tileNumber * 8*8 +
							       xSubTile + ySubTile * 8));
	if (color8 != 0) // if color is zero, transparent
	{
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
	}
      }
      else // Tiles are encoded in a 4 bits per pixel format
      {
	int paletteNumber = (tileData & 0x0000f000) >> 12;
	int color4 = 0x000000ff & vMem.hardwareAccessLoadByte(0x0000ffff &
							      (dataAddress +
							       tileNumber * 8*8/2 +
							       ((xSubTile + ySubTile * 8) >> 1)));
	if ((xSubTile & 0x01) != 0) color4 >>>= 4;
	else color4 &= 0x0f;
	if (color4 != 0) // if color is zero, transparent
	{
	  short color15 = pMem.hardwareAccessLoadHalfWord((paletteNumber * 16 + color4) << 1);
	  rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
	}
      }
      
    }
  }

  public void drawSprites(int yScr, int priority)
  {
    final int tileDataAddress = 0x00010000;
    int nbSprite = 128;
    for (int i = nbSprite - 1; i >= 0 ; i--)
    {
      if (priority == sMem.getSpritePriority(i))
      {
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

	if (rotScalEnabled)
	{
	  boolean is256Color = sMem.is256Color(i);
	  int tileNumberYIncr = (ioMem.is1DMappingMode() ?
				 (is256Color ? xNbTile * 2 : xNbTile)
				 : 32);
	  
	  int xRotCenter = xSize / 2;
	  int yRotCenter = ySize / 2;

	  // Test if we have to double the size of the render field.
	  if (doubleSizeEnabled)
	  {
	    xSize *= 2;
	    ySize *= 2;
	  }
	  
          if (yPos + ySize >= 256) yPos -= 256;
          
	  // (xScr, yScr) is the coordinate in the screen.
	  if ((yScr >= yPos) && (yScr < yPos + ySize))
	  {
	    // (x, y) is the coordinate in the sprite.
	    int y = yScr - yPos;
	    if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0x0000fffe;
	    int rotScalIndex = sMem.getRotScalIndex(i);
	    int pa = sMem.getPA(rotScalIndex);
	    int pb = sMem.getPB(rotScalIndex);
	    int pc = sMem.getPC(rotScalIndex);
	    int pd = sMem.getPD(rotScalIndex);
	    int xRotCenter2 = xSize / 2;
	    int yRotCenter2 = ySize / 2;
	    
	    for (int x = 0; x < xSize; x++)
	    {
	      int xScr = xPos + x;
	      if ((xScr >= 0) && (xScr < xScreenSize))
	      {
		int x2 = (x - xRotCenter2) << 8;
		int y2 = (y - yRotCenter2) << 8;
		// (x3, y3) is the coordinate in the texture of the sprite.
		int x3 = ((pa * x2 + pb * y2) >> 16) + xRotCenter;
		int y3 = ((pc * x2 + pd * y2) >> 16) + yRotCenter;
		
		// Test if the pixel to render is inside the sprite texture.
		if ((x3 >= 0) && (x3 < xNbTile * 8) && (y3 >= 0) && (y3 < yNbTile * 8))
		{
		  int xTile = x3 >>> 3;
		  int yTile = y3 >>> 3;
		  int xSubTile = x3 & 0x07;
		  int ySubTile = y3 & 0x07;

		  if (is256Color)
		  {
		    int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
		    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
								    tileNumber * 8*8/2 +
								    xSubTile + ySubTile * 8);
		    if (color8 != 0) // if color is zero, transparent
		    {
		      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
		      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
		    }
		  }
		  else
		  {
		    int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);
		    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
								    tileNumber * 8*8/2 +
								    (xSubTile >>> 1) + ySubTile * 4);
		    int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
		    if (color4 != 0) // if color is zero, transparent
		    {
		      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
								      ((paletteNumber * 16) << 1)+
								      (color4 << 1));
		      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
		    }
		  }
		}
	      }
	    }
	  }
	}
	else
	{
          if (!doubleSizeEnabled)
	  {
	    boolean is256Color = sMem.is256Color(i);
	    int tileNumberYIncr = (ioMem.is1DMappingMode() ?
				   (is256Color ? xNbTile * 2 : xNbTile)
				   : 32);
	    boolean hFlip = sMem.isHFlipEnabled(i);
	    boolean vFlip = sMem.isVFlipEnabled(i);
	    
            if (yPos >= yScreenSize) yPos -= 256;
            
	    if ((yScr >= yPos) && (yScr < yPos + yNbTile * 8))
	    {
	      int y = yScr - yPos;
	      if (is256Color && !ioMem.is1DMappingMode()) tileBaseNumber &= 0xfffe;
	      
	      for (int x = 0; x < xNbTile * 8; x++)
	      {
		int xScr = xPos + x;
		if ((xScr >= 0) && (xScr < xScreenSize))
		{
		  int x2 = (hFlip ? xNbTile * 8 - 1 - x : x); // handle horizontal tile flip
		  int y2 = (vFlip ? yNbTile * 8 - 1 - y : y); // handle vertical tile flip
		  
		  int xTile = x2 >>> 3;
		  int yTile = y2 >>> 3;
		  int xSubTile = x2 & 0x07;
		  int ySubTile = y2 & 0x07;

		  if (is256Color)
		  {
		    int tileNumber = (tileBaseNumber + xTile * 2 + yTile * tileNumberYIncr);
		    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
								    tileNumber * 8*8/2 +
								    xSubTile + ySubTile * 8);
		    if (color8 != 0) // if color is zero, transparent
		    {
		      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress + (color8 << 1));
		      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
		    }
		  }
		  else
		  {
		    int tileNumber = (tileBaseNumber + xTile + yTile * tileNumberYIncr);
		    
		    // To prevent VRAM Overflow ..
		    //tileNumber &= 0x000001ff;
		    
		    int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(tileDataAddress +
								    tileNumber * 8*8/2 +
								    (xSubTile >>> 1) + ySubTile * 4);
		    int color4 = (color8 >>> ((xSubTile & 0x01) * 4)) & 0x0f;
		    if (color4 != 0) // if color is zero, transparent
		    {
		      short color15 = pMem.hardwareAccessLoadHalfWord(paletteAddress +
								      ((paletteNumber * 16) << 1)+
								      (color4 << 1));
		      rawPixels[xScr + yScr * xScreenSize] = color15BitsTo24Bits(color15);
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
  
  protected int bg2XOrigin = 0;
  protected int bg2YOrigin = 0;
  
  public void updateBG2XOriginL(short value)
  {bg2XOrigin = (bg2XOrigin & 0xffff0000) | (value & 0x0000ffff);}
  
  public void updateBG2XOriginH(short value)
  {bg2XOrigin = (value << 16) | (bg2XOrigin & 0x0000ffff);}
  
  public void updateBG2YOriginL(short value)
  {bg2YOrigin = (bg2YOrigin & 0xffff0000) | (value & 0x0000ffff);}
  
  public void updateBG2YOriginH(short value)
  {bg2YOrigin = (value << 16) | (bg2YOrigin & 0x0000ffff);}
  
  protected void drawBG2RotScalModeLine(int y)
  {
    int mapAddress        = ioMem.getBGTileMapAddress(2);
    int dataAddress       = ioMem.getBGTileDataAddress(2);
    int numberOfTile      = ioMem.getBG2RotScalNumberOfTile();
    int numberOfTileMask  = numberOfTile - 1;
    boolean isWrapAround  = ioMem.isBG2RotScalWrapAround();
    
    // 20 bits for integers & 8 bits for decimals
    if (y == 0) {
      bg2XOrigin = ioMem.getBG2RotScalXOrigin();
      bg2YOrigin = ioMem.getBG2RotScalYOrigin();
    }
    
    // 8 bits for integers & 8 bits for decimals
    int pa = ioMem.getBG2RotScalPA();
    int pb = ioMem.getBG2RotScalPB();
    int pc = ioMem.getBG2RotScalPC();
    int pd = ioMem.getBG2RotScalPD();
    
    int xCurrentPos = bg2XOrigin;
    int yCurrentPos = bg2YOrigin;
    
    for (int x = 0; x < xScreenSize; x++)
    {
      // Determinate the source of pixel to display
      int xTile = (xCurrentPos >> 8) >>> 3;
      int yTile = (yCurrentPos >> 8) >>> 3;
      
      if ((isWrapAround) ||
	  ((xTile >= 0) && (xTile < numberOfTile) &&
	   (yTile >= 0) && (yTile < numberOfTile)))
      {
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
	if (color8 != 0) // if color is zero, transparent
	{
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  rawPixels[x + y * xScreenSize] = color15BitsTo24Bits(color15);
	}
      }
      
      xCurrentPos += pa;
      yCurrentPos += pc;
    }
    
    // Update the origin point for the next horizontal line
    bg2XOrigin += pb;
    bg2YOrigin += pd;
  }

  protected int bg3XOrigin = 0;
  protected int bg3YOrigin = 0;
  
  public void updateBG3XOriginL(short value)
  {bg3XOrigin = (bg3XOrigin & 0xffff0000) | (value & 0x0000ffff);}
  
  public void updateBG3XOriginH(short value)
  {bg3XOrigin = (value << 16) | (bg3XOrigin & 0x0000ffff);}
  
  public void updateBG3YOriginL(short value)
  {bg3YOrigin = (bg3YOrigin & 0xffff0000) | (value & 0x0000ffff);}
  
  public void updateBG3YOriginH(short value)
  {bg3YOrigin = (value << 16) | (bg3YOrigin & 0x0000ffff);}
  
  protected void drawBG3RotScalModeLine(int y)
  {
    int mapAddress        = ioMem.getBGTileMapAddress(3);
    int dataAddress       = ioMem.getBGTileDataAddress(3);
    int numberOfTile      = ioMem.getBG3RotScalNumberOfTile();
    int numberOfTileMask  = numberOfTile - 1;
    boolean isWrapAround  = ioMem.isBG3RotScalWrapAround();
    
    // 20 bits for integers & 8 bits for decimals
    if (y == 0) bg3XOrigin = ioMem.getBG3RotScalXOrigin();
    if (y == 0) bg3YOrigin = ioMem.getBG3RotScalYOrigin();
    
    // 8 bits for integers & 8 bits for decimals
    int pa = ioMem.getBG3RotScalPA();
    int pb = ioMem.getBG3RotScalPB();
    int pc = ioMem.getBG3RotScalPC();
    int pd = ioMem.getBG3RotScalPD();
    
    int xCurrentPos = bg3XOrigin;
    int yCurrentPos = bg3YOrigin;
    
    for (int x = 0; x < xScreenSize; x++)
    {
      // Determinate the source of pixel to display
      int xTile = (xCurrentPos >> 8) >>> 3;
      int yTile = (yCurrentPos >> 8) >>> 3;
      
      if ((isWrapAround) ||
	  ((xTile >= 0) && (xTile < numberOfTile) &&
	   (yTile >= 0) && (yTile < numberOfTile)))
      {
	// handle the wraparound effect.
	xTile &= numberOfTileMask;
	yTile &= numberOfTileMask;
	
	int xSubTile = (xCurrentPos >> 8) & 0x07;
	int ySubTile = (yCurrentPos >> 8) & 0x07;
	
	int tileNumber = 0x00000000ff & vMem.hardwareAccessLoadByte(mapAddress + xTile + yTile * numberOfTile);
	
	int color8 = 0x000000ff & vMem.hardwareAccessLoadByte(dataAddress +
							tileNumber * 8*8 +
							xSubTile + ySubTile * 8);
	if (color8 != 0) // if color is zero, transparent
	{
	  short color15 = pMem.hardwareAccessLoadHalfWord(color8 << 1);
	  rawPixels[x + y * xScreenSize] = color15BitsTo24Bits(color15);
	}
      }
      
      xCurrentPos += pa;
      yCurrentPos += pc;
    }
    
    // Update the origin point for the next horizontal line
    bg3XOrigin += pb;
    bg3YOrigin += pd;
  }

  protected int color15BitsTo24Bits(short color15)
  {
    // the 15 bits format is "?bbbbbgggggrrrrr"
    int r = ((color15 & 0x0000001f) >>> 0)  << 3;
    int g = ((color15 & 0x000003e0) >>> 5)  << 3;
    int b = ((color15 & 0x00007c00) >>> 10) << 3;
    // the 32 bits format is "11111111rrrrrrrrggggggggbbbbbbbb"
    return (0xff000000 | (r << 16) | (g << 8) | b); // Alpha channel is full value.
  }
}
