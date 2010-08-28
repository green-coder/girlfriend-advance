package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import com.lemoulinstudio.gfa.core.util.Hex;

public class ArmStateSwi extends ArmStateInstruction {

  public ArmStateSwi(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int SoftwareInterrupVectorAddress = 0x00000008;
  static final protected int ParameterMask = 0x00ffffff;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    getRegister(14, svcModeBits).set(PC);
    getRegister(17, svcModeBits).set(CPSR);
    setMode(svcModeBits);
    PC.set(SoftwareInterrupVectorAddress);
    setArmState();
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    return "swi #0x" + Hex.toString(opcode & ParameterMask);
  }

}
