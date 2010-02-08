package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateUndef extends ArmStateInstruction {

  public ArmStateUndef(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int undefinedInstructionVectorAddress = 0x00000004;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    getRegister(14, undModeBits).set(PC);
    getRegister(17, undModeBits).set(CPSR);
    setMode(undModeBits);
    PC.set(undefinedInstructionVectorAddress);
    setArmState();
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    return "undef" + preconditionToString(opcode);
  }

}
