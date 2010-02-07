package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

/**
 * This class is the super-class of all the "arm-state mode instruction set" of the ARM7TDMI.
 * It is a 32-bits-per-instruction instruction set.
 * It allows the use of a flag based precondition on each of its element.
 * Arm-state instructions allows the use of many constants in its opcode.
 */
public abstract class ArmStateInstruction
  extends Instruction
{

  protected int opcode;

  public ArmStateInstruction(ArmReg[][] regs, MemoryInterface memory)
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

  protected int getOpcode(int instructionOffset)
  {
    return memory.loadWord(instructionOffset);
  }

  static final protected int ConditionBitsMask = 0xf0000000; // the bits concerning the condition in the instruction
  static final protected int ConditionEQBits   = 0x00000000; // equal
  static final protected int ConditionNEBits   = 0x10000000; // not equal
  static final protected int ConditionCSBits   = 0x20000000; // unsigned higher or same
  static final protected int ConditionCCBits   = 0x30000000; // unsigned lower
  static final protected int ConditionMIBits   = 0x40000000; // negative
  static final protected int ConditionPLBits   = 0x50000000; // positive or zero
  static final protected int ConditionVSBits   = 0x60000000; // overflow
  static final protected int ConditionVCBits   = 0x70000000; // no overflow
  static final protected int ConditionHIBits   = 0x80000000; // unsigned higher
  static final protected int ConditionLSBits   = 0x90000000; // unsigned lower or same
  static final protected int ConditionGEBits   = 0xa0000000; // greater or equal
  static final protected int ConditionLTBits   = 0xb0000000; // less than
  static final protected int ConditionGTBits   = 0xc0000000; // greater than
  static final protected int ConditionLEBits   = 0xd0000000; // less than or equal
  static final protected int ConditionALBits   = 0xe0000000; // always

  public boolean isPreconditionSatisfied()
  {
    int conditionBits = opcode & ConditionBitsMask;
    
    switch (conditionBits)
    {
    case ConditionEQBits: return  CPSR.isBitSet(zFlagBit);
    case ConditionNEBits: return !CPSR.isBitSet(zFlagBit);
    case ConditionCSBits: return  CPSR.isBitSet(cFlagBit);
    case ConditionCCBits: return !CPSR.isBitSet(cFlagBit);
    case ConditionMIBits: return  CPSR.isBitSet(nFlagBit);
    case ConditionPLBits: return !CPSR.isBitSet(nFlagBit);
    case ConditionVSBits: return  CPSR.isBitSet(vFlagBit);
    case ConditionVCBits: return !CPSR.isBitSet(vFlagBit);
    case ConditionHIBits: return  CPSR.isBitSet(cFlagBit) && !CPSR.isBitSet(zFlagBit);
    case ConditionLSBits: return !CPSR.isBitSet(cFlagBit) ||  CPSR.isBitSet(zFlagBit);
    case ConditionGEBits: return  CPSR.isBitSet(nFlagBit) ==  CPSR.isBitSet(vFlagBit);
    case ConditionLTBits: return  CPSR.isBitSet(nFlagBit) !=  CPSR.isBitSet(vFlagBit);
    case ConditionGTBits: return !CPSR.isBitSet(zFlagBit) && (CPSR.isBitSet(nFlagBit) == CPSR.isBitSet(vFlagBit));
    case ConditionLEBits: return  CPSR.isBitSet(zFlagBit) || (CPSR.isBitSet(nFlagBit) != CPSR.isBitSet(vFlagBit));
    case ConditionALBits: return true;
    default:
	// Undefined condition. We interprete this like a NOP (No OPeration : an instruction that does nothing).
	return false;
    }
  }

  protected String preconditionToString(int opcode)
  {
    int conditionBits = opcode & ConditionBitsMask;
    
    switch (conditionBits)
    {
    case ConditionEQBits: return "eq";
    case ConditionNEBits: return "ne";
    case ConditionCSBits: return "cs";
    case ConditionCCBits: return "cc";
    case ConditionMIBits: return "mi";
    case ConditionPLBits: return "pl";
    case ConditionVSBits: return "vs";
    case ConditionVCBits: return "vc";
    case ConditionHIBits: return "hi";
    case ConditionLSBits: return "ls";
    case ConditionGEBits: return "ge";
    case ConditionLTBits: return "lt";
    case ConditionGTBits: return "gt";
    case ConditionLEBits: return "le";
    case ConditionALBits: return "";
    default: return "_precond??";
    }
  }
}
