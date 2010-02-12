package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public abstract class ThumbStateF4 extends ThumbStateInstruction {

  public ThumbStateF4(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int RsMask = 0x00000038;
  static final protected int RdMask = 0x00000007;
  protected ArmReg sourceRegister;
  protected ArmReg destinationRegister;

  public void execute() {
    sourceRegister = getRegister((opcode & RsMask) >>> 3);
    destinationRegister = getRegister(opcode & RdMask);

    applyOperation();
  }

  protected abstract void applyOperation();

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String rd = getRegisterName(opcode & RdMask);
    String rs = getRegisterName((opcode & RsMask) >>> 3);
    return getInstructionName() + " " + rd + ", " + rs;
  }

  protected abstract String getInstructionName();

}
