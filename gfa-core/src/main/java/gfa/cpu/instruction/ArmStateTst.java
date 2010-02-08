package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateTst extends ArmStateTest {

  public ArmStateTst(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2) {
    int result = operand1 & operand2;
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "tst";
  }

}
