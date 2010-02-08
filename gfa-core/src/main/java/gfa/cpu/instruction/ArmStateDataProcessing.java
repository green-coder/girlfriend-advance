package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;
import gfa.util.Hex;

abstract public class ArmStateDataProcessing extends ArmStateInstruction {

  public ArmStateDataProcessing(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
    tmpCPSR = new ArmReg(0);
  }

  protected ArmReg tmpCPSR;
  protected ArmReg destinationRegister;

  static final protected int ImmediateBit      = 0x02000000;
  static final protected int SetConditionBit   = 0x00100000;
  static final protected int Operand1Mask      = 0x000f0000;
  static final protected int Operand2Mask      = 0x00000fff;
  static final protected int DestinationMask   = 0x0000f000;
  static final protected int Op2RegisterMask   = 0x0000000f;
  static final protected int ShiftModeBit      = 0x00000010;
  static final protected int ShiftTypeMask     = 0x00000060;
  static final protected int ShiftAmountMask   = 0x00000f80;
  static final protected int ShiftRegisterMask = 0x00000f00;
  static final protected int LogicalLeftBits   = 0x00000000;
  static final protected int LogicalRightBits  = 0x00000020;
  static final protected int ArithmRightBits   = 0x00000040;
  static final protected int RotateRightBits   = 0x00000060;
  static final protected int Op2RotateMask     = 0x00000f00;
  static final protected int Op2ImmediateMask  = 0x000000ff;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    tmpCPSR.set(CPSR);
    ArmReg regOp1 = getRegister((opcode & Operand1Mask) >>> 16);
    
    int operand1 = regOp1.get();
    if (regOp1 == PC) operand1 += 4;
    
    int operand2;
    destinationRegister = getRegister((opcode & DestinationMask) >>> 12);
    
