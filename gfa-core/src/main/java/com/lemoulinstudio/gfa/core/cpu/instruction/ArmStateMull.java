package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import java.math.BigInteger;

public class ArmStateMull extends ArmStateInstruction {

  public ArmStateMull(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int UnsignedBit     = 0x00400000;
  static final protected int AccumulateBit   = 0x00200000;
  static final protected int SetConditionBit = 0x00100000;
  static final protected int RdHiMask        = 0x000f0000;
  static final protected int RdLoMask        = 0x0000f000;
  static final protected int RsMask          = 0x00000f00;
  static final protected int RmMask          = 0x0000000f;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    ArmReg rdHi = getRegister((opcode & RdHiMask) >>> 16);
    ArmReg rdLo = getRegister((opcode & RdLoMask) >>> 12);
    ArmReg rs = getRegister((opcode & RsMask) >>> 8);
    ArmReg rm = getRegister(opcode & RmMask);
    long longResult;
    
    if ((opcode & UnsignedBit) != 0) { // signed multiplication
      longResult = (long) rm.get() * (long) rs.get();
      
      if ((opcode & AccumulateBit) != 0)
        longResult += ((long) rdHi.get() << 32) | (0xffffffffL & (long) rdLo.get());
    }
    else { // unsigned multiplication 64 bits ... :-(
      BigInteger operand1 = new BigInteger(Long.toString(0xffffffffL & (long) rm.get()));
      BigInteger operand2 = new BigInteger(Long.toString(0xffffffffL & (long) rs.get()));
      BigInteger bigResult = operand1.multiply(operand2);
      
      if ((opcode & AccumulateBit) != 0) {
        BigInteger hi = new BigInteger(Long.toString(0xffffffffL & (long) rdHi.get()));
	BigInteger lo = new BigInteger(Long.toString(0xffffffffL & (long) rdLo.get()));
	bigResult = bigResult.add(hi.shiftLeft(32).or(lo));
      }
      
      longResult = bigResult.longValue();
    }
    
    rdLo.set((int) longResult);
    rdHi.set((int) (longResult >> 32));
    
    if ((opcode & SetConditionBit) != 0) {
      CPSR.setBit(zFlagBit, (longResult == 0));
      CPSR.setBit(nFlagBit, (longResult < 0));
      //CPSR.setBit(cFlagBit, meaninglessCondition);
      //CPSR.setBit(vFlagBit, meaninglessCondition);
    }
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String instruc = ((opcode & UnsignedBit) != 0) ? "s" : "u";
    instruc += ((opcode & AccumulateBit) != 0) ? "mlal" : "mull";
    instruc += preconditionToString(opcode);
    instruc += ((opcode & SetConditionBit) != 0) ? "s" : "";
    String rdHi = getRegisterName((opcode & RdHiMask) >>> 16);
    String rdLo = getRegisterName((opcode & RdLoMask) >>> 12);
    String rs = getRegisterName((opcode & RsMask) >>> 8);
    String rm = getRegisterName(opcode & RmMask);
    return instruc + " " + rdLo + ", " + rdHi + ", " + rm + ", " + rs;
  }

}
