package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF11
  extends ThumbStateInstruction
{

  public ThumbStateF11(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int LoadStoreBit = 0x00000800;
  static final protected int RdMask       = 0x00000700;
  static final protected int ImmMask      = 0x000000ff;

  public void execute()
  {
    ArmReg srcDstRegister = getRegister((opcode & RdMask) >>> 8);
    int immediateValue = opcode & ImmMask;
    int offset = (getSP().get() & 0xfffffffc) + (immediateValue * 4);
    
    if ((opcode & LoadStoreBit) == 0) //store
      memory.storeWord(offset, srcDstRegister.get());
    else // load
      srcDstRegister.set(memory.loadWord(offset));
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "str" : "ldr";
    String rd = getRegisterName((opcode & RdMask) >>> 8);
    int word8 = (opcode & ImmMask) * 4;
    return instru + " " + rd + ", [sp, #" + word8 + "]";
  }
}
