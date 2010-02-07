package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ArmStateSbc
  extends ArmStateArithmLogic
{
  public ArmStateSbc(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2)
  {
    int notCFlagValue = (CPSR.isBitSet(cFlagBit) ? 0 : 1);
    int result = operand1 - (operand2 + notCFlagValue);
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
    tmpCPSR.setCVFlagsForSub(operand1, operand2, result);
  }

  protected String getInstructionName()
  {
    return "sbc";
  }
}
