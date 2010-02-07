package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF4Neg
  extends ThumbStateF4
{

  public ThumbStateF4Neg(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    int operand1 = sourceRegister.get();
    if (sourceRegister == PC) operand1 += 2;
    int operand2 = 0;
    int result = operand2 - operand1;
    destinationRegister.set(result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
    CPSR.setCVFlagsForSub(operand2, operand1, result);
  }

  protected String getInstructionName()
  {
    return "neg";
  }

}
