package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF5Bx extends ThumbStateF5 {

  public ThumbStateF5Bx(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    PC.set(sourceValue & 0xfffffffe);
    if ((sourceValue & 0x00000001) == 0) setArmState();
    else setThumbState();
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String rs = getRegisterName((opcode & (RsMask | H2Bit)) >>> 3);
    return getInstructionName() + " " + rs;
  }

  protected String getInstructionName() {
    return "bx";
  }

}
