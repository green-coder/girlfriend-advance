package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF5Cmp extends ThumbStateF5 {

  public ThumbStateF5Cmp(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int operand1 = destinationRegister.get();
    //int operand2 = sourceValue;
    int result = operand1 - sourceValue;
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
    CPSR.setCVFlagsForSub(operand1, sourceValue, result);
  }

  protected String getInstructionName() {
    return "cmp";
  }

}
