package com.lemoulinstudio.gfa.core.gfx;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.MemoryManagementUnit_16_32;
import com.lemoulinstudio.gfa.core.memory.ObjectAttributMemory_16_32;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Lcd implements ImageProducer {

  protected final int[] rawPixels;
  private final DirectColorModel model;
  private final List<ImageConsumer> consumerList;

  public final static int xScreenSize = 240;
  public final static int yScreenSize = 160;

  protected IORegisterSpace_8_16_32   ioMem; // registers
  protected MemoryManagementUnit_16_32 pMem; // palette
  protected MemoryManagementUnit_16_32 vMem; // video
  protected ObjectAttributMemory_16_32 sMem; // sprite

  public Lcd() {
    rawPixels = new int[xScreenSize * yScreenSize];
    model = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);
    consumerList = new CopyOnWriteArrayList<ImageConsumer>();
  }

  public final void addConsumer(ImageConsumer ic) {
    ic.setDimensions(xScreenSize, yScreenSize);
    ic.setHints(ic.TOPDOWNLEFTRIGHT |
		ic.COMPLETESCANLINES |
		ic.SINGLEPASS |
		ic.SINGLEFRAME);
    ic.setColorModel(model);
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
    ic.setPixels(0, 0, xScreenSize, yScreenSize, model, rawPixels, 0, xScreenSize);
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
  
  protected final int color15BitsTo24Bits(short color15) {
    // The 15 bits format is "?bbbbbgggggrrrrr".
    // The 24 bits format is "11111111rrrrr000ggggg000bbbbb000".
    int r = (color15 & 0x0000001f) << 19;
    int g = (color15 & 0x000003e0) << 6;
    int b = (color15 & 0x00007c00) >> 7;
    return (0xff000000 | r | g | b);
  }

  public abstract void drawLine(int y);

}
