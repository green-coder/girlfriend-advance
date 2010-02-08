package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateMrs extends ArmStateInstruction {

  static final protected int SourceBit       = 0x00400000;
  static final protected int DestinationMask = 0x0000f000;

  public ArmStateMrs(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    try {
      ArmReg srcReg = (((opcode & SourceBit) == 0) ? CPSR : getSPSR());
      ArmReg dstReg = getRegister((opcode & DestinationMask) >>> 12);
      dstReg.set(srcReg);
    }
    catch (NullPointerException e) {
      signalError("Bad use of the MRS instruction in user mode.");
    }
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String srcReg = (((opcode & SourceBit) == 0) ? "cpsr" : "spsr");
    String dstReg = getRegisterName((opcode & DestinationMask) >>> 12);
    return "mrs" + preconditionToString(opcode) + " " + dstReg + ", " + srcReg;
  }

}
