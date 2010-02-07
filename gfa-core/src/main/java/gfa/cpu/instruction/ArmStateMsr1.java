package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ArmStateMsr1
  extends ArmStateInstruction
{
  static final protected int SourceMask     = 0x0000000f;
  static final protected int DestinationBit = 0x00400000;
  static final protected int FlagsOnly      = 0xf0000000;

  public ArmStateMsr1(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  public void execute()
  {
    if (!isPreconditionSatisfied()) return;
    
    try
    {
      ArmReg srcReg = getRegister(opcode & SourceMask);
      ArmReg dstReg = (((opcode & DestinationBit) == 0) ? CPSR : getSPSR());
      if (getMode() == usrModeBits)
        dstReg.set((dstReg.get() & ~FlagsOnly) | (srcReg.get() & FlagsOnly));
      else
        dstReg.set(srcReg);
    }
    catch (NullPointerException e)
    {
      signalError("Usage of the SPSR register in user mode.");
    }
  }

  public String disassemble(int offset)
  {
    int opcode = getOpcode(offset);
    String srcReg = getRegisterName(opcode & SourceMask);
    String dstReg = (((opcode & DestinationBit) == 0) ? "cpsr" : "spsr");
    return "msr" + preconditionToString(opcode) + " " + dstReg + ", " + srcReg;
  }
}
