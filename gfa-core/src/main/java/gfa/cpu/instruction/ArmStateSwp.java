package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ArmStateSwp
  extends ArmStateInstruction
{

  public ArmStateSwp(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int ByteWordBit = 0x00400000;
  static final protected int RnMask      = 0x000f0000;
  static final protected int RdMask      = 0x0000f000;
  static final protected int RmMask      = 0x0000000f;

  public void execute()
  {
    if (!isPreconditionSatisfied()) return;
    
    ArmReg baseRegister = getRegister((opcode & RnMask) >>> 16);
    ArmReg destinationRegister = getRegister((opcode & RdMask) >>> 12);
    ArmReg sourceRegister = getRegister(opcode & RmMask);
    int swapAdress = baseRegister.get();
    
    if ((opcode & ByteWordBit) != 0) // Byte
      destinationRegister.set(0x000000ff & memory.swapByte(swapAdress, (byte) sourceRegister.get()));
    else // Word
      destinationRegister.set(memory.swapWord(swapAdress, sourceRegister.get()));
  }

  public String disassemble(int offset)
  {
    int opcode = getOpcode(offset);
    String instru = "swp" + preconditionToString(opcode);
    if ((opcode & ByteWordBit) != 0) instru += "b";
    String rd = getRegisterName((opcode & RdMask) >>> 12);
    String rm = getRegisterName(opcode & RmMask);
    String rn = getRegisterName((opcode & RnMask) >>> 16);
    return instru + " " + rd + ", " + rm + ", [" + rn + "]";
  }
}
