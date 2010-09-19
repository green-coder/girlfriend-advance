package com.lemoulinstudio.gfa.core.dma;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.var.IOMem16Var;
import com.lemoulinstudio.gfa.core.memory.var.IOMem32Var;

public class Dma1 extends Dma {

  public Dma1() {
    super(1);
    countMaxValue = 0x00004000;
  }

  @Override
  public void connectToMemory(GfaMMU memory) {
    super.connectToMemory(memory);

    src = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA1SrcLAddress, 0x0fffffff);
    dst = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA1DstLAddress, 0x07ffffff);
    count = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA1SizeAddress, (short) (countMaxValue - 1));
    cr = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA1CrAddress, (short) 0xffff);
  }
  
}
