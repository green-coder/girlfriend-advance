package com.lemoulinstudio.gfa.core.gfx;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.MemoryManagementUnit_16_32;
import com.lemoulinstudio.gfa.core.memory.ObjectAttributMemory_16_32;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Lcd implements ImageProducer {

  private final ColorModel colorModel;
  private final List<ImageConsumer> consumerList;
  protected final int[] rawPixels;

  public final static int xScreenSize = 240;
  public final static int yScreenSize = 160;

  protected IORegisterSpace_8_16_32   ioMem; // registers
  protected MemoryManagementUnit_16_32 pMem; // palette
  protected MemoryManagementUnit_16_32 vMem; // video
  protected ObjectAttributMemory_16_32 sMem; // sprite

  public Lcd(ColorModel colorModel) {
    this.colorModel = colorModel;
    consumerList = new CopyOnWriteArrayList<ImageConsumer>();
    rawPixels = new int[xScreenSize * yScreenSize];
  }

  public final void addConsumer(ImageConsumer ic) {
    ic.setDimensions(xScreenSize, yScreenSize);
    ic.setHints(ic.TOPDOWNLEFTRIGHT |
		ic.COMPLETESCANLINES |
		ic.SINGLEPASS |
		ic.SINGLEFRAME);
    ic.setColorModel(colorModel);
    consumerList.add(ic);
  }

  public final boolean isConsumer(ImageConsumer ic) {
    return consumerList.contains(ic);
  }

  public final void removeConsumer(ImageConsumer ic) {
    consumerList.remove(ic);
  }

  public final void startProduction(ImageConsumer ic) {
    addConsumer(ic);
    updatePixels();
  }

  public final void requestTopDownLeftRightResend(ImageConsumer ic) {
    ic.setPixels(0, 0, xScreenSize, yScreenSize, colorModel, rawPixels, 0, xScreenSize);
    ic.imageComplete(ic.SINGLEFRAMEDONE);
  }

  public final void updatePixels() {
    for (ImageConsumer ic : consumerList)
      requestTopDownLeftRightResend(ic);
  }

  public final void connectToMemory(GfaMMU memory) {
    ioMem = (IORegisterSpace_8_16_32)    memory.getMemoryBank(0x04); // registers
    pMem  = (MemoryManagementUnit_16_32) memory.getMemoryBank(0x05); // palette
    vMem  = (MemoryManagementUnit_16_32) memory.getMemoryBank(0x06); // video
    sMem  = (ObjectAttributMemory_16_32) memory.getMemoryBank(0x07); // sprite
  }

  public final void reset() {
    Arrays.fill(rawPixels, 0);
    updatePixels();
  }

  protected int[] bgXOrigin = new int[2];
  protected int[] bgYOrigin = new int[2];

  public final void updateBGXOriginL(short value, int bgNumber) {
    bgXOrigin[bgNumber - 2] = (bgXOrigin[bgNumber - 2] & 0xffff0000) | (value & 0x0000ffff);
  }
  
  public final void updateBGXOriginH(short value, int bgNumber) {
    bgXOrigin[bgNumber - 2] = (value << 16) | (bgXOrigin[bgNumber - 2] & 0x0000ffff);
  }
  
  public final void updateBGYOriginL(short value, int bgNumber) {
    bgYOrigin[bgNumber - 2] = (bgYOrigin[bgNumber - 2] & 0xffff0000) | (value & 0x0000ffff);
  }
  
  public final void updateBGYOriginH(short value, int bgNumber) {
    bgYOrigin[bgNumber - 2] = (value << 16) | (bgYOrigin[bgNumber - 2] & 0x0000ffff);
  }
  
  public abstract void drawLine(int y);

}
