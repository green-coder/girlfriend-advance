package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;
import gfa.util.Hex;

public class ArmStateLdrStr extends ArmStateInstruction {

  public ArmStateLdrStr(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int ImmediateBit     = 0x02000000;
  static final protected int PreIndexingBit   = 0x01000000;
  static final protected int UpDownBit        = 0x00800000;
  static final protected int ByteWordBit      = 0x00400000;
  static final protected int WriteBackBit     = 0x00200000;
  static final protected int LoadStoreBit     = 0x00100000;
  static final protected int RnMask           = 0x000f0000;
  static final protected int RdMask           = 0x0000f000;
  static final protected int ImmediateMask    = 0x00000fff;
  static final protected int RmMask           = 0x0000000f;
  static final protected int ShiftTypeMask    = 0x00000060;
  static final protected int ShiftAmountMask  = 0x00000f80;
  static final protected int LogicalLeftBits  = 0x00000000;
  static final protected int LogicalRightBits = 0x00000020;
  static final protected int ArithmRightBits  = 0x00000040;
  static final protected int RotateRightBits  = 0x00000060;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    ArmReg baseRegister = getRegister((opcode & RnMask) >>> 16);
    ArmReg srcDstRegister = getRegister((opcode & RdMask) >>> 12);
    
    int offset = 0;
    if ((opcode & ImmediateBit) == 0)
      offset = opcode & ImmediateMask;
    else {
      int offsetValue = getRegister(opcode & RmMask).get();
      int shiftAmount = (opcode & ShiftAmountMask) >>> 7;
      int shiftType = opcode & ShiftTypeMask;
      if (shiftType == LogicalLeftBits)       offset = offsetValue << shiftAmount;
      else if (shiftType == LogicalRightBits) offset = offsetValue >>> shiftAmount;
      else if (shiftType == ArithmRightBits)  offset = offsetValue >> shiftAmount;
      else if (shiftType == RotateRightBits)  offset = (offsetValue >>> shiftAmount) | (offsetValue << (32 - shiftAmount));
    }
    
    int newAdress = baseRegister.get();
    if (baseRegister == PC) newAdress += 4;
    
    if ((opcode & UpDownBit) == 0)
      offset = -offset;
    
    if ((opcode & PreIndexingBit) != 0) // PreIndex
      newAdress += offset;
    
/*
    int oldMode = getCurrentMode();
    if (((opcode & WriteBackBit) != 0) && // if (WriteBack and
        ((opcode & PreIndexingBit) == 0)) //     PostIndex)
      setMode(usrMode);
    ...
    setMode(oldMode);
    
    if (((opcode & WriteBackBit) != 0) && // if (WriteBack and
	((opcode & PreIndexingBit) == 0)) //     PostIndex)
      signalError("A non-emulated instruction for an OS has been used !");
*/
    
    if ((opcode & LoadStoreBit) == 0) { // Store
      int valueToStore = srcDstRegister.get();
      if (srcDstRegister == PC) valueToStore += 8;
      
      if ((opcode & ByteWordBit) == 0) { // Word
        int wordAlignedAdress = newAdress & 0xfffffffc;
	memory.storeWord(wordAlignedAdress, valueToStore);
      }
      else                                         // Byte
        memory.storeByte(newAdress, (byte) valueToStore);
    }
    else {                                          // Load
      if ((opcode & ByteWordBit) == 0) { // Word
	int wordAlignedAdress = newAdress & 0xfffffffc;
	int rightRotate = (newAdress & 0x00000003) << 3;
	int value = memory.loadWord(wordAlignedAdress);
	srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
      }
      else                                         // Byte
        srcDstRegister.set(0x000000ff & memory.loadByte(newAdress));
    }
    
    if ((opcode & PreIndexingBit) == 0) // PostIndex
      newAdress += offset;
    
    if (((opcode & WriteBackBit) != 0) || // if (WriteBack or
	((opcode & PreIndexingBit) == 0)) //     PostIndex)
      baseRegister.set(newAdress);
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);

    String instru = ((opcode & LoadStoreBit) == 0) ? "str" : "ldr";
    instru += preconditionToString(opcode);
    if ((opcode & ByteWordBit) != 0) instru += "b";
    if (((opcode & WriteBackBit) != 0) &&
        ((opcode & PreIndexingBit) == 0)) instru += "t";
    
    String rd = getRegisterName((opcode & RdMask) >>> 12);
    String rn = getRegisterName((opcode & RnMask) >>> 16);
    String rm = getRegisterName(opcode & RmMask);
    rm = (((opcode & UpDownBit) == 0) ? ", -" : ", ") + rm;
    
    String shift = "";
    int shiftType = opcode & ShiftTypeMask;
    if (shiftType == LogicalLeftBits)       shift = ", lsl ";
    else if (shiftType == LogicalRightBits) shift = ", lsr ";
    else if (shiftType == ArithmRightBits)  shift = ", asr ";
    else if (shiftType == RotateRightBits)  shift = ", ror ";
    shift += (opcode & ShiftAmountMask) >>> 7; // shiftAmount
    if ((shiftType == LogicalLeftBits) &&
	(((opcode & ShiftAmountMask) >>> 7) == 0))
      shift = "";
    
    String offset2 = " + #" + (((opcode & UpDownBit) == 0) ? "-": "") +
	Hex.toString(opcode & ImmediateMask);
    if ((opcode & ImmediateMask) == 0) offset2 = "";
    
    String address;
    if ((opcode & PreIndexingBit) != 0) { // PreIndex
      if ((opcode & ImmediateBit) == 0) address = "[" + rn + offset2 + "]";
      else address = "[" + rn + rm + shift + "]";	
      if ((opcode & WriteBackBit) != 0) address += "!";
    }
    else { // PostIndex
      if ((opcode & ImmediateBit) == 0) address = "[" + rn + "]" + offset2;
      else address = "[" + rn + "]" + rm + shift;
    }
    
    return instru + " " + rd + ", " + address;
  }

}
