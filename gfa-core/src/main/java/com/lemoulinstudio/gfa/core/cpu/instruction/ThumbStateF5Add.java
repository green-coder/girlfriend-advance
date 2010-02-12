package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF5Add extends ThumbStateF5 {

  public ThumbStateF5Add(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    destinationRegister.add(sourceValue);
  }

  protected String getInstructionName() {
    return "add";
  }

}
