package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateUnknown extends ArmStateInstruction {

  public ArmStateUnknown(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  public void execute() {
    System.out.println("ArmState Unknown Instruction");
    throw new RuntimeException("ArmState Unknown Instruction");
  }

  public String disassemble(int offset) {
    return "?????";
  }

}
