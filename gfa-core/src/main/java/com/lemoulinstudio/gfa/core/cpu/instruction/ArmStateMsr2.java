package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import com.lemoulinstudio.gfa.core.util.Hex;

public class ArmStateMsr2 extends ArmStateInstruction {

  static final protected int SourceMask     = 0x0000000f;
  static final protected int DestinationBit = 0x00400000;
  static final protected int ImmediateBit   = 0x02000000;
  static final protected int ImmValueMask   = 0x000000ff;
  static final protected int ImmRotateMask  = 0x00000f00;
  static final protected int FlagsOnly      = 0xf0000000;

  public ArmStateMsr2(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    try {
      ArmReg dstReg = (((opcode & DestinationBit) == 0) ? CPSR : getSPSR());
      int value;
      if ((opcode & ImmediateBit) == 0)
        value = getRegister(opcode & SourceMask).get();
      else {
        int immValue = opcode & ImmValueMask;
	int immRotate = (opcode & ImmRotateMask) >>> 7;
	value = (immValue >> immRotate) | (immValue << (32 - immRotate));
      }
      dstReg.set((dstReg.get() & ~FlagsOnly) | (value & FlagsOnly));
    }
    catch (NullPointerException e) {
      signalError("Usage of the SPSR register in user mode.");
    }
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String dstReg = (((opcode & DestinationBit) == 0) ? "cpsr_flg" : "spsr_flg");
    String src;
    if ((opcode & ImmediateBit) == 0)
      src = getRegisterName(opcode & SourceMask);
    else {
      int immValue = opcode & ImmValueMask;
      int immRotate = (opcode & ImmRotateMask) >>> 7;
      src = "#" + Hex.toString((immValue >> immRotate) | (immValue << (32 - immRotate)));
    }
    return "msr" + preconditionToString(opcode) + " " + dstReg + ", " + src;
  }

}
