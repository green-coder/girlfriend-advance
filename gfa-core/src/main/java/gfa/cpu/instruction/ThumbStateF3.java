package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;
import gfa.util.*;

public abstract class ThumbStateF3
  extends ThumbStateInstruction
{

  public ThumbStateF3(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int OffsetMask = 0x000000ff;
  static final protected int RdMask     = 0x00000700;
  protected ArmReg srcDstRegister;
  protected int immediateValue;
  
  public void execute()
  {
    srcDstRegister = getRegister((opcode & RdMask) >>> 8);
    immediateValue = opcode & OffsetMask;
    
    applyOperation();
  }

  abstract protected void applyOperation();

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String rd = getRegisterName((opcode & RdMask) >>> 8);
    String offset8 = "#" + Hex.toString(opcode & OffsetMask);
    return getInstructionName() + " " + rd + ", " + offset8;
  }

  abstract protected String getInstructionName();
}
