package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF4Sbc extends ThumbStateF4 {

  public ThumbStateF4Sbc(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int operand1 = destinationRegister.get();
    int operand2 = sourceRegister.get();
    if (sourceRegister == PC) operand2 += 2;
    int notCFlagValue = (CPSR.isBitSet(cFlagBit) ? 0 : 1);
    int result = operand1 - operand2 - notCFlagValue;
    destinationRegister.set(result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
    CPSR.setCVFlagsForSub(operand1, operand2, result);
  }

  protected String getInstructionName() {
    return "sbc";
  }

}
