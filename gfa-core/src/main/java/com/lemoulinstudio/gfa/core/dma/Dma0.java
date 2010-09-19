package com.lemoulinstudio.gfa.core.dma;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.var.IOMem16Var;
import com.lemoulinstudio.gfa.core.memory.var.IOMem32Var;

public class Dma0 extends Dma {

  public Dma0() {
    super(0);
    countMaxValue = 0x00004000;
  }

  @Override
  public void connectToMemory(GfaMMU memory) {
    super.connectToMemory(memory);

    src = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA0SrcLAddress, 0x07ffffff);
    dst = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA0DstLAddress, 0x07ffffff);
    count = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA0SizeAddress, (short) (countMaxValue - 1));
    cr = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA0CrAddress, (short) 0xffff);
  }

}
