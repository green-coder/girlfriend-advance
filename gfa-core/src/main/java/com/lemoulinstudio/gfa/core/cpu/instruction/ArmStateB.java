package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import com.lemoulinstudio.gfa.core.util.Hex;

public class ArmStateB extends ArmStateInstruction {

  static final protected int LinkBit    = 0x01000000;
  static final protected int OffsetMask = 0x00ffffff;

  public ArmStateB(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    int offset = ((opcode & OffsetMask) << 8) >> 6;
    if ((opcode & LinkBit) != 0)
      getLR().set(PC.get() & 0xfffffffc);
    PC.add(offset + 4);
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String link = ((opcode & LinkBit) != 0) ? "l" : "";
    int targetOffset = offset + (((opcode & OffsetMask) << 8) >> 6) + 8;
    
    return "b" + link + preconditionToString(opcode) + " " + Hex.toString(targetOffset);
  }

}
