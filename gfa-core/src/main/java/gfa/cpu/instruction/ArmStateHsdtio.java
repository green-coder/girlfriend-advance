package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateHsdtio extends ArmStateInstruction {

  static final protected int PreIndexingBit  = 0x01000000;
  static final protected int UpDownBit       = 0x00800000;
  static final protected int WriteBackBit    = 0x00200000;
  static final protected int LoadStoreBit    = 0x00100000;
  static final protected int RnMask          = 0x000f0000;
  static final protected int RdMask          = 0x0000f000;
  static final protected int HiOffsetMask    = 0x00000f00;
  static final protected int SHMask          = 0x00000060;
  static final protected int LoOffsetMask    = 0x0000000f;
  
  static final protected int SWPInstrBits    = 0x00000000;
  static final protected int UnsignHalfBits  = 0x00000020;
  static final protected int SignedByteBits  = 0x00000040;
  static final protected int SignedHalfBits  = 0x00000060;

  public ArmStateHsdtio(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  /*
   * Halfword and Signed Data Transfert : Immediat Offset
   */
  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    int offset = ((opcode & HiOffsetMask) >>> 4) | (opcode & LoOffsetMask);
    ArmReg baseRegister = getRegister((opcode & RnMask) >>> 16);
    ArmReg srcDstRegister = getRegister((opcode & RdMask) >>> 12);
    
    int address = baseRegister.get();
    if (baseRegister == PC) address += 4;
    
    if ((opcode & UpDownBit) == 0)
      offset = -offset;
    
    if ((opcode & PreIndexingBit) != 0) // PreIndex
      address += offset;
    
    if ((opcode & LoadStoreBit) == 0) { // Store
      int SHBits = opcode & SHMask;
      if (SHBits == UnsignHalfBits) {
        int valueToStore = srcDstRegister.get();
	if (srcDstRegister == PC) valueToStore += 8;
	memory.storeHalfWord(address, (short) valueToStore);
      }
      else if (SHBits == SignedByteBits)
	signalError("Illegal combinaison : store signed byte");
      else if (SHBits == SignedHalfBits)
	signalError("Illegal combinaison : store signed halfword");
    }
    else {                                         // Load
      int SHBits = opcode & SHMask;
      if (SHBits == UnsignHalfBits)
	// $$$ : C'est pas dit, mais on suppose que la partie haute est remplie avec des zeros.
	srcDstRegister.set(0x0000ffff & memory.loadHalfWord(address));
      else if (SHBits == SignedByteBits)
	srcDstRegister.set(memory.loadByte(address));
      else if (SHBits == SignedHalfBits)
	srcDstRegister.set(memory.loadHalfWord(address));
    }
    
    if ((opcode & PreIndexingBit) == 0) // PostIndex
      address += offset;
    
    if (((opcode & WriteBackBit) != 0) || // WriteBack
	((opcode & PreIndexingBit) == 0)) // PostIndex
      baseRegister.set(address);

    if (((opcode & WriteBackBit) != 0) && // WriteBack
	((opcode & PreIndexingBit) == 0)) // PostIndex
      signalError("WriteBack and PostIndex are both selected !");
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "str" : "ldr";
    instru += preconditionToString(opcode);
    int SHBits = opcode & SHMask;
    if (SHBits == UnsignHalfBits)
      instru += "h";
    else if (SHBits == SignedByteBits)
      instru += ((opcode & LoadStoreBit) == 0) ? "_?sb?" : "sb";
    else if (SHBits == SignedHalfBits)
      instru += ((opcode & LoadStoreBit) == 0) ? "_?sh?" : "sh";
    else instru += "_?swp?";
    
    String rd = getRegisterName((opcode & RdMask) >>> 12);
    
    String rn = getRegisterName((opcode & RnMask) >>> 16);
    String immOffset = "" + (((opcode & HiOffsetMask) >>> 4) | (opcode & LoOffsetMask));
    immOffset = (((opcode & UpDownBit) == 0) ? "-" : "+") + immOffset;
    String address;
    if ((opcode & PreIndexingBit) != 0) { // PreIndex
      address = ("[" + rn + ", " + immOffset + "]");
      if ((opcode & WriteBackBit) != 0) address += "!";
    }
    else // PostIndex
      address = ("[" + rn + "], " + immOffset);
    
    return instru + " " + rd + ", " + address;
  }

}
