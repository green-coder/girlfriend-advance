package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF9 extends ThumbStateInstruction {

  public ThumbStateF9(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int ByteWordBit  = 0x00001000;
  static final protected int LoadStoreBit = 0x00000800;
  static final protected int OffsetMask   = 0x000007c0;
  static final protected int RbMask       = 0x00000038;
  static final protected int RdMask       = 0x00000007;

  public void execute() {
    int baseOffset = getRegister((opcode & RbMask) >>> 3).get();
    int offset = (opcode & OffsetMask) >>> 6;
    ArmReg srcDstRegister = getRegister(opcode & RdMask);
    
    if ((opcode & ByteWordBit) == 0) { // word
      baseOffset += (offset << 2);
      int wordAlignedOffset = baseOffset & 0xfffffffc;
      if ((opcode & LoadStoreBit) == 0) // store
        memory.storeWord(wordAlignedOffset, srcDstRegister.get());
      else { // load
        int rightRotate = (baseOffset & 0x00000003) << 3;
	int value = memory.loadWord(wordAlignedOffset);
	srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
      }
    }
    else { // byte
      if ((opcode & LoadStoreBit) == 0) // store
        memory.storeByte(baseOffset + offset, (byte) srcDstRegister.get());
      else // load
        srcDstRegister.set(0x000000ff & memory.loadByte(baseOffset + offset));
    }
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru = (((opcode & LoadStoreBit) == 0) ? "str" : "ldr") +
	            (((opcode & ByteWordBit) == 0) ? "" : "b");
    String rd = getRegisterName(opcode & RdMask);
    String rb = getRegisterName((opcode & RbMask) >>> 3);
    int immValue = (opcode & OffsetMask) >>> 6;
    if ((opcode & ByteWordBit) == 0) immValue <<= 2; // word
    return instru + " " + rd + " [" + rb + ", #" + immValue + "]";
  }

}
