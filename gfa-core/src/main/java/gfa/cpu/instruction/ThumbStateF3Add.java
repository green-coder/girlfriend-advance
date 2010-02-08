package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF3Add extends ThumbStateF3 {

  public ThumbStateF3Add(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int sourceValue = srcDstRegister.get();
    int result = sourceValue + immediateValue;
    srcDstRegister.set(result);
    
    CPSR.setCVFlagsForAdd(sourceValue, immediateValue, result);
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "add";
  }

}
