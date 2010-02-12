package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF3Cmp extends ThumbStateF3 {

  public ThumbStateF3Cmp(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int sourceValue = srcDstRegister.get();
    int result = sourceValue - immediateValue;

    CPSR.setCVFlagsForSub(sourceValue, immediateValue, result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "cmp";
  }

}
