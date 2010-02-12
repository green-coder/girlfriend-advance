package com.lemoulinstudio.gfa.core.cpu.instruction;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public abstract class ArmStateArithmLogic extends ArmStateDataProcessing {

  public ArmStateArithmLogic(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  protected void applyOperation(int operand1, int operand2) {
    int result = operand1 & operand2;
    destinationRegister.set(result);
    tmpCPSR.setBit(zFlagBit, (result == 0));
    tmpCPSR.setBit(nFlagBit, (result < 0));
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String instru = getInstructionName() + preconditionToString(opcode);
    if ((opcode & SetConditionBit) != 0) {
      instru += "s";
    }
    String rd = getRegisterName((opcode & DestinationMask) >>> 12);
    String rn = getRegisterName((opcode & Operand1Mask) >>> 16);
    String op2 = disassembleOp2(opcode);

    return instru + " " + rd + ", " + rn + ", " + op2;
  }

  abstract protected String getInstructionName();

}
