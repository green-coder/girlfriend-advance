package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ThumbStateF4Lsl extends ThumbStateF4 {

  public ThumbStateF4Lsl(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation() {
    int src = sourceRegister.get();
    if (sourceRegister == PC) src += 2; // +4 ?
    
    if (src < 0) src = (src & 0x1f) | 64; // avoid the case of a negative value.
    int dst = destinationRegister.get();
    
    if (src == 0)
      ; // Conservation of the old cFlagBit;
    else if (src < 32) {
      CPSR.setBit(cFlagBit, ((dst & (1 << (32 - src))) != 0));
      dst <<= src;
    }
    else if (src == 32) {
      CPSR.setBit(cFlagBit, ((dst & 0x01) != 0));
      dst = 0;
    }
    else { //if (src > 32)
      CPSR.setOff(cFlagBit);
      dst = 0;
    }
    
    destinationRegister.set(dst);
    CPSR.setBit(zFlagBit, (dst == 0));
    CPSR.setBit(nFlagBit, (dst < 0));
  }

  protected String getInstructionName() {
    return "lsl";
  }

}
