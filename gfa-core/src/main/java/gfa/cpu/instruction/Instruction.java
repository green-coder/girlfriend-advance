package gfa.cpu.instruction;

import gfa.cpu.*;
import gfa.memory.MemoryInterface;

/**
 * This class is the super-class of all instructions of the ARM7TDMI processor.
 * An instruction can be executed and disassembled, and gives some semantic informations.
 * Instruction objects are immutable.
 */
public abstract class Instruction
{
  static final protected int modeBitsMask = 0x0000001f; // Cover all bits concerning the cpu mode.
  static final protected int usrModeBits  = 0x00000010; // User mode.
  static final protected int fiqModeBits  = 0x00000011; // FIQ mode.
  static final protected int irqModeBits  = 0x00000012; // IRQ mode.
  static final protected int svcModeBits  = 0x00000013; // Supervisor mode.
  static final protected int abtModeBits  = 0x00000017; // Abort mode.
  static final protected int undModeBits  = 0x0000001b; // Undefined mode.
  static final protected int sysModeBits  = 0x0000001f; // System mode.

  static final public int nFlagBit  = 0x80000000; // negative or less than.
  static final public int zFlagBit  = 0x40000000; // zero.
  static final public int cFlagBit  = 0x20000000; // carry or borrow or extends.
  static final public int vFlagBit  = 0x10000000; // overflow.
  static final public int iFlagBit  = 0x00000080; // irq disable.
  static final public int fFlagBit  = 0x00000040; // fiq disable.
  static final public int tFlagBit  = 0x00000020; // (thumb) state bit.

  protected final ArmReg[][] regs;
  protected final MemoryInterface memory;
  protected final ArmReg CPSR;
  protected final ArmReg PC;

  /**
   * "offset" is the position in the memory of the represented instruction.
   * "regs" is a reference to the set of all registers of the ARM7TDMI
   * which entirely define the actual state of the cpu.
   */
  public Instruction(ArmReg[][] regs, MemoryInterface memory)
  {
    this.regs = regs;
    this.memory = memory;
    CPSR = regs[usrModeBits][16];
    PC = regs[usrModeBits][15];
  }

  /**
   * Execute the current instruction.
   */
  abstract public void execute();

  /**
   * Return a String disassembled representation of the instruction at the specified offset.
   */
  abstract public String disassemble(int offset);

  /**
   * Return an array of all parameters used by the current instruction.
   */
  //final public Parameter[] getParameters() {return null;}



  /**
   * Return an array of array of all registers of the Arm7Tdmi.
   * The first array level is the mode array, the second array
   * level is the register number array.
   */
  protected ArmReg getRegister(int registerNumber)
  {
/*
    if (regs[getMode()] == null)
    {
      System.out.println("Instruction.getRegister().modeBits = " + gfa.util.Hex.toString(getMode()));
      System.out.println("Instruction.getRegister().registerNumber = " + registerNumber);
      gfa.debug.StepFrame.BREAK();
      return regs[svcModeBits][registerNumber];
    }
*/
    return regs[getMode()][registerNumber];
  }

  protected String getRegisterName(int registerNumber)
  {
    if (registerNumber < 13) return "r" + registerNumber;
    else if (registerNumber == 13) return "sp";
    else if (registerNumber == 14) return "lr";
    else if (registerNumber == 15) return "pc";
    else if (registerNumber == 16) return "cpsr";
    else if (registerNumber == 17) return "spsr";
    else return "reg??_" + registerNumber;
  }

  protected ArmReg getRegister(int registerNumber, int modeBits)
  {
    return regs[modeBits][registerNumber];
  }

  protected ArmReg getSP()
  {
    return regs[getMode()][13];
  }

  protected ArmReg getLR()
  {
    return regs[getMode()][14];
  }

  protected ArmReg getSPSR()
  {
    return regs[getMode()][17];
  }

  protected int getMode()
  {
    return (CPSR.get() & modeBitsMask);
  }

  protected void setMode(int modeBits)
  {
    CPSR.set((CPSR.get() & ~modeBitsMask) | modeBits);
  }

  protected void setArmState()
  {
    CPSR.setOff(tFlagBit);
  }

  protected void setThumbState()
  {
    CPSR.setOn(tFlagBit);
  }

  protected void signalError(String errorMsg)
  {
    System.out.println("ERROR : " + errorMsg);
    Arm7Tdmi.cpu.stopPlease();
  }
}
