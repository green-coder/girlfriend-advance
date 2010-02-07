package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.*;

public class ArmStateMul
  extends ArmStateInstruction
{

  public ArmStateMul(ArmReg[][] regs, MemoryInterface memory)
  {
    super(regs, memory);
  }

  static final protected int AccumulateBit   = 0x00200000;
  static final protected int SetConditionBit = 0x00100000;
  static final protected int RdMask          = 0x000f0000;
  static final protected int RnMask          = 0x0000f000;
  static final protected int RsMask          = 0x00000f00;
  static final protected int RmMask          = 0x0000000f;

  public void execute()
  {
    if (!isPreconditionSatisfied()) return;
    
    ArmReg rd = getRegister((opcode & RdMask) >>> 16);
    ArmReg rn = getRegister((opcode & RnMask) >>> 12);
    ArmReg rs = getRegister((opcode & RsMask) >>> 8);
    ArmReg rm = getRegister(opcode & RmMask);
    int result = rm.get() * rs.get();
    
    if ((opcode & AccumulateBit) != 0)
      result += rn.get();
    
    rd.set(result);
    
    if ((opcode & SetConditionBit) != 0)
    {
      CPSR.setBit(zFlagBit, (result == 0));
      CPSR.setBit(nFlagBit, (result < 0));
      //CPSR.setBit(cFlagBit, meaninglessCondition);
    }
  }

  public String disassemble(int offset)
  {
    int opcode = getOpcode(offset);
    String rd = getRegisterName((opcode & RdMask) >>> 16);
    String rn = getRegisterName((opcode & RnMask) >>> 12);
    String rs = getRegisterName((opcode & RsMask) >>> 8);
    String rm = getRegisterName(opcode & RmMask);
    
    String sTag = ((opcode & SetConditionBit) != 0) ? "S" : "";
    
    if ((opcode & AccumulateBit) != 0)
      return "mla" + preconditionToString(opcode) + sTag + " " + rd + ", " + rm + ", " + rs + ", " + rn;
    else
      return "mul" + preconditionToString(opcode) + sTag + " " + rd + ", " + rm + ", " + rs;
  }
}
