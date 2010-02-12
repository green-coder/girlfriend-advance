package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF5Mov extends ThumbStateF5 {

  public ThumbStateF5Mov(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    destinationRegister.set(sourceValue);
  }

  protected String getInstructionName() {
    return "mov";
  }
  
}
