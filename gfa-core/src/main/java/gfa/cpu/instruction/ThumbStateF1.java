package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF1 extends ThumbStateInstruction {

  public ThumbStateF1(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int RsMask     = 0x00000038;
  static final protected int RdMask     = 0x00000007;
  static final protected int OpMask     = 0x00001800;
  static final protected int OffsetMask = 0x000007c0;
  static final protected int LslBits    = 0x00000000;
  static final protected int LsrBits    = 0x00000800;
  static final protected int AsrBits    = 0x00001000;

  public void execute() {
    ArmReg sourceRegister = getRegister((opcode & RsMask) >>> 3);
    ArmReg destinationRegister = getRegister(opcode & RdMask);
    int shiftType = opcode & OpMask;
    int shiftAmount = (opcode & OffsetMask) >>> 6;
    
    int sourceValue = sourceRegister.get();
    // $$$ : Verifier si c'est pas +4 plutot que +2 a cause du shift.
    if (sourceRegister == PC) sourceValue += 2;
    
    if (shiftAmount == 0) {
      if (shiftType == LslBits) {
	// Do nothing : the old cFlagBit must be conserved.
	// Ne fait rien : l'ancien cFlagBit doit etre conserve.
      }
      else if (shiftType == LsrBits) {
	CPSR.setBit(cFlagBit, sourceValue < 0);
	sourceValue = 0;
      }
      else if (shiftType == AsrBits) {
	CPSR.setBit(cFlagBit, sourceValue < 0);
	// fill all with the bit 31 of operand2.
	sourceValue >>= 31;
      }
    }
    else { // ((shiftAmount > 0) && (shiftAmount < 32))
      if (shiftType == LslBits) {
        CPSR.setBit(cFlagBit, ((sourceValue & (1 << (32 - shiftAmount))) != 0));
	sourceValue <<= shiftAmount;
      }
      else if (shiftType == LsrBits) {
        CPSR.setBit(cFlagBit, ((sourceValue & (1 << (shiftAmount - 1))) != 0));
	sourceValue >>>= shiftAmount;
      }
      else if (shiftType == AsrBits) {
        CPSR.setBit(cFlagBit, ((sourceValue & (1 << (shiftAmount - 1))) != 0));
	sourceValue >>= shiftAmount;
      }
    }
    
    destinationRegister.set(sourceValue);
    
    CPSR.setBit(zFlagBit, (sourceValue == 0));
    CPSR.setBit(nFlagBit, (sourceValue < 0));
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru;
    int shiftType = opcode & OpMask;
    if (shiftType == LslBits) instru = "lsl";
    else if (shiftType == LsrBits) instru = "lsr";
    else if (shiftType == AsrBits) instru = "asr";
    else instru = "F1_??";
    String rd = getRegisterName(opcode & RdMask);
    String rs = getRegisterName((opcode & RsMask) >>> 3);
    int offset5 = (opcode & OffsetMask) >>> 6;
    return instru + " " + rd + ", " + rs + ", #" + offset5;
  }

}
