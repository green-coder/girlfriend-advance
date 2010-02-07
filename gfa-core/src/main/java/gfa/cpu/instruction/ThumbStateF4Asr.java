package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF4Asr
  extends ThumbStateF4
{

  public ThumbStateF4Asr(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  protected void applyOperation()
  {
    int src = sourceRegister.get();
    if (sourceRegister == PC) src += 2; // asr==decalage, alors +4 ?
    
    if (src < 0) src = (src & 0x1f) | 64; // avoid the case of a negative value.
    int dst = destinationRegister.get();
    
    if (src == 0)
      ; // Conservation of the old cFlagBit;
    else if (src < 32)
    {
      CPSR.setBit(cFlagBit, ((dst & (1 << (src - 1))) != 0));
      dst >>= src;
    }
    else //if (src >= 32)
    {
      CPSR.setBit(cFlagBit, ((dst & 0x80000000) != 0));
      dst >>= 31;
    }
    
    destinationRegister.set(dst);
    CPSR.setBit(zFlagBit, (dst == 0));
    CPSR.setBit(nFlagBit, (dst < 0));
  }

  protected String getInstructionName()
  {
    return "asr";
  }

}
