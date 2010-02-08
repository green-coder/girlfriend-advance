package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF4Tst extends ThumbStateF4 {

  public ThumbStateF4Tst(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int operand2 = sourceRegister.get();
    if (sourceRegister == PC) operand2 += 2;
    int result = destinationRegister.get() & operand2;
    CPSR.setBit(zFlagBit, (result == 0));
    CPSR.setBit(nFlagBit, (result < 0));
  }

  protected String getInstructionName() {
    return "tst";
  }

}
