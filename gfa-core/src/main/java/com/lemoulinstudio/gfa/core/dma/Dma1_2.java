package com.lemoulinstudio.gfa.core.dma;

public class Dma1_2 extends Dma {

  public Dma1_2(int dmaNumber) {
    super(dmaNumber);
  }

  @Override
  public void setSrcHRegister(short srcH) {
    src = (src & 0x0000ffff) | ((srcH & 0x0fff) << 16);
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
