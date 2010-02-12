package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF3Mov extends ThumbStateF3 {

  public ThumbStateF3Mov(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    srcDstRegister.set(immediateValue);

    CPSR.setBit(zFlagBit, (immediateValue == 0));
    CPSR.setBit(nFlagBit, (immediateValue < 0));
  }

  protected String getInstructionName() {
    return "mov";
  }

}
