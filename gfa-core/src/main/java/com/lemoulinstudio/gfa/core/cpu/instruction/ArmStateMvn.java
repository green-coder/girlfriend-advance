package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ArmStateMvn extends ArmStateMove {

  public ArmStateMvn(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation(int operand2) {
    int result = ~operand2;
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "mvn";
  }

}
