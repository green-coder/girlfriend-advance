package com.lemoulinstudio.gfa.core.dma;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.var.IOMem16Var;
import com.lemoulinstudio.gfa.core.memory.var.IOMem32Var;

public class Dma3 extends Dma {

  public Dma3() {
    super(3);
    countMaxValue = 0x00010000;
  }

  @Override
  public void connectToMemory(GfaMMU memory) {
    super.connectToMemory(memory);

    src = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA3SrcLAddress, 0x0fffffff);
    dst = new IOMem32Var(ioMem, IORegisterSpace_8_16_32.DMA3DstLAddress, 0x0fffffff);
    count = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA3SizeAddress, (short) (countMaxValue - 1));
    cr = new IOMem16Var(ioMem, IORegisterSpace_8_16_32.DMA3CrAddress, (short) 0xffff);
  }

}
