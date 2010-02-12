package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ArmStateRsb extends ArmStateArithmLogic {

  public ArmStateRsb(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2) {
    int result = operand2 - operand1;
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
    tmpCPSR.setCVFlagsForSub(operand2, operand1, result);
  }

  protected String getInstructionName() {
    return "rsb";
  }

}
