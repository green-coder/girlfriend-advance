package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF10
  extends ThumbStateInstruction
{

  public ThumbStateF10(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int LoadStoreBit = 0x00000800;
  static final protected int OffsetMask   = 0x000007c0;
  static final protected int RbMask       = 0x00000038;
  static final protected int RdMask       = 0x00000007;

  public void execute()
  {
    int offset = getRegister((opcode & RbMask) >>> 3).get() +
	                    ((opcode & OffsetMask) >>> 6) * 2;
    ArmReg srcDstRegister = getRegister(opcode & RdMask);
    
    if ((opcode & LoadStoreBit) == 0) // store
      memory.storeHalfWord(offset, (short) srcDstRegister.get());
    else
      srcDstRegister.set(0x0000ffff & memory.loadHalfWord(offset));
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "strh" : "ldrh";
    String rd = getRegisterName(opcode & RdMask);
    String rb = getRegisterName((opcode & RbMask) >>> 3);
    int immValue = ((opcode & OffsetMask) >>> 6) * 2;
    return instru + " " + rd + ", [" + rb + ", #" + immValue + "]";
  }
}
