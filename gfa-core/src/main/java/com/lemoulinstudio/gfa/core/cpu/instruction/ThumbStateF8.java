package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF8 extends ThumbStateInstruction {

  public ThumbStateF8(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int HBit            = 0x00000800;
  static final protected int SignExtendedBit = 0x00000400;
  static final protected int RoMask          = 0x000001c0;
  static final protected int RbMask          = 0x00000038;
  static final protected int RdMask          = 0x00000007;

  public void execute() {
    int offset = getRegister((opcode & RoMask) >>> 6).get() +
	         getRegister((opcode & RbMask) >>> 3).get();
    ArmReg srcDstRegister = getRegister(opcode & RdMask);
    
    if ((opcode & SignExtendedBit) == 0) {
      if ((opcode & HBit) == 0)
        memory.storeHalfWord(offset & 0xfffffffe, (short) srcDstRegister.get());
      else
        srcDstRegister.set(0x0000ffff & memory.loadHalfWord(offset & 0xfffffffe));
    }
    else {
      if ((opcode & HBit) == 0)
        srcDstRegister.set(memory.loadByte(offset));
      else
        srcDstRegister.set(memory.loadHalfWord(offset & 0xfffffffe));
    }
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru;
    if ((opcode & SignExtendedBit) == 0) {
      if ((opcode & HBit) == 0) instru = "strh";
      else instru = "ldrh";
    }
    else {
      if ((opcode & HBit) == 0) instru = "ldsb";
      else instru = "ldsh";
    }
    String rd = getRegisterName(opcode & RdMask);
    String rb = getRegisterName((opcode & RbMask) >>> 3);
    String ro = getRegisterName((opcode & RoMask) >>> 6);
    return instru + " " + rd + ", [" + rb + ", " + ro + "]";
  }

}
