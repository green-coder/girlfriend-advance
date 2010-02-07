package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF5Mov
  extends ThumbStateF5
{

  public ThumbStateF5Mov(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    destinationRegister.set(sourceValue);
  }

  protected String getInstructionName()
  {
    return "mov";
  }

}
