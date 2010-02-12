package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ArmStateAdc extends ArmStateArithmLogic {

  public ArmStateAdc(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2) {
    int cFlagValue = (CPSR.isBitSet(cFlagBit) ? 1 : 0);
    int result = operand1 + operand2 + cFlagValue;
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
    tmpCPSR.setCVFlagsForAdd(operand1, operand2, result);
  }

  protected String getInstructionName() {
    return "adc";
  }

}
