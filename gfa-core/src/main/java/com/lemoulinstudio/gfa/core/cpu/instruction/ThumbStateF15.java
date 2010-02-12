package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class ThumbStateF15 extends ThumbStateInstruction {

  public ThumbStateF15(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int LoadStoreBit = 0x00000800;
  static final protected int RbMask       = 0x00000700;

  public void execute() {
    ArmReg baseRegister = getRegister((opcode & RbMask) >>> 8);
    int baseValue = baseRegister.get();
    
    if ((opcode & LoadStoreBit) == 0) { // push
      for (int i = 0; i <= 7; i++)
        if ((opcode & (1 << i)) != 0) {
          memory.storeWord(baseValue, getRegister(i).get());
	  baseValue += 4;
	}
    }
    else { // pop
      for (int i = 0; i <= 7; i++)
        if ((opcode & (1 << i)) != 0) {
          getRegister(i).set(memory.loadWord(baseValue));
	  baseValue += 4;
	}
    }
    
    baseRegister.set(baseValue);
  }

  public String disassemble(int offset) {
    short opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "stmia" : "ldmia";
    String rb = getRegisterName((opcode & RbMask) >>> 8) + "!";
    String regList = "";
    for (int i = 0; i < 8; i++)
      if ((opcode & (1 << i)) != 0)
	regList += ", r" + i;
    regList = "{" + ((regList.length() < 2) ? "" : regList.substring(2)) + "}";
    return instru + " " + rb + ", " + regList;
  }

}
