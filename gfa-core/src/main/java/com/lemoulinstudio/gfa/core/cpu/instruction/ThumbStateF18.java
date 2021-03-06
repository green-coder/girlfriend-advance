package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import com.lemoulinstudio.gfa.core.util.Hex;

public class ThumbStateF18 extends ThumbStateInstruction {

  public ThumbStateF18(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int OffsetMask = 0x000007ff;

  public void execute() {
    int offset = ((opcode & OffsetMask) << 21) >> 21;
    PC.add(2 + offset * 2);
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    int offset11 = ((opcode & OffsetMask) << 21) >> 21;
    int realTargetAddress = offset + 4 + offset11 * 2;
    return "b #" + Hex.toString(realTargetAddress);
  }

}
