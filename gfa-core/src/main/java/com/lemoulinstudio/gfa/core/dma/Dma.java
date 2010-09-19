package com.lemoulinstudio.gfa.core.dma;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.var.Mem16Var;
import com.lemoulinstudio.gfa.core.memory.var.Mem32Var;
import com.lemoulinstudio.gfa.core.util.Hex;

public class Dma {

  private final int dmaNumber;

  protected Mem32Var src;
  protected Mem32Var dst;
  protected Mem16Var count;
  protected Mem16Var cr;

  protected GfaMMU mem;
  protected IORegisterSpace_8_16_32 ioMem;
  protected int countMaxValue;

  public Dma(int dmaNumber) {
    this.dmaNumber = dmaNumber;
  }

  public void connectToMemory(GfaMMU memory) {
    this.mem = memory;
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(4);
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

  public void notifyCrModified() {
    short crValue = cr.getValue();
    if (((crValue & DMAXCrEnabledBit) != 0) &&
        ((crValue & DMAXCrStartModeMask) == startModeImmediatly)) {
      blit();
      cr.setValue((short) (crValue & ~DMAXCrEnabledBit));
    }
  }

  protected void blit() {
    short crValue = cr.getValue();

    // Size in byte of each element to be copied.
    int dataSize = (((crValue & DMAXCrWidthBit) == 0) ? 2 : 4);

    // Number of elements to be copied.
    int unsignedCount = count.getValue() & 0xffff;
    if (unsignedCount == 0) unsignedCount = countMaxValue;

    // Set the increment of the source pointer.
    int toAddToSrc;
    switch (crValue & DMAXCrSrcModeMask) {
      case 0x0000: toAddToSrc =  dataSize; break;
      case 0x0080: toAddToSrc = -dataSize; break;
      case 0x0100: toAddToSrc =  0;        break;
      default: System.out.println("Dma SrcMode illegal : " + this); return;
    }

    // Set the increment of the destination pointer.
    int toAddToDst;
    switch (crValue & DMAXCrDstModeMask) {
      case 0x0000: toAddToDst =  dataSize; break;
      case 0x0020: toAddToDst = -dataSize; break;
      case 0x0040: toAddToDst =  0;        break;
      default:     toAddToDst =  dataSize; break;
    }

    int srcValue = src.getValue();
    int dstValue = dst.getValue();
    int oldDst = dstValue;
    
    // Process the copy
    if ((crValue & DMAXCrWidthBit) == 0) { // if the copy is 16bits-based
      for (int i = 0; i < unsignedCount; i++) {
        mem.storeHalfWord(dstValue, mem.loadHalfWord(srcValue));
        srcValue += toAddToSrc;
        dstValue += toAddToDst;
      }
    }
    else { // else, the copy is 32bits-based
      for (int i = 0; i < unsignedCount; i++) {
        mem.storeWord(dstValue, mem.loadWord(srcValue));
        srcValue += toAddToSrc;
        dstValue += toAddToDst;
      }
    }

    src.setValue(srcValue);
    dst.setValue(dstValue);

    if ((crValue & DMAXCrDstModeMask) == 0x0060) // dst incr&reload
      dst.setValue(oldDst);

    if ((crValue & DMAXCrIRQBit) != 0)
      ioMem.genInterrupt(IORegisterSpace_8_16_32.dmaInterruptBit[dmaNumber]); // generate an interrupt.
  }

  public void notifyVBlank() {
    short crValue = cr.getValue();
    if (((crValue & DMAXCrEnabledBit) != 0) &&
        ((crValue & DMAXCrStartModeMask) == startModeVBlank)) {
      blit();
      if ((crValue & DMAXCrRepeatBit) == 0)
        cr.setValue((short) (crValue & ~DMAXCrEnabledBit));
    }
  }

  public void notifyHBlank() {
    short crValue = cr.getValue();
    if (((crValue & DMAXCrEnabledBit) != 0) &&
        ((crValue & DMAXCrStartModeMask) == startModeHBlank)) {
      blit();
      if ((crValue & DMAXCrRepeatBit) == 0)
        cr.setValue((short) (crValue & ~DMAXCrEnabledBit));
    }
  }

  /**
   * Called when the user reset gfa.
   */
  public void reset() {
    src.setValue(0);
    dst.setValue(0);
    count.setValue((short) 0);
    cr.setValue((short) 0);
  }

  @Override
  public String toString() {
    return ("["
            + "dma" + dmaNumber
            + " src = " + Hex.toString(src.getValue())
            + " dst = " + Hex.toString(dst.getValue())
            + " count = " + Hex.toString(count.getValue())
            + " cr = " + Hex.toString(cr.getValue())
            + "]");
  }
  
}
