package gfa.dma;

import gfa.memory.IORegisterSpace_8_16_32;

public class Dma0 extends Dma {

  public Dma0() {
    super("dma0");
    interruptBit = IORegisterSpace_8_16_32.dma0InterruptBit;
    countMaxValue = 0x00004000;
  }

  public void setSrcHRegister(short srcH) {
    src = (src & 0x0000ffff) | ((srcH & 0x07ff) << 16);
  }

  public void setDstHRegister(short dstH) {
    dst = (dst & 0x0000ffff) | ((dstH & 0x07ff) << 16);
  }

  public void setCountRegister(short count) {
    this.count = (short) (count & 0x3fff);
  }

}
