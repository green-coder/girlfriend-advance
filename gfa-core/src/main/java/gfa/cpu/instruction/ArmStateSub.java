package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ArmStateSub
  extends ArmStateArithmLogic
{
  public ArmStateSub(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2)
  {
    int result = operand1 - operand2;
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
    tmpCPSR.setCVFlagsForSub(operand1, operand2, result);
  }

  protected String getInstructionName()
  {
    return "sub";
  }
}
