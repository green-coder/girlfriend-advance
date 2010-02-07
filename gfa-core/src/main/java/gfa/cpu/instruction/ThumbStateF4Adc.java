package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF4Adc
  extends ThumbStateF4
{

  public ThumbStateF4Adc(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    int operand1 = destinationRegister.get();
    int operand2 = sourceRegister.get();
    if (sourceRegister == PC) operand2 += 2;
    int cFlagValue = (CPSR.isBitSet(cFlagBit) ? 1 : 0);
    int result = operand1 + operand2 + cFlagValue;
    destinationRegister.set(result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
    CPSR.setCVFlagsForAdd(operand1, operand2, result);
  }

  protected String getInstructionName()
  {
    return "adc";
  }

}
