package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF4Cmn extends ThumbStateF4 {

  public ThumbStateF4Cmn(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int operand1 = destinationRegister.get();
    int operand2 = sourceRegister.get();
    if (sourceRegister == PC) operand2 += 2;
    int result = operand1 + operand2;
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
    CPSR.setCVFlagsForAdd(operand1, operand2, result);
  }

  protected String getInstructionName() {
    return "cmn";
  }

}
