package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF7 extends ThumbStateInstruction {

  public ThumbStateF7(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int LoadStoreBit = 0x00000800;
  static final protected int ByteWordBit  = 0x00000400;
  static final protected int RoMask       = 0x000001c0;
  static final protected int RbMask       = 0x00000038;
  static final protected int RdMask       = 0x00000007;

  public void execute() {
    int offset = getRegister((opcode & RoMask) >>> 6).get() +
	         getRegister((opcode & RbMask) >>> 3).get();
    ArmReg srcDstRegister = getRegister(opcode & RdMask);
    
    if ((opcode & LoadStoreBit) == 0) { // store
      if ((opcode & ByteWordBit) == 0) // word
        memory.storeWord(offset, srcDstRegister.get());
      else // byte
        memory.storeByte(offset, (byte) srcDstRegister.get());
    }
    else { // load
      if ((opcode & ByteWordBit) == 0) { // word
        int wordAlignedOffset = offset & 0xfffffffc;
	int rightRotate = (offset & 0x00000003) << 3;
	int value = memory.loadWord(wordAlignedOffset);
	srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
      }
      else // byte
        srcDstRegister.set(0x000000ff & memory.loadByte(offset));
    }
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru = (((opcode & LoadStoreBit) == 0) ? "str" : "ldr") +
	            (((opcode & ByteWordBit) == 0) ? "" : "b");
    String rd = getRegisterName(opcode & RdMask);
    String rb = getRegisterName((opcode & RbMask) >>> 3);
    String ro = getRegisterName((opcode & RoMask) >>> 6);
    return instru + " " + rd + ", [" + rb + ", " + ro + "]";
  }

}
