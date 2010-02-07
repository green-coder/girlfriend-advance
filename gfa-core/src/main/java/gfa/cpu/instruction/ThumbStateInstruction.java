package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

/**
 * This class is the super-class of all the "thumb-state mode instruction set" of the ARM7TDMI.
 * It is a 16-bits-per-instruction instruction set.
 * Thumb-state instructions are essencially register-based instruction,
 * due to the little space in memory of their opcode.
 */
public abstract class ThumbStateInstruction
    extends Instruction
{

  protected int opcode;

  public ThumbStateInstruction(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  /*
   * Is used by the InstructionDecoder.
   */
  public void setOpcode(int opcode)
  {
    this.opcode = opcode;
  }

  protected short getOpcode(int instructionOffset)
  {
    return memory.loadHalfWord(instructionOffset);
  }

}
