package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateSwi extends ArmStateInstruction {

  public ArmStateSwi(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int softwareInterrupVectorAddress = 0x00000008;
  static final protected int ParameterMask = 0x00ffffff;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    //System.out.println("swi #" + (opcode & ParameterMask));
    
    getRegister(14, svcModeBits).set(PC);
    getRegister(17, svcModeBits).set(CPSR);
    setMode(svcModeBits);
    PC.set(softwareInterrupVectorAddress);
    setArmState();
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    return "swi #" + (opcode & ParameterMask);
  }

}
