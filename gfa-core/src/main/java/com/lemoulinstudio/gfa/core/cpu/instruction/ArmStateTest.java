package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public abstract class ArmStateTest extends ArmStateDataProcessing {
  public ArmStateTest(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String instru = getInstructionName() + preconditionToString(opcode);
    String rn = getRegisterName((opcode & Operand1Mask) >>> 16);
    String op2 = disassembleOp2(opcode);
    
    return instru + " " + rn + ", " + op2;
  }

  abstract protected String getInstructionName();

}
