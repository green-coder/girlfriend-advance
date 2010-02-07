package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;
import gfa.util.*;

public class ThumbStateF19
  extends ThumbStateInstruction
{

  public ThumbStateF19(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int LowHiOffsetBit = 0x00000800;
  static final protected int OffsetMask     = 0x000007ff;

  public void execute()
  {
    int offset = opcode & OffsetMask;
    if ((opcode & LowHiOffsetBit) == 0) // High offset
    {
      offset = ((offset << 21) >> 21) << 12;
      getLR().set(offset + 2 + (PC.get() & 0xfffffffe));
    }
    else // Low offset
    {
      ArmReg LR = getLR();
      LR.add(offset << 1);
      int nextInstrAdress = PC.get();
      PC.set(LR);
      LR.set(nextInstrAdress | 0x00000001);
    }
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    if ((opcode & LowHiOffsetBit) == 0) return "; bl beginning";
    short opcodeBefore = memory.loadHalfWord(offset - 2);
    int low = ((opcode & OffsetMask) << 1);
    int hi = (((opcodeBefore & OffsetMask) << 21) >> 21) << 12;
    int realTargetAddress = offset + 2 + hi + low;
    return "bl #" + Hex.toString(realTargetAddress);
  }
}
