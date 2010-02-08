package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;
import gfa.util.Hex;

public class ThumbStateF6 extends ThumbStateInstruction {

  public ThumbStateF6(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int RdMask  = 0x00000700;
  static final protected int ImmMask = 0x000000ff;

  public void execute() {
    ArmReg destinationRegister = getRegister((opcode & RdMask) >>> 8);
    int immValue = opcode & ImmMask;
    int value = memory.loadWord(((PC.get() + 2) & 0xfffffffc) + (immValue << 2));
    destinationRegister.set(value);
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String rd = getRegisterName((opcode & RdMask) >>> 8);
    int immValue = (opcode & ImmMask) << 2;
    int realSourceAddress = ((offset + 4) & 0xfffffffc) + immValue;
    return "ldr " + rd + ", [pc + #" + immValue + "]   ;@" + Hex.toString(realSourceAddress);
  }

}
