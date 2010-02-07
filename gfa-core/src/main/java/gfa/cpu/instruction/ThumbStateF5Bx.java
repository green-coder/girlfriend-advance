package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF5Bx
  extends ThumbStateF5
{

  public ThumbStateF5Bx(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    PC.set(sourceValue & 0xfffffffe);
    if ((sourceValue & 0x00000001) == 0) setArmState();
    else setThumbState();
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String rs = getRegisterName((opcode & (RsMask | H2Bit)) >>> 3);
    return getInstructionName() + " " + rs;
  }

  protected String getInstructionName()
  {
    return "bx";
  }

}
