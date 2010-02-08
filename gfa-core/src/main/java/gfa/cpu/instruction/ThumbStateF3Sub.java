package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF3Sub extends ThumbStateF3 {

  public ThumbStateF3Sub(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int sourceValue = srcDstRegister.get();
    int result = sourceValue - immediateValue;
    srcDstRegister.set(result);

    CPSR.setCVFlagsForSub(sourceValue, immediateValue, result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "sub";
  }

}
