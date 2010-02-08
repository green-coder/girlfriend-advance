package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;
import gfa.util.Hex;

public class ThumbStateF16 extends ThumbStateInstruction {

  public ThumbStateF16(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int CondMask    = 0x00000f00;
  static final protected int SOffsetMask = 0x000000ff;
  static final protected int EQBits      = 0x00000000;
  static final protected int NEBits      = 0x00000100;
  static final protected int CSBits      = 0x00000200;
  static final protected int CCBits      = 0x00000300;
  static final protected int MIBits      = 0x00000400;
  static final protected int PLBits      = 0x00000500;
  static final protected int VSBits      = 0x00000600;
  static final protected int VCBits      = 0x00000700;
  static final protected int HIBits      = 0x00000800;
  static final protected int LSBits      = 0x00000900;
  static final protected int GEBits      = 0x00000a00;
  static final protected int LTBits      = 0x00000b00;
  static final protected int GTBits      = 0x00000c00;
  static final protected int LEBits      = 0x00000d00;

  public void execute() {
    int offset = (int) ((byte) (opcode & SOffsetMask));
    int condType = opcode & CondMask;
    boolean condition;
    
    switch (condType) {
      case EQBits: condition =  CPSR.isBitSet(zFlagBit); break;
      case NEBits: condition = !CPSR.isBitSet(zFlagBit); break;
      case CSBits: condition =  CPSR.isBitSet(cFlagBit); break;
      case CCBits: condition = !CPSR.isBitSet(cFlagBit); break;
      case MIBits: condition =  CPSR.isBitSet(nFlagBit); break;
      case PLBits: condition = !CPSR.isBitSet(nFlagBit); break;
      case VSBits: condition =  CPSR.isBitSet(vFlagBit); break;
      case VCBits: condition = !CPSR.isBitSet(vFlagBit); break;
      case HIBits: condition =  CPSR.isBitSet(cFlagBit) && !CPSR.isBitSet(zFlagBit); break;
      case LSBits: condition = !CPSR.isBitSet(cFlagBit) ||  CPSR.isBitSet(zFlagBit); break;
      case GEBits: condition =  CPSR.isBitSet(nFlagBit) ==  CPSR.isBitSet(vFlagBit); break;
      case LTBits: condition =  CPSR.isBitSet(nFlagBit) !=  CPSR.isBitSet(vFlagBit); break;
      case GTBits: condition = !CPSR.isBitSet(zFlagBit) && (CPSR.isBitSet(nFlagBit) == CPSR.isBitSet(vFlagBit)); break;
      case LEBits: condition =  CPSR.isBitSet(zFlagBit) || (CPSR.isBitSet(nFlagBit) != CPSR.isBitSet(vFlagBit)); break;
      default: condition = false; // Si condType indefini, ne saute pas, ne fait rien.
    }
    
    if (condition) PC.set(PC.get() + 2 + offset * 2);
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru = "b" + condName[(opcode & CondMask) >>> 8];
    int sOffset8 = (int) ((byte) (opcode & SOffsetMask));
    int realTargetAddress = offset + 4 + sOffset8 * 2;
    return instru + " #" + Hex.toString(realTargetAddress);
  }

  static private String[] condName = {
    "eq", "ne", "cs", "cc", "mi", "pl", "vs", "vc",
    "hi", "ls", "ge", "lt", "gt", "le", "_??_", "_??_"
  };
  
}
