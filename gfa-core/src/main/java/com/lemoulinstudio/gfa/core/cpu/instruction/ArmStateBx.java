package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ArmStateBx extends ArmStateInstruction {

  public ArmStateBx(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int RnMask = 0x0000000f;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    int registerNumber = opcode & RnMask;
    int value = getRegister(registerNumber).get();
    PC.set(value & 0xfffffffe);
    if ((value & 0x00000001) == 0) setArmState();
    else setThumbState();
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    int registerNumber = opcode & RnMask;
    return "bx" + preconditionToString(opcode) + " " + getRegisterName(registerNumber);
  }

}
