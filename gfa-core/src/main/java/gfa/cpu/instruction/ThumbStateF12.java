package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF12
  extends ThumbStateInstruction
{

  public ThumbStateF12(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int SPBit      = 0x00000800;
  static final protected int RdMask     = 0x00000700;
  static final protected int OffsetMask = 0x000000ff;

  public void execute()
  {
    int sourceAdress = (((opcode & SPBit) == 0) ? (PC.get() + 4) : getSP().get());
    ArmReg destinationRegister = getRegister((opcode & RdMask) >>> 8);
    int offset = opcode & OffsetMask;
    destinationRegister.set((sourceAdress & 0xfffffffc) + offset * 4);
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String rd = getRegisterName((opcode & RdMask) >>> 8);
    String rs = ((opcode & SPBit) == 0) ? "pc" : "sp";
    int immValue = (opcode & OffsetMask) * 4;
    return "add " + rd + ", " + rs + ", #" + immValue;
  }
}
