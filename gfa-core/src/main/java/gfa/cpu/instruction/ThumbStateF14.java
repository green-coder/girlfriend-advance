package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ThumbStateF14
  extends ThumbStateInstruction
{

  public ThumbStateF14(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int LoadStoreBit = 0x00000800;
  static final protected int PcLrBit      = 0x00000100;

  public void execute()
  {
    ArmReg SP = getSP();
    int spValue = SP.get();
    
    if ((opcode & LoadStoreBit) == 0) // push
    {
      if ((opcode & PcLrBit) != 0) // push LR too
      {
        spValue -= 4;
	memory.storeWord(spValue, getLR().get());
      }
      
      for (int i = 7; i >= 0; i--)
        if ((opcode & (1 << i)) != 0)
        {
          spValue -= 4;
	  memory.storeWord(spValue, getRegister(i).get());
	}
    }
    else // pop
    {
      for (int i = 0; i <= 7; i++)
        if ((opcode & (1 << i)) != 0)
	{
          getRegister(i).set(memory.loadWord(spValue));
	  spValue += 4;
	}
      
      if ((opcode & PcLrBit) != 0) // pop PC too
      {
        PC.set(memory.loadWord(spValue) & 0xfffffffe);
	spValue += 4;
      }
    }
    
    SP.set(spValue);
  }

  public String disassemble(int offset)
  {
    short opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "push" : "pop";
    String regList = "";
    for (int i = 0; i < 8; i++)
      if ((opcode & (1 << i)) != 0)
	regList += ", r" + i;
    if ((opcode & PcLrBit) != 0)
      regList += ((opcode & LoadStoreBit) == 0) ? ", lr" : ", pc";
    regList = "{" + ((regList.length() < 2) ? "" : regList.substring(2)) + "}";
    return instru + " " + regList;
  }
}
