package gfa.dma;

import gfa.memory.IORegisterSpace_8_16_32;

public class Dma2 extends Dma1_2 {

  public Dma2() {
    super("dma2");
    interruptBit = IORegisterSpace_8_16_32.dma2InterruptBit;
    countMaxValue = 0x00004000;
  }

}
