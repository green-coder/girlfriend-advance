package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF4Orr extends ThumbStateF4 {

  public ThumbStateF4Orr(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int operand2 = sourceRegister.get();
    if (sourceRegister == PC) operand2 += 2;
    int result = destinationRegister.get() | operand2;
    destinationRegister.set(result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "orr";
  }

}
