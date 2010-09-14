package com.lemoulinstudio.gfa.core.dma;

public class Dma3 extends Dma {

  public Dma3() {
    super(3);
    countMaxValue = 0x00010000;
  }

  @Override
  public void setSrcHRegister(short srcH) {
    src = (src & 0x0000ffff) | ((srcH & 0x0fff) << 16);
  }

  @Override
  public void setDstHRegister(short dstH) {
    dst = (dst & 0x0000ffff) | ((dstH & 0x0fff) << 16);
  }

}
