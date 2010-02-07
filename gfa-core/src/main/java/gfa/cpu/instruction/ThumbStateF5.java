package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public abstract class ThumbStateF5
  extends ThumbStateInstruction
{

  public ThumbStateF5(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int H1Bit  = 0x00000080;
  static final protected int H2Bit  = 0x00000040;
  static final protected int RsMask = 0x00000038;
  static final protected int RdMask = 0x00000007;
  protected int  sourceValue;
  protected ArmReg destinationRegister;

  public void execute()
  {
    ArmReg sourceRegister = getRegister((opcode & (RsMask | H2Bit)) >>> 3);
    destinationRegister = getRegister((opcode & RdMask) | ((opcode & H1Bit) >>> 4));
    
    sourceValue = sourceRegister.get();
    if (sourceRegister == PC) sourceValue += 2;
    
    applyOperation();
  }

  protected abstract void applyOperation();

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String rd = getRegisterName((opcode & RdMask) | ((opcode & H1Bit) >>> 4));
    String rs = getRegisterName((opcode & (RsMask | H2Bit)) >>> 3);
    if (((opcode & H1Bit) == 0) && ((opcode & H2Bit) == 0))
      return "F5_undefined_";
    else
      return getInstructionName() + " " + rd + ", " + rs;
  }

  protected abstract String getInstructionName();
}
