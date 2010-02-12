package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF2 extends ThumbStateInstruction {

  public ThumbStateF2(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int RsMask       = 0x00000038;
  static final protected int RdMask       = 0x00000007;
  static final protected int RnMask       = 0x000001c0;
  static final protected int ImmediateBit = 0x00000400;
  static final protected int OpBit        = 0x00000200;

  public void execute() {
    ArmReg sourceRegister = getRegister((opcode & RsMask) >>> 3);
    ArmReg destinationRegister = getRegister(opcode & RdMask);
    int value = (opcode & RnMask) >>> 6;
    
    int sourceValue = sourceRegister.get();
    if (sourceRegister == PC) sourceValue += 2;
    
    if ((opcode & ImmediateBit) == 0)
      value = getRegister(value).get();
    
    int result;
    if ((opcode & OpBit) != 0) { // substraction
      result = sourceValue - value;
      CPSR.setCVFlagsForSub(sourceValue, value, result);
    }
    else {
      result = sourceValue + value;
      CPSR.setCVFlagsForAdd(sourceValue, value, result);
    }
    
    destinationRegister.set(result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru = ((opcode & OpBit) != 0) ? "sub" : "add";
    String rd = getRegisterName(opcode & RdMask);
    String rs = getRegisterName((opcode & RsMask) >>> 3);
    int value = (opcode & RnMask) >>> 6;
    String rnOrOffset3 = ((opcode & ImmediateBit) == 0) ? getRegisterName(value) : ("#" + value);
    return instru + " " + rd + ", " + rs + ", " + rnOrOffset3;
  }

}
