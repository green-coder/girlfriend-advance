package gfa.dma;

import gfa.memory.GfaMMU;
import gfa.memory.IORegisterSpace_8_16_32;
import gfa.util.Hex;

public class Dma {

  protected String name;

  protected int src;
  protected int dst;
  protected short count;
  protected short cr;

  protected boolean dmaEnabled;
  protected boolean irqEnabled;
  protected boolean repeatEnabled;
  protected int startMode;

  protected GfaMMU mem;
  protected IORegisterSpace_8_16_32 ioMem;
  protected short interruptBit;
  protected int countMaxValue;

  public Dma(String name) {
    this.name = name;

    src = 0;
    dst = 0;
    count = 0;
    cr = 0;

    dmaEnabled = false;
    irqEnabled = false;
    repeatEnabled = false;
    startMode = 0;
  }

  public void connectToMemory(GfaMMU memory) {
    this.mem = memory;
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(4);
  }

  public short getSrcLRegister() {
    return (short) src;
  }

  public short getSrcHRegister() {
    return (short) (src >>> 16);
  }

  public short getDstLRegister() {
    return (short) dst;
  }

  public short getDstHRegister() {
    return (short) (dst >>> 16);
  }

  public short getCountRegister() {
    return count;
  }

  public short getCrRegister() {
    return cr;
  }

  public void setSrcLRegister(short srcL) {
    src = (src & 0xffff0000) | (0x0000ffff & srcL);
  }

  public void setSrcHRegister(short srcH) {
    src = (src & 0x0000ffff) | ((int) srcH << 16);
  }

  public void setDstLRegister(short dstL) {
    dst = (dst & 0xffff0000) | (0x0000ffff & dstL);
  }

  public void setDstHRegister(short dstH) {
    dst = (dst & 0x0000ffff) | ((int) dstH << 16);
  }

  public void setCountRegister(short count) {
    this.count = count;
  }

  public final int DMAXCrEnabledBit    = 0x8000;
  public final int DMAXCrIRQBit        = 0x4000;
  public final int DMAXCrStartModeMask = 0x3000;
  public final int DMAXCrWidthBit      = 0x0400;
  public final int DMAXCrRepeatBit     = 0x0200;
  public final int DMAXCrSrcModeMask   = 0x0180;
  public final int DMAXCrDstModeMask   = 0x0060;
  public final int startModeImmediatly = 0x0000;
  public final int startModeVBlank     = 0x1000;
  public final int startModeHBlank     = 0x2000;

  public void setCrRegister(short val) {
    cr = val;
    dmaEnabled = ((cr & DMAXCrEnabledBit) != 0);
    irqEnabled = ((cr & DMAXCrIRQBit) != 0);
    repeatEnabled = ((cr & DMAXCrRepeatBit) != 0);
    startMode = (cr & DMAXCrStartModeMask);

    if (dmaEnabled && (startMode == startModeImmediatly)) {
      blit();
      cr &= ~DMAXCrEnabledBit; // Disable the dma.
      dmaEnabled = false;
    }
  }

  protected void blit() {
    // Size in byte of each element to be copied.
    int dataSize = (((cr & DMAXCrWidthBit) == 0) ? 2 : 4);

    // Number of elements to be copied.
    int unsignedCount = count & 0xffff;
    if (unsignedCount == 0) unsignedCount = countMaxValue;

    // Set the increment of the source pointer.
    int toAddToSrc;
    switch (cr & DMAXCrSrcModeMask) {
      case 0x0000: toAddToSrc =  dataSize; break;
      case 0x0080: toAddToSrc = -dataSize; break;
      case 0x0100: toAddToSrc =  0;        break;
      default: System.out.println("Dma SrcMode illegal : " + this); return;
    }

    // Set the increment of the destination pointer.
    int toAddToDst;
    int oldDst = dst;
    switch (cr & DMAXCrDstModeMask) {
      case 0x0000: toAddToDst =  dataSize; break;
      case 0x0020: toAddToDst = -dataSize; break;
      case 0x0040: toAddToDst =  0;        break;
      default:     toAddToDst =  dataSize; break;
    }

    // Process the copy
    if ((cr & DMAXCrWidthBit) == 0) { // if the copy is 16bits-based
      for (int i = 0; i < unsignedCount; i++) {
        mem.storeHalfWord(dst, mem.loadHalfWord(src));
        src += toAddToSrc;
        dst += toAddToDst;
      }
    }
    else { // else, the copy is 32bits-based
      for (int i = 0; i < unsignedCount; i++) {
        mem.storeWord(dst, mem.loadWord(src));
        src += toAddToSrc;
        dst += toAddToDst;
      }
    }

    if ((cr & DMAXCrDstModeMask) == 0x0060) // dst incr&reload
      dst = oldDst;

    if (irqEnabled)
      ioMem.genInterrupt(interruptBit); // generate an interrupt.
  }

  public void notifyVBlank() {
    if (dmaEnabled && (startMode == startModeVBlank)) {
      blit();
      if (!repeatEnabled) {
        cr &= ~DMAXCrEnabledBit; // Disable the dma.
        dmaEnabled = false;
      }
    }
  }

  public void notifyHBlank() {
    if (dmaEnabled && (startMode == startModeHBlank)) {
      blit();
      if (!repeatEnabled) {
        cr &= ~DMAXCrEnabledBit; // Disable the dma.
        dmaEnabled = false;
      }
    }
  }

  /**
   * Called when the user reset gfa.
   */
  public void reset() {
    src   = 0;
    dst   = 0;
    count = 0;
    cr    = 0;

    dmaEnabled    = false;
    irqEnabled    = false;
    repeatEnabled = false;
    startMode     = 0;
  }

  public String toString() {
    return ("["
            + name
            + " src = " + Hex.toString(src)
            + " dst = " + Hex.toString(dst)
            + " count = " + Hex.toString(count)
            + " cr = " + Hex.toString(cr)
            + "]");
  }
  
}
