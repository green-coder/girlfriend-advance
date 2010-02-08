package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF13 extends ThumbStateInstruction {

  public ThumbStateF13(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int SignBit        = 0x00000080;
  static final protected int ImmediateMask  = 0x0000007f;

  public void execute() {
    int offset = opcode & ImmediateMask;
    if ((opcode & SignBit) != 0) offset = -offset;
    getSP().add(offset * 4);
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String immValue = (((opcode & SignBit) == 0) ? "" : "-") + ((opcode & ImmediateMask) * 4);
    return "add sp, #" + immValue;
  }
  
}