    if ((opcode & ImmediateBit) == 0) { // operand 2 is a register
      ArmReg rm = getRegister(opcode & Op2RegisterMask);
      operand2 = rm.get();
      if (rm == PC) operand2 += 4;
      
      int shiftType = opcode & ShiftTypeMask;
      int shiftAmount;
      
      if ((opcode & ShiftModeBit) == 0) { // The shift amount is a value.
        shiftAmount = (opcode & ShiftAmountMask) >>> 7;
	
	if (shiftAmount == 0) {
          if (shiftType == LogicalLeftBits) {
            // Do nothing : the old cFlagBit must be conserved.
            // Ne fait rien : l'ancien cFlagBit doit etre conserve.
	  }
	  else if (shiftType == LogicalRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	    operand2 = 0;
	  }
	  else if (shiftType == ArithmRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
            // fill all with the bit 31 of operand2.
	    operand2 >>= 31;
	  }
	  else if (shiftType == RotateRightBits) {
            boolean isCFlagBit = tmpCPSR.isBitSet(cFlagBit);
	    tmpCPSR.setBit(cFlagBit, ((operand2 & 0x00000001) != 0));
	    operand2 >>>= 1;
	    if (isCFlagBit) operand2 |= 0x80000000;
	  }
	}
	else {
          if (shiftType == LogicalLeftBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (32 - shiftAmount))) != 0));
	    operand2 <<= shiftAmount;
	  }
	  else if (shiftType == LogicalRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
	    operand2 >>>= shiftAmount;
	  }
	  else if (shiftType == ArithmRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
	    operand2 >>= shiftAmount;
	  }
	  else if (shiftType == RotateRightBits) {
            operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
	    tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	  }
	}
      }
      else { // Register specified shift amount.
        ArmReg regToShift = getRegister((opcode & ShiftRegisterMask) >>> 8);
	shiftAmount = regToShift.get();
	if (shiftAmount < 0) shiftAmount = (shiftAmount & 0x0000001f) | 64; // avoid the case of a negative value.
	if (regToShift == PC) shiftAmount += 8;
	
	if (shiftAmount == 0)
          ; // Ne rien faire : conservation de l'ancien cFlag;
	else if (shiftAmount < 32) {
          if (shiftType == LogicalLeftBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (32 - shiftAmount))) != 0));
	    operand2 <<= shiftAmount;
	  }
	  else if (shiftType == LogicalRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
	    operand2 >>>= shiftAmount;
	  }
	  else if (shiftType == ArithmRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
	    operand2 >>= shiftAmount;
	  }
	  else if (shiftType == RotateRightBits) {
            operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
	    tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	  }
	}
	else if (shiftAmount == 32) {
          if (shiftType == LogicalLeftBits) {
	    tmpCPSR.setBit(cFlagBit, ((operand2 & 0x00000001) != 0));
	    operand2 = 0;
	  }
	  else if (shiftType == LogicalRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	    operand2 = 0;
	  }
	  else if (shiftType == ArithmRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	    operand2 >>= 31;
	  }
	  else if (shiftType == RotateRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
          }
	}
	else if (shiftAmount > 32) {
	  if (shiftType == LogicalLeftBits) {
            tmpCPSR.setOff(cFlagBit);
	    operand2 = 0;
	  }
	  else if (shiftType == LogicalRightBits) {
            tmpCPSR.setOff(cFlagBit);
	    operand2 = 0;
	  }
	  else if (shiftType == ArithmRightBits) {
            tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	    operand2 >>= 31;
	  }
	  else if (shiftType == RotateRightBits) {
	    shiftAmount = ((shiftAmount - 1) & 0x0000001f) + 1; // put shiftAmount in the range [1..32]
	    operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
	    tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
	  }
	}
      }
    }
    else { // operand 2 is an immediate value
      int immediateValue = opcode & Op2ImmediateMask;
      int twiceRotateValue = ((opcode & Op2RotateMask) >>> 8) * 2;
      operand2 = (immediateValue >>> twiceRotateValue) | (immediateValue << (32 - twiceRotateValue));
    }
    
    applyOperation(operand1, operand2);
    
    if ((opcode & SetConditionBit) != 0) {
      if (destinationRegister == PC)
	CPSR.set(getSPSR());
      else
        CPSR.set(tmpCPSR);
    }
  }

  abstract protected void applyOperation(int operand1, int operand2);

  protected String disassembleOp2(int opcode) {
    if ((opcode & ImmediateBit) == 0) { // operand 2 is a register
      String rm = getRegisterName(opcode & Op2RegisterMask);
      
      String shift = "";
      int shiftType = opcode & ShiftTypeMask;
      if (shiftType == LogicalLeftBits) shift = ", lsl";
      else if (shiftType == LogicalRightBits) shift = ", lsr";
      else if (shiftType == ArithmRightBits) shift = ", asr";
      else if (shiftType == RotateRightBits) shift = ", ror";
      
      if ((opcode & ShiftModeBit) == 0) // The shift amount is a value.
        shift += " " + ((opcode & ShiftAmountMask) >>> 7);
      else // Register specified shift amount.
	shift += " " + getRegisterName((opcode & ShiftRegisterMask) >>> 8);
      
      if ((shiftType == RotateRightBits) &&
	  ((opcode & ShiftModeBit) == 0) &&
	  (((opcode & ShiftAmountMask) >>> 7) == 0))
	shift = ", rrx";
      else if ((shiftType == LogicalLeftBits) &&
	       ((opcode & ShiftModeBit) == 0) &&
	       (((opcode & ShiftAmountMask) >>> 7) == 0))
	shift = "";
      
      return rm + shift;
    }
    else { // operand 2 is an immediate value
      int immediateValue = opcode & Op2ImmediateMask;
      int twiceRotateValue = ((opcode & Op2RotateMask) >>> 8) * 2;
      return "#" + Hex.toString(((immediateValue >>> twiceRotateValue) |
				 (immediateValue << (32 - twiceRotateValue))));
    }
  }

}
