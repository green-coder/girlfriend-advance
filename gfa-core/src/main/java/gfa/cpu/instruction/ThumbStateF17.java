package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF17 extends ThumbStateInstruction {

  public ThumbStateF17(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int instructionParameterMask      = 0x000000ff;
  static final protected int softwareInterrupVectorAddress = 0x00000008;

  public void execute() {
    //System.out.println("swi #" + (opcode & instructionParameterMask));
    
    getRegister(14, svcModeBits).set(PC);   // LR_svc <- PC
    getRegister(17, svcModeBits).set(CPSR); // SPSR_svc <- CPSR
    setMode(svcModeBits);
    PC.set(softwareInterrupVectorAddress);
    setArmState();
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    return "swi #" + (opcode & instructionParameterMask);
  }
  
}
