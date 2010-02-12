package com.lemoulinstudio.gfa.core.dma;

import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;

public class Dma1 extends Dma1_2 {

  public Dma1() {
    super("dma1");
    interruptBit = IORegisterSpace_8_16_32.dma1InterruptBit;
    countMaxValue = 0x00004000;
  }

}
