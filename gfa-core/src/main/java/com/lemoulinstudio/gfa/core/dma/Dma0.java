package com.lemoulinstudio.gfa.core.dma;

public class Dma0 extends Dma {

  public Dma0() {
    super(0);
    countMaxValue = 0x00004000;
  }

  @Override
  public void setSrcHRegister(short srcH) {
    src = (src & 0x0000ffff) | ((srcH & 0x07ff) << 16);
  }

  @Override
  public void setDstHRegister(short dstH) {
    dst = (dst & 0x0000ffff) | ((dstH & 0x07ff) << 16);
  }

  @Override
  public void setCountRegister(short count) {
    this.count = (short) (count & 0x3fff);
  }

}
