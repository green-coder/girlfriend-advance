package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF5Add
  extends ThumbStateF5
{

  public ThumbStateF5Add(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    destinationRegister.add(sourceValue);
  }

  protected String getInstructionName()
  {
    return "add";
  }

}
