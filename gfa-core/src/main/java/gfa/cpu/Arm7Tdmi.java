package gfa.cpu;

import java.math.BigInteger;
import java.io.*;
import gfa.memory.*;
import gfa.time.*;
import gfa.debug.*;
import gfa.util.*;

public class Arm7Tdmi
{
  static public PrintStream debug;
  static
  {
      //try {debug = new PrintStream(new FileOutputStream("debug.txt"));}
      //catch (IOException e) {debug = System.out;}
      debug = System.out;
  }

  protected MemoryInterface memory;
  protected Time time;

/*
  r13 a.k.a. SP "Stack Pointer"   : utilise par convention pour servir de base pur la pile.
  r14 a.k.a. LR "Link Register"   : adresse de depart pour revenir d'une interruption.
  r15 a.k.a. PC "Program Counter" : contient l'adresse de l'instruction courante.
*/

  // General-purpose registers.
  public ArmReg[] currentRegisters;
  public ArmReg[] usrRegisters;
  public ArmReg[] fiqRegisters;
  public ArmReg[] irqRegisters;
  public ArmReg[] svcRegisters;
  public ArmReg[] abtRegisters;
  public ArmReg[] undRegisters;
  public ArmReg[] sysRegisters;

  public ArmReg PC;

  public ArmReg CPSR; // Contient les drapeaux du code conditionnel.
  //            SPSR; // "Saved Program Status Register" : sauvegarde de cpsr pour retour d'interruption.
  public ArmReg SPSR_fiq;
  public ArmReg SPSR_svc;
  public ArmReg SPSR_abt;
  public ArmReg SPSR_irq;
  public ArmReg SPSR_und;

  static final public int nFlagBit  = 0x80000000; // negative or less than.
  static final public int zFlagBit  = 0x40000000; // zero.
  static final public int cFlagBit  = 0x20000000; // carry or borrow or extends.
  static final public int vFlagBit  = 0x10000000; // overflow.
  static final public int iFlagBit  = 0x00000080; // irq disable.
  static final public int fFlagBit  = 0x00000040; // fiq disable.
  static final public int tFlagBit  = 0x00000020; // (thumb) state bit.

  static final protected int cFlagBitNumber = 29; // the c-flag is the bit number 29.

  static final public int modeBitsMask = 0x0000001f; // Cover all bits concerning mode.
  static final public int usrModeBits  = 0x00000010; // User mode.
  static final public int fiqModeBits  = 0x00000011; // FIQ mode.
  static final public int irqModeBits  = 0x00000012; // IRQ mode.
  static final public int svcModeBits  = 0x00000013; // Supervisor mode.
  static final public int abtModeBits  = 0x00000017; // Abort mode.
  static final public int undModeBits  = 0x0000001b; // Undefined mode.
  static final public int sysModeBits  = 0x0000001f; // System mode.

  static final protected int resetVectorAddress                = 0x00000000;
  static final protected int undefinedInstructionVectorAddress = 0x00000004;
  static final protected int softwareInterrupVectorAddress     = 0x00000008;
  static final protected int prefetchAbortVectorAddress        = 0x0000000c;
  static final protected int dataAbortVectorAddress            = 0x00000010;
  static final protected int irqVectorAddress                  = 0x00000018;
  static final protected int fiqVectorAddress                  = 0x0000001c;

  public Arm7Tdmi()
  {
    initRegisters();
  }

  protected void initRegisters()
  {
    usrRegisters = new ArmReg[18];
    fiqRegisters = new ArmReg[18];
    irqRegisters = new ArmReg[18];
    svcRegisters = new ArmReg[18];
    abtRegisters = new ArmReg[18];
    undRegisters = new ArmReg[18];
    sysRegisters = new ArmReg[18];
    currentRegisters = null; // need a first reset to set it up.

    for (int i = 0; i <= 7; i++)
    {
      //ArmReg reg = ((i == 9) ? new DebugArmReg(0, "R" + i, debug) : new ArmReg(0));
      ArmReg reg = new ArmReg(0);
      usrRegisters[i] = reg;
      fiqRegisters[i] = reg;
      irqRegisters[i] = reg;
      svcRegisters[i] = reg;
      abtRegisters[i] = reg;
      undRegisters[i] = reg;
      sysRegisters[i] = reg;
    }

    for (int i = 8; i <= 12; i++)
    {
      //ArmReg reg = ((i == 9) ? new DebugArmReg(0, "R" + i, debug) : new ArmReg(0));
      ArmReg reg = new ArmReg(0);
      usrRegisters[i] = reg;
      irqRegisters[i] = reg;
      svcRegisters[i] = reg;
      abtRegisters[i] = reg;
      undRegisters[i] = reg;
      sysRegisters[i] = reg;

      ArmReg reg_fiq = new ArmReg(0);
      fiqRegisters[i] = reg_fiq;
    }

    ArmReg r13 = new ArmReg(0);
    ArmReg r13_fiq = new ArmReg(0);
    ArmReg r13_irq = new ArmReg(0);
    ArmReg r13_svc = new ArmReg(0);
    ArmReg r13_abt = new ArmReg(0);
    ArmReg r13_und = new ArmReg(0);

    usrRegisters[13] = r13;
    fiqRegisters[13] = r13_fiq;
    irqRegisters[13] = r13_irq;
    svcRegisters[13] = r13_svc;
    abtRegisters[13] = r13_abt;
    undRegisters[13] = r13_und;
    sysRegisters[13] = r13;

    ArmReg r14 = new ArmReg(0);
    ArmReg r14_fiq = new ArmReg(0);
    ArmReg r14_irq = new ArmReg(0);
    ArmReg r14_svc = new ArmReg(0);
    ArmReg r14_abt = new ArmReg(0);
    ArmReg r14_und = new ArmReg(0);

    usrRegisters[14] = r14;
    fiqRegisters[14] = r14_fiq;
    irqRegisters[14] = r14_irq;
    svcRegisters[14] = r14_svc;
    abtRegisters[14] = r14_abt;
    undRegisters[14] = r14_und;
    sysRegisters[14] = r14;

    ArmReg r15 = new ArmReg(0);

    usrRegisters[15] = r15;
    fiqRegisters[15] = r15;
    irqRegisters[15] = r15;
    svcRegisters[15] = r15;
    abtRegisters[15] = r15;
    undRegisters[15] = r15;
    sysRegisters[15] = r15;
    PC = r15;

    CPSR = new ArmReg(0);
    usrRegisters[16] = CPSR;
    fiqRegisters[16] = CPSR;
    irqRegisters[16] = CPSR;
    svcRegisters[16] = CPSR;
    abtRegisters[16] = CPSR;
    undRegisters[16] = CPSR;
    sysRegisters[16] = CPSR;

    SPSR_fiq = new ArmReg(0);
    SPSR_svc = new ArmReg(0);
    SPSR_abt = new ArmReg(0);
    SPSR_irq = new ArmReg(0);
    SPSR_und = new ArmReg(0);

    usrRegisters[17] = null;
    fiqRegisters[17] = SPSR_fiq;
    irqRegisters[17] = SPSR_irq;
    svcRegisters[17] = SPSR_svc;
    abtRegisters[17] = SPSR_abt;
    undRegisters[17] = SPSR_und;
    sysRegisters[17] = null;
  }

  public void connectToMemory(MemoryInterface memory)
  {
    this.memory = memory;
  }
  
  
  public void connectToTime(Time time)
  {
    this.time = time;
  }

  protected void setArmState()
  {
    CPSR.setOff(tFlagBit);
  }

  protected void setThumbState()
  {
    CPSR.setOn(tFlagBit);
  }

  protected void setMode(int modeBits)
  {
    if (modeBits == usrModeBits) currentRegisters = usrRegisters;
    else if (modeBits == fiqModeBits) currentRegisters = fiqRegisters;
    else if (modeBits == irqModeBits) currentRegisters = irqRegisters;
    else if (modeBits == svcModeBits) currentRegisters = svcRegisters;
    else if (modeBits == abtModeBits) currentRegisters = abtRegisters;
    else if (modeBits == undModeBits) currentRegisters = undRegisters;
    else if (modeBits == sysModeBits) currentRegisters = sysRegisters;
    else throw new Error("Mode non-defini.");

    CPSR.set((CPSR.get() & ~modeBitsMask) | modeBits);
  }

  protected int getCurrentMode()
  {
    return (CPSR.get() & modeBitsMask);
  }

  public boolean isInThumbState()
  {
    return CPSR.isBitSet(tFlagBit);
  }

  public boolean isInArmState()
  {
    return !CPSR.isBitSet(tFlagBit);
  }

  public int currentInstructionSize()
  {
    if (isInThumbState()) return 2;
    else return 4;
  }

  public ArmReg getRegister(int registerNumber)
  {
    return currentRegisters[registerNumber];
  }

  public ArmReg getRegister(int registerNumber, int modeBits)
  {
    ArmReg[] regs;

    if (modeBits == usrModeBits) regs = usrRegisters;
    else if (modeBits == fiqModeBits) regs = fiqRegisters;
    else if (modeBits == irqModeBits) regs = irqRegisters;
    else if (modeBits == svcModeBits) regs = svcRegisters;
    else if (modeBits == abtModeBits) regs = abtRegisters;
    else if (modeBits == undModeBits) regs = undRegisters;
    else if (modeBits == sysModeBits) regs = sysRegisters;
    else throw new RuntimeException("Mode non-defini.");

    return regs[registerNumber];
  }

  public ArmReg getSPSR()
  {
    return currentRegisters[17];
  }

  public ArmReg getSP()
  {
    return currentRegisters[13];
  }

  public ArmReg getLR()
  {
    return currentRegisters[14];
  }

  public void reset()
  {
    svcRegisters[14].set(PC);
    svcRegisters[17].set(PC);
    setMode(svcModeBits);
    CPSR.setOn(iFlagBit | fFlagBit);
    setArmState();
    //try {PC.set(memory.loadWord(resetVectorAddress);} catch {AbortException e) {}

    PC.set(0x08000000);      // pas ecrit dans la doc
    getSP().set(0x03007f00); // et ca non plus

    time.reset(); // CPUCycle = 0
  }

  protected void handleAbort(int vectorAddress)
  {
/*
    abtRegisters[14].set(PC);
    SPSR_abt.set(CPSR);
    setMode(abtModeBits);
    try{PC.set(memory.loadWord(vectorAddress));}
    catch(AbortException e) {} // Should not happen
    setArmState();
*/
  }

  protected void handleIRQ()
  {
    
  }

  public void step()
  {
    // Here Handle Reset exception
    // ... but for gfa emulator, nothing to do.

    int instructionTime;

    if (isInThumbState())
    {
      try
      {
        short halfWord = memory.loadHalfWord(PC.get());
        PC.add(currentInstructionSize());
        try {decodeThumbStateInstruction(halfWord);}
        catch(AbortException e)
	{
	  System.out.println("Data abort : " + e);
	  handleAbort(dataAbortVectorAddress);
	}
      }
      catch(AbortException e)
      {
	System.out.println("Prefetch Abort : " + e);
	handleAbort(prefetchAbortVectorAddress);
      }
      instructionTime = 2;
    }
    else
    {
      try
      {
        int word = memory.loadWord(PC.get());
        PC.add(currentInstructionSize());
        if (decodeArmStateCondition(word))
        {
          try {decodeArmStateInstruction(word);}
	  catch(AbortException e)
	  {
	    System.out.println("Data abort : " + e);
	    handleAbort(dataAbortVectorAddress);
	  }
        }
      }
      catch(AbortException e)
      {
	System.out.println("Prefetch Abort : " + e);
	handleAbort(prefetchAbortVectorAddress);
      }
      
      instructionTime = 4;
    }

    // Here handle FIQ
    // ...

    // Here handle IRQ
    handleIRQ();
    
    time.addTime(instructionTime);
  }

  public void breakpoint(int offset)
  {
    do
    {
      step();
    } while (PC.get() != offset);
  }

  public void stepOver()
  {
    breakpoint(PC.get() + currentInstructionSize());
  }

  private boolean stopPolitelyRequested = false;

  public void run()
  {
    while (!stopPolitelyRequested)
      step();

    stopPolitelyRequested = false;
  }

  public void stopPlease()
  {
    stopPolitelyRequested = true;
  }

  static final protected int armStateConditionBitsMask = 0xf0000000; // the bits concerning the condition in the instruction
  static final protected int armStateConditionEQBits   = 0x00000000; // equal
  static final protected int armStateConditionNEBits   = 0x10000000; // not equal
  static final protected int armStateConditionCSBits   = 0x20000000; // unsigned higher or same
  static final protected int armStateConditionCCBits   = 0x30000000; // unsigned lower
  static final protected int armStateConditionMIBits   = 0x40000000; // negative
  static final protected int armStateConditionPLBits   = 0x50000000; // positive or zero
  static final protected int armStateConditionVSBits   = 0x60000000; // overflow
  static final protected int armStateConditionVCBits   = 0x70000000; // no overflow
  static final protected int armStateConditionHIBits   = 0x80000000; // unsigned higher
  static final protected int armStateConditionLSBits   = 0x90000000; // unsigned lower or same
  static final protected int armStateConditionGEBits   = 0xa0000000; // greater or equal
  static final protected int armStateConditionLTBits   = 0xb0000000; // less than
  static final protected int armStateConditionGTBits   = 0xc0000000; // greater than
  static final protected int armStateConditionLEBits   = 0xd0000000; // less than or equal
  static final protected int armStateConditionALBits   = 0xe0000000; // always

  public boolean decodeArmStateCondition(int word)
  {
    //debug.println("decodeArmStateCondition");

    int conditionBits = word & armStateConditionBitsMask;
    boolean conditionIsTrue;

    switch (conditionBits)
    {
      case armStateConditionEQBits:
          conditionIsTrue = CPSR.isBitSet(zFlagBit);
        break;

      case armStateConditionNEBits:
          conditionIsTrue = !CPSR.isBitSet(zFlagBit);
        break;

      case armStateConditionCSBits:
          conditionIsTrue = CPSR.isBitSet(cFlagBit);
        break;

      case armStateConditionCCBits:
          conditionIsTrue = !CPSR.isBitSet(cFlagBit);
        break;

      case armStateConditionMIBits:
          conditionIsTrue = CPSR.isBitSet(nFlagBit);
        break;

      case armStateConditionPLBits:
          conditionIsTrue = !CPSR.isBitSet(nFlagBit);
        break;

      case armStateConditionVSBits:
          conditionIsTrue = CPSR.isBitSet(vFlagBit);
        break;

      case armStateConditionVCBits:
          conditionIsTrue = !CPSR.isBitSet(vFlagBit);
        break;

      case armStateConditionHIBits:
          conditionIsTrue = CPSR.isBitSet(cFlagBit) &&
                            !CPSR.isBitSet(zFlagBit);
        break;

      case armStateConditionLSBits:
          conditionIsTrue = !CPSR.isBitSet(cFlagBit) ||
                            CPSR.isBitSet(zFlagBit);
        break;

      case armStateConditionGEBits:
          conditionIsTrue = CPSR.isBitSet(nFlagBit) ==
                            CPSR.isBitSet(vFlagBit);
        break;

      case armStateConditionLTBits:
          conditionIsTrue = CPSR.isBitSet(nFlagBit) !=
                            CPSR.isBitSet(vFlagBit);
        break;

      case armStateConditionGTBits:
          conditionIsTrue = !CPSR.isBitSet(zFlagBit) &&
                            (CPSR.isBitSet(nFlagBit) ==
                             CPSR.isBitSet(vFlagBit));
        break;

      case armStateConditionLEBits:
          conditionIsTrue = CPSR.isBitSet(zFlagBit) ||
                            (CPSR.isBitSet(nFlagBit) !=
                             CPSR.isBitSet(vFlagBit));
        break;

      case armStateConditionALBits:
          conditionIsTrue = true;
        break;

      default:
        // Undocumented condition.
	// For now we interprete this like a NOP.
        // Pour l'instant on traite son instruction comme un NOP.
        conditionIsTrue = false;
    }

    return conditionIsTrue;
  }

  static final protected int armStateBXInstructionMask = 0x0ffffff0;
  static final protected int armStateBXInstructionBits = 0x012fff10;
  static final protected int armStateBXRnMask          = 0x0000000f;

  static final protected int armStateBInstructionMask = 0x0e000000;
  static final protected int armStateBInstructionBits = 0x0a000000;
  static final protected int armStateBLinkBit         = 0x01000000;
  static final protected int armStateBOffsetMask      = 0x00ffffff;

  static final protected int armStateDataProcessingInstructionMask = 0x0c000000;
  static final protected int armStateDataProcessingInstructionBits = 0x00000000;
  static final protected int armStateDataProcessingOpcodeMask      = 0x01e00000;
  static final protected int armStateDataProcessingANDBits         = 0x00000000;
  static final protected int armStateDataProcessingEORBits         = 0x00200000;
  static final protected int armStateDataProcessingSUBBits         = 0x00400000;
  static final protected int armStateDataProcessingRSBBits         = 0x00600000;
  static final protected int armStateDataProcessingADDBits         = 0x00800000;
  static final protected int armStateDataProcessingADCBits         = 0x00a00000;
  static final protected int armStateDataProcessingSBCBits         = 0x00c00000;
  static final protected int armStateDataProcessingRSCBits         = 0x00e00000;
  static final protected int armStateDataProcessingTSTBits         = 0x01000000;
  static final protected int armStateDataProcessingTEQBits         = 0x01200000;
  static final protected int armStateDataProcessingCMPBits         = 0x01400000;
  static final protected int armStateDataProcessingCMNBits         = 0x01600000;
  static final protected int armStateDataProcessingORRBits         = 0x01800000;
  static final protected int armStateDataProcessingMOVBits         = 0x01a00000;
  static final protected int armStateDataProcessingBICBits         = 0x01c00000;
  static final protected int armStateDataProcessingMVNBits         = 0x01e00000;
  static final protected int armStateDataProcessingImmediateBit    = 0x02000000;
  static final protected int armStateDataProcessingSetConditionBit = 0x00100000;
  static final protected int armStateDataProcessingOperand1Mask    = 0x000f0000;
  static final protected int armStateDataProcessingOperand2Mask    = 0x00000fff;
  static final protected int armStateDataProcessingDestinationMask = 0x0000f000;

  static final protected int armStateDataProcessingOp2RegisterMask   = 0x0000000f;
  static final protected int armStateDataProcessingShiftModeBit      = 0x00000010;
  static final protected int armStateDataProcessingShiftTypeMask     = 0x00000060;
  static final protected int armStateDataProcessingShiftAmountMask   = 0x00000f80;
  static final protected int armStateDataProcessingShiftRegisterMask = 0x00000f00;
  static final protected int armStateDataProcessingLogicalLeftBits   = 0x00000000;
  static final protected int armStateDataProcessingLogicalRightBits  = 0x00000020;
  static final protected int armStateDataProcessingArithmRightBits   = 0x00000040;
  static final protected int armStateDataProcessingRotateRightBits   = 0x00000060;

  static final protected int armStateDataProcessingOp2RotateMask    = 0x00000f00;
  static final protected int armStateDataProcessingOp2ImmediateMask = 0x000000ff;

  static final protected int armStateMRSInstructionMask  = 0x0fbf0fff;
  static final protected int armStateMRSInstructionBits  = 0x010f0000;
  static final protected int armStateMRSSourceBit        = 0x00400000;
  static final protected int armStateMRSDestinationMask  = 0x0000f000;

  static final protected int armStateMSR1InstructionMask = 0x0fbffff0;
  static final protected int armStateMSR1InstructionBits = 0x0129f000;
  static final protected int armStateMSR1SourceMask      = 0x0000000f;
  static final protected int armStateMSR1DestinationBit  = 0x00400000;
  static final protected int armStateMSR1FlagsOnly       = 0xf0000000;

  static final protected int armStateMSR2InstructionMask = 0x0dbff000;
  static final protected int armStateMSR2InstructionBits = 0x0128f000;
  static final protected int armStateMSR2SourceMask      = 0x0000000f;
  static final protected int armStateMSR2DestinationBit  = 0x00400000;
  static final protected int armStateMSR2ImmediateBit    = 0x02000000;
  static final protected int armStateMSR2ImmValueMask    = 0x000000ff;
  static final protected int armStateMSR2ImmRotateMask   = 0x00000f00;
  static final protected int armStateMSR2FlagsOnly       = 0xf0000000;

  static final protected int armStateMULInstructionMask = 0x0fc000f0;
  static final protected int armStateMULInstructionBits = 0x00000090;
  static final protected int armStateMULAccumulateBit   = 0x00200000;
  static final protected int armStateMULSetConditionBit = 0x00100000;
  static final protected int armStateMULRdMask          = 0x000f0000;
  static final protected int armStateMULRnMask          = 0x0000f000;
  static final protected int armStateMULRsMask          = 0x00000f00;
  static final protected int armStateMULRmMask          = 0x0000000f;

  static final protected int armStateMULLInstructionMask = 0x0f8000f0;
  static final protected int armStateMULLInstructionBits = 0x00800090;
  static final protected int armStateMULLUnsignedBit     = 0x00400000;
  static final protected int armStateMULLAccumulateBit   = 0x00200000;
  static final protected int armStateMULLSetConditionBit = 0x00100000;
  static final protected int armStateMULLRdHiMask        = 0x000f0000;
  static final protected int armStateMULLRdLoMask        = 0x0000f000;
  static final protected int armStateMULLRsMask          = 0x00000f00;
  static final protected int armStateMULLRmMask          = 0x0000000f;

  static final protected int armStateLDRSTRInstructionMask  = 0x0c000000;
  static final protected int armStateLDRSTRInstructionBits  = 0x04000000;
  static final protected int armStateLDRSTRImmediateBit     = 0x02000000;
  static final protected int armStateLDRSTRPreIndexingBit   = 0x01000000;
  static final protected int armStateLDRSTRUpDownBit        = 0x00800000;
  static final protected int armStateLDRSTRByteWordBit      = 0x00400000;
  static final protected int armStateLDRSTRWriteBackBit     = 0x00200000;
  static final protected int armStateLDRSTRLoadStoreBit     = 0x00100000;
  static final protected int armStateLDRSTRRnMask           = 0x000f0000;
  static final protected int armStateLDRSTRRdMask           = 0x0000f000;
  static final protected int armStateLDRSTRImmediateMask    = 0x00000fff;
  static final protected int armStateLDRSTRRmMask           = 0x0000000f;
  static final protected int armStateLDRSTRShiftTypeMask    = 0x00000060;
  static final protected int armStateLDRSTRShiftAmountMask  = 0x00000f80;
  static final protected int armStateLDRSTRLogicalLeftBits  = 0x00000000;
  static final protected int armStateLDRSTRLogicalRightBits = 0x00000020;
  static final protected int armStateLDRSTRArithmRightBits  = 0x00000040;
  static final protected int armStateLDRSTRRotateRightBits  = 0x00000060;

  static final protected int armStateHSDTROInstructionMask = 0x0e400f90;
  static final protected int armStateHSDTROInstructionBits = 0x00000090;
  static final protected int armStateHSDTROPreIndexingBit  = 0x01000000;
  static final protected int armStateHSDTROUpDownBit       = 0x00800000;
  static final protected int armStateHSDTROWriteBackBit    = 0x00200000;
  static final protected int armStateHSDTROLoadStoreBit    = 0x00100000;
  static final protected int armStateHSDTRORnMask          = 0x000f0000;
  static final protected int armStateHSDTRORdMask          = 0x0000f000;
  static final protected int armStateHSDTROSHMask          = 0x00000060;
  static final protected int armStateHSDTROSWPInstrBits    = 0x00000000;
  static final protected int armStateHSDTROUnsignHalfBits  = 0x00000020;
  static final protected int armStateHSDTROSignedByteBits  = 0x00000040;
  static final protected int armStateHSDTROSignedHalfBits  = 0x00000060;
  static final protected int armStateHSDTRORmMask          = 0x0000000f;

  static final protected int armStateHSDTIOInstructionMask = 0x0e400090;
  static final protected int armStateHSDTIOInstructionBits = 0x00400090;
  static final protected int armStateHSDTIOPreIndexingBit  = 0x01000000;
  static final protected int armStateHSDTIOUpDownBit       = 0x00800000;
  static final protected int armStateHSDTIOWriteBackBit    = 0x00200000;
  static final protected int armStateHSDTIOLoadStoreBit    = 0x00100000;
  static final protected int armStateHSDTIORnMask          = 0x000f0000;
  static final protected int armStateHSDTIORdMask          = 0x0000f000;
  static final protected int armStateHSDTIOHiOffsetMask    = 0x00000f00;
  static final protected int armStateHSDTIOSHMask          = 0x00000060;
  static final protected int armStateHSDTIOSWPInstrBits    = 0x00000000;
  static final protected int armStateHSDTIOUnsignHalfBits  = 0x00000020;
  static final protected int armStateHSDTIOSignedByteBits  = 0x00000040;
  static final protected int armStateHSDTIOSignedHalfBits  = 0x00000060;
  static final protected int armStateHSDTIOLoOffsetMask    = 0x0000000f;

  static final protected int armStateLDMSTMInstructionMask = 0x0e000000;
  static final protected int armStateLDMSTMInstructionBits = 0x08000000;
  static final protected int armStateLDMSTMPreIndexingBit  = 0x01000000;
  static final protected int armStateLDMSTMUpDownBit       = 0x00800000;
  static final protected int armStateLDMSTMPSRBit          = 0x00400000;
  static final protected int armStateLDMSTMWriteBackBit    = 0x00200000;
  static final protected int armStateLDMSTMLoadStoreBit    = 0x00100000;
  static final protected int armStateLDMSTMRnMask          = 0x000f0000;
  static final protected int armStateLDMSTMR15Bit          = 0x00008000;

  static final protected int armStateSWPInstructionMask = 0x0fb00ff0;
  static final protected int armStateSWPInstructionBits = 0x01000090;
  static final protected int armStateSWPByteWordBit     = 0x00400000;
  static final protected int armStateSWPRnMask          = 0x000f0000;
  static final protected int armStateSWPRdMask          = 0x0000f000;
  static final protected int armStateSWPRmMask          = 0x0000000f;

  static final protected int armStateSWIInstructionMask = 0x0f000000;
  static final protected int armStateSWIInstructionBits = 0x0f000000;

  static final protected int armStateUndefInstructionMask = 0x0e000010;
  static final protected int armStateUndefInstructionBits = 0x06000010;

  protected void decodeArmStateInstruction(int word)
    throws AbortException
  {
    //debug.println("decodeArmStateInstruction word = 0x" + Hex.toString(word));

    if ((word & armStateBXInstructionMask) == armStateBXInstructionBits)
    {
      int registerNumber = word & armStateBXRnMask;
      int value = getRegister(registerNumber).get();
      PC.set(value & 0xfffffffe);
      if ((value & 0x00000001) == 0) setArmState();
      else setThumbState();
    }

    else if ((word & armStateBInstructionMask) == armStateBInstructionBits)
    {
      //debug.println("B instruction");

      int offset = ((word & armStateBOffsetMask) << 8) >> 6;
      if ((word & armStateBLinkBit) != 0)
        currentRegisters[14].set(PC.get() & 0xfffffffc);
      PC.add(offset + 4);
    }

    else if ((word & armStateMRSInstructionMask) == armStateMRSInstructionBits)
    {
      //debug.println("MRS instruction");
      try
      {
        ArmReg srcReg = (((word & armStateMRSSourceBit) == 0) ? CPSR : getSPSR());
        ArmReg dstReg = getRegister((word & armStateMRSDestinationMask) >>> 12);
        dstReg.set(srcReg);
      }
      catch (NullPointerException e) {/* $$$ : Usage de l'instruction en mode usr :-( */}
    }

    else if ((word & armStateMSR1InstructionMask) == armStateMSR1InstructionBits)
    {
      //debug.println("MSR1 instruction");
      try
      {
        ArmReg srcReg = getRegister(word & armStateMSR1SourceMask);
        ArmReg dstReg = (((word & armStateMSR1DestinationBit) == 0) ? CPSR : getSPSR());
        if (getCurrentMode() == usrModeBits)
          dstReg.set((dstReg.get() & ~armStateMSR1FlagsOnly) | (srcReg.get() & armStateMSR1FlagsOnly));
        else
        {
          dstReg.set(srcReg);
          setMode(getCurrentMode()); // update the mode in case CPRS[4:0] is changed
        }
      }
      catch (NullPointerException e) {/* $$$ : Usage de SPSR en mode usr :-( */}
    }

    else if ((word & armStateMSR2InstructionMask) == armStateMSR2InstructionBits)
    {
      //debug.println("MSR2 instruction");
      try
      {
        ArmReg dstReg = (((word & armStateMSR2DestinationBit) == 0) ? CPSR : getSPSR());
        int value;
        if ((word & armStateMSR2ImmediateBit) == 0)
          value = getRegister(word & armStateMSR2SourceMask).get();
        else
        {
          int immValue = word & armStateMSR2ImmValueMask;
          int immRotate = (word & armStateMSR2ImmRotateMask) >>> 7;
          value = (immValue >> immRotate) | (immValue << (32 - immRotate));
        }
        dstReg.set((dstReg.get() & ~armStateMSR2FlagsOnly) | (value & armStateMSR2FlagsOnly));
      }
      catch (NullPointerException e) {/* Usage de SPSR en mode usr :-( */}
    }

    else if ((word & armStateMULInstructionMask) == armStateMULInstructionBits)
    {
      //debug.println("MUL instruction");
      ArmReg rd = getRegister((word & armStateMULRdMask) >>> 16);
      ArmReg rn = getRegister((word & armStateMULRnMask) >>> 12);
      ArmReg rs = getRegister((word & armStateMULRsMask) >>> 8);
      ArmReg rm = getRegister(word & armStateMULRmMask);
      int result = rm.get() * rs.get();
      //debug.println(rm.toString() + " * " + rs + " = " + Integer.toHexString(result));

      if ((word & armStateMULAccumulateBit) != 0)
        result += rn.get();

      rd.set(result);

      if ((word & armStateMULSetConditionBit) != 0)
      {
        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
        //CPSR.setBit(cFlagBit, meaninglessCondition);
      }
    }

    else if ((word & armStateMULLInstructionMask) == armStateMULLInstructionBits)
    {
      //debug.println("MULL instruction");
      ArmReg rdHi = getRegister((word & armStateMULLRdHiMask) >>> 16);
      ArmReg rdLo = getRegister((word & armStateMULLRdLoMask) >>> 12);
      ArmReg rs = getRegister((word & armStateMULLRsMask) >>> 8);
      ArmReg rm = getRegister(word & armStateMULLRmMask);
      long longResult;

      if ((word & armStateMULLUnsignedBit) != 0)
      {
        longResult = (long) rm.get() * (long) rs.get();

        if ((word & armStateMULLAccumulateBit) != 0)
          longResult += ((long) rdHi.get() << 32) | (0xffffffffL & (long) rdLo.get());
      }
      else // multiplication unsigned 64 bits ... :-(
      {
        BigInteger operand1 = new BigInteger(Long.toString(0xffffffffL & (long) rm.get()));
        BigInteger operand2 = new BigInteger(Long.toString(0xffffffffL & (long) rs.get()));
        BigInteger bigResult = operand1.multiply(operand2);

        if ((word & armStateMULLAccumulateBit) != 0)
        {
          BigInteger hi = new BigInteger(Long.toString(0xffffffffL & (long) rdHi.get()));
          BigInteger lo = new BigInteger(Long.toString(0xffffffffL & (long) rdLo.get()));
          bigResult = bigResult.add(hi.shiftLeft(32).or(lo));
        }

        longResult = bigResult.longValue();
      }

      rdLo.set((int) longResult);
      rdHi.set((int) (longResult >> 32));

      if ((word & armStateMULLSetConditionBit) != 0)
      {
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));
        //CPSR.setBit(cFlagBit, meaninglessCondition);
        //CPSR.setBit(vFlagBit, meaninglessCondition);
      }
    }

    else if ((word & armStateHSDTROInstructionMask) == armStateHSDTROInstructionBits)
    {
      int offset = getRegister(word & armStateHSDTRORmMask).get();
      ArmReg baseRegister = getRegister((word & armStateHSDTRORnMask) >>> 16);
      ArmReg srcDstRegister = getRegister((word & armStateHSDTRORdMask) >>> 12);

      int newAdress = baseRegister.get();
      if (baseRegister == PC) newAdress += 4;

      if ((word & armStateHSDTROUpDownBit) == 0)
        offset -= offset;

      if ((word & armStateHSDTROPreIndexingBit) != 0) // PreIndex
        newAdress += offset;

      if ((word & armStateHSDTROLoadStoreBit) == 0) // Store
      {
        int SHBits = word & armStateHSDTROSHMask;
        if (SHBits == armStateHSDTROUnsignHalfBits)
        {
          int valueToStore = srcDstRegister.get();
          if (srcDstRegister == PC) valueToStore += 8;
          memory.storeHalfWord(newAdress, (short) valueToStore);
        }
        else if (SHBits == armStateHSDTROSignedByteBits)
          ;// unspecified
        else if (SHBits == armStateHSDTROSignedHalfBits)
          ;// unspecified
      }
      else                                          // Load
      {
        int SHBits = word & armStateHSDTROSHMask;
        if (SHBits == armStateHSDTROUnsignHalfBits)
           // $$$ : C'est pas dit, mais on suppose que la partie haute est remplie avec des zeros.
           srcDstRegister.set(0x0000ffff & memory.loadHalfWord(newAdress));
        else if (SHBits == armStateHSDTROSignedByteBits)
           srcDstRegister.set(memory.loadByte(newAdress));
        else if (SHBits == armStateHSDTROSignedHalfBits)
           srcDstRegister.set(memory.loadHalfWord(newAdress));
      }

      if ((word & armStateHSDTROPreIndexingBit) == 0) // PostIndex
        newAdress += offset;

      if (((word & armStateHSDTROWriteBackBit) != 0) ||
          ((word & armStateHSDTROPreIndexingBit) == 0))
        baseRegister.set(newAdress);
    }

    else if ((word & armStateHSDTIOInstructionMask) == armStateHSDTIOInstructionBits)
    {
      int offset = ((word & armStateHSDTIOHiOffsetMask) >>> 4) | (word & armStateHSDTIOLoOffsetMask);
      ArmReg baseRegister = getRegister((word & armStateHSDTIORnMask) >>> 16);
      ArmReg srcDstRegister = getRegister((word & armStateHSDTIORdMask) >>> 12);

      int newAdress = baseRegister.get();
      if (baseRegister == PC) newAdress += 4;

      if ((word & armStateHSDTIOUpDownBit) == 0)
        offset -= offset;

      if ((word & armStateHSDTIOPreIndexingBit) != 0) // PreIndex
        newAdress += offset;

      if ((word & armStateHSDTIOLoadStoreBit) == 0) // Store
      {
        int SHBits = word & armStateHSDTIOSHMask;
        if (SHBits == armStateHSDTIOUnsignHalfBits)
        {
          int valueToStore = srcDstRegister.get();
          if (srcDstRegister == PC) valueToStore += 8;
          memory.storeHalfWord(newAdress, (short) valueToStore);
        }
        else if (SHBits == armStateHSDTIOSignedByteBits)
          ;// non specifie
        else if (SHBits == armStateHSDTIOSignedHalfBits)
          ;// non specifie
      }
      else                                          // Load
      {
        int SHBits = word & armStateHSDTIOSHMask;
        if (SHBits == armStateHSDTIOUnsignHalfBits)
           // $$$ : C'est pas dit, mais on suppose que la partie haute est remplie avec des zeros.
           srcDstRegister.set(0x0000ffff & memory.loadHalfWord(newAdress));
        else if (SHBits == armStateHSDTIOSignedByteBits)
           srcDstRegister.set(memory.loadByte(newAdress));
        else if (SHBits == armStateHSDTIOSignedHalfBits)
           srcDstRegister.set(memory.loadHalfWord(newAdress));
      }

      if ((word & armStateHSDTIOPreIndexingBit) == 0) // PostIndex
        newAdress += offset;

      if (((word & armStateHSDTIOWriteBackBit) != 0) ||
          ((word & armStateHSDTIOPreIndexingBit) == 0))
        baseRegister.set(newAdress);
    }

    else if ((word & armStateSWPInstructionMask) == armStateSWPInstructionBits)
    {
      ArmReg baseRegister = getRegister((word & armStateSWPRnMask) >>> 16);
      ArmReg destinationRegister = getRegister((word & armStateSWPRdMask) >>> 12);
      ArmReg sourceRegister = getRegister(word & armStateSWPRmMask);
      int swapAdress = baseRegister.get();

      if ((word & armStateSWPByteWordBit) != 0) // Byte
        destinationRegister.set(0xff & memory.swapByte(swapAdress, (byte) sourceRegister.get()));
      else // Word
        destinationRegister.set(memory.swapWord(swapAdress, sourceRegister.get()));
    }

    else if ((word & armStateDataProcessingInstructionMask) == armStateDataProcessingInstructionBits)
    {
      //debug.println("Data Processing instruction");

      ArmReg tmpCPSR = new ArmReg(CPSR);
      ArmReg regOp1 = getRegister((word & armStateDataProcessingOperand1Mask) >>> 16);

      int operand1 = regOp1.get();
      if (regOp1 == PC) operand1 += 4;

      int operand2;
      ArmReg destinationRegister = getRegister((word & armStateDataProcessingDestinationMask) >>> 12);

      if ((word & armStateDataProcessingImmediateBit) == 0)
      {
        // Rm register
        ArmReg regOp2 = getRegister(word & armStateDataProcessingOp2RegisterMask);
        operand2 = regOp2.get();
        if (regOp2 == PC) operand2 += 4;

        int shiftType = word & armStateDataProcessingShiftTypeMask;
        int shiftAmount;

        if ((word & armStateDataProcessingShiftModeBit) == 0)
        {
          shiftAmount = (word & armStateDataProcessingShiftAmountMask) >>> 7;

          if (shiftAmount == 0)
          {
            if (shiftType == armStateDataProcessingLogicalLeftBits)
            {
              // Do nothing : the old cFlagBit must be conserved.
              // Ne fait rien : l'ancien cFlagBit doit etre conserve.
            }
            else if (shiftType == armStateDataProcessingLogicalRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
              operand2 = 0;
            }
            else if (shiftType == armStateDataProcessingArithmRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
              // fill all with the bit 31 of operand2.
              operand2 >>= 16; operand2 >>= 16;
            }
            else if (shiftType == armStateDataProcessingRotateRightBits)
            {
              boolean isCFlagBit = tmpCPSR.isBitSet(cFlagBit);
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x01) != 0));
              operand2 >>>= 1;
              if (isCFlagBit) operand2 |= 0x80000000;
            }
          }
          else
          {
            if (shiftType == armStateDataProcessingLogicalLeftBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (32 - shiftAmount))) != 0));
              operand2 <<= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingLogicalRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
              operand2 >>>= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingArithmRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
              operand2 >>= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingRotateRightBits)
            {
              operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
            }
          }
        }
        else // Register specified shift amount
        {
          ArmReg regToShift = getRegister((word & armStateDataProcessingShiftRegisterMask) >>> 8);
          shiftAmount = regToShift.get();
          if (regToShift == PC) shiftAmount += 8;

          if (shiftAmount == 0)
            ; // Ne rien faire : conservation de l'ancien cFlag;
          else if (shiftAmount < 32)
          {
            if (shiftType == armStateDataProcessingLogicalLeftBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (32 - shiftAmount))) != 0));
              operand2 <<= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingLogicalRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
              operand2 >>>= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingArithmRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & (1 << (shiftAmount - 1))) != 0));
              operand2 >>= shiftAmount;
            }
            else if (shiftType == armStateDataProcessingRotateRightBits)
            {
              operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
            }
          }
          else if (shiftAmount == 32)
          {
            if (shiftType == armStateDataProcessingLogicalLeftBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x01) != 0));
              operand2 = 0;
            }
            else if (shiftType == armStateDataProcessingLogicalRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
              operand2 = 0;
            }
            else if (shiftType == armStateDataProcessingArithmRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
              operand2 >>= 16;
              operand2 >>= 16;
            }
            else if (shiftType == armStateDataProcessingRotateRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
            }
          }
          else if (shiftAmount > 32)
          {
            if (shiftType == armStateDataProcessingLogicalLeftBits)
            {
              tmpCPSR.setOff(cFlagBit);
              operand2 = 0;
            }
            else if (shiftType == armStateDataProcessingLogicalRightBits)
            {
              tmpCPSR.setOff(cFlagBit);
              operand2 = 0;
            }
            else if (shiftType == armStateDataProcessingArithmRightBits)
            {
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
              operand2 >>= 16;
              operand2 >>= 16;
            }
            else if (shiftType == armStateDataProcessingRotateRightBits)
            {
              shiftAmount = ((shiftAmount - 1) & 0x1f) + 1; // put shiftAmount in the range [1..32]
              operand2 = (operand2 >>> shiftAmount) | (operand2 << (32 - shiftAmount));
              tmpCPSR.setBit(cFlagBit, ((operand2 & 0x80000000) != 0));
            }
          }
        }
      }
      else
      {
        int immediateValue = word & armStateDataProcessingOp2ImmediateMask;
        int twiceRotateValue = ((word & armStateDataProcessingOp2RotateMask) >>> 8) * 2;
        operand2 = (immediateValue >>> twiceRotateValue) | (immediateValue << (32 - twiceRotateValue));
      }

      int opcodeBits = word & armStateDataProcessingOpcodeMask;

      if (opcodeBits == armStateDataProcessingANDBits)
      {
        int result = operand1 & operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingEORBits)
      {
        int result = operand1 ^ operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingSUBBits)
      {
        int result = operand1 - operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForSub(operand1, operand2, result);
      }
      else if (opcodeBits == armStateDataProcessingRSBBits)
      {
        int result = operand2 - operand1;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForSub(operand2, operand1, result);
      }
      else if (opcodeBits == armStateDataProcessingADDBits)
      {
        int result = operand1 + operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForAdd(operand1, operand2, result);
      }
      else if (opcodeBits == armStateDataProcessingADCBits)
      {
        int cFlagValue = (CPSR.isBitSet(cFlagBit) ? 1 : 0);
        int result = operand1 + operand2 + cFlagValue;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForAdd(operand1, operand2, result);  // $$$ faire les tests pour savoir ou mettre la retenue.
      }
      else if (opcodeBits == armStateDataProcessingSBCBits)
      {
        int notCFlagValue = (CPSR.isBitSet(cFlagBit) ? 0 : 1);
        int result = operand1 - (operand2 + notCFlagValue);
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForSub(operand1, operand2, result);  // $$$ faire les tests pour savoir ou mettre la retenue.
      }
      else if (opcodeBits == armStateDataProcessingRSCBits)
      {
        int notCFlagValue = (CPSR.isBitSet(cFlagBit) ? 0 : 1);
        int result = operand2 - (operand1 + notCFlagValue);
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForSub(operand2, operand1, result);  // $$$ faire les tests pour savoir ou mettre la retenue.
      }
      else if (opcodeBits == armStateDataProcessingTSTBits)
      {
	//System.out.println("TST " + Hex.toString(operand1) + ", " + Hex.toString(operand2));
        int result = operand1 & operand2;
	//System.out.println("result = " + Hex.toString(result));
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingTEQBits)
      {
        int result = operand1 ^ operand2;
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingCMPBits)
      {
        int result = operand1 - operand2;
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForSub(operand1, operand2, result);
      }
      else if (opcodeBits == armStateDataProcessingCMNBits)
      {
        int result = operand1 + operand2;
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
        tmpCPSR.setCVFlagsForAdd(operand1, operand2, result);
      }
      else if (opcodeBits == armStateDataProcessingORRBits)
      {
        int result = operand1 | operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingMOVBits)
      {
        int result = operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingBICBits)
      {
        int result = operand1 & ~operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }
      else if (opcodeBits == armStateDataProcessingMVNBits)
      {
        int result = ~operand2;
        destinationRegister.set(result);
        tmpCPSR.setBit(zFlagBit, (result == 0));
        tmpCPSR.setBit(nFlagBit, (result < 0));
      }

      if ((word & armStateDataProcessingSetConditionBit) != 0)
      {
        if (destinationRegister == PC)
        {
          CPSR.set(getSPSR());
          setMode(getCurrentMode()); // Swap les banques de registre si besoin.
        }
        else
          CPSR.set(tmpCPSR);
      }
    }

    else if ((word & armStateUndefInstructionMask) == armStateUndefInstructionBits)
    {
      undRegisters[14].set(PC);
      SPSR_und.set(CPSR);
      setMode(undModeBits);
      PC.set(memory.loadWord(undefinedInstructionVectorAddress));
      setArmState();
    }

    else if ((word & armStateLDRSTRInstructionMask) == armStateLDRSTRInstructionBits)
    {
      ArmReg baseRegister = getRegister((word & armStateLDRSTRRnMask) >>> 16);
      ArmReg srcDstRegister = getRegister((word & armStateLDRSTRRdMask) >>> 12);

      int offset = 0;
      if ((word & armStateLDRSTRImmediateBit) == 0)
        offset = word & armStateLDRSTRImmediateMask;
      else
      {
        int offsetValue = getRegister(word & armStateLDRSTRRmMask).get();
        int shiftAmount = (word & armStateLDRSTRShiftAmountMask) >>> 7;
        int shiftType = word & armStateLDRSTRShiftTypeMask;
        if (shiftType == armStateLDRSTRLogicalLeftBits)       offset = offsetValue << shiftAmount;
        else if (shiftType == armStateLDRSTRLogicalRightBits) offset = offsetValue >>> shiftAmount;
        else if (shiftType == armStateLDRSTRArithmRightBits)  offset = offsetValue >> shiftAmount;
        else if (shiftType == armStateLDRSTRRotateRightBits)  offset = (offsetValue >>> shiftAmount) | (offsetValue << (32 - shiftAmount));
      }

      int newAdress = baseRegister.get();
      if (baseRegister == PC) newAdress += 4;

      if ((word & armStateLDRSTRUpDownBit) == 0)
        offset = -offset;

      if ((word & armStateLDRSTRPreIndexingBit) != 0) // PreIndex
        newAdress += offset;

/*
      int oldMode = getCurrentMode();
      if (((word & armStateLDRSTRWriteBackBit) != 0) && // if (WriteBack and
          ((word & armStateLDRSTRPreIndexingBit) == 0)) //     PostIndex)
        setMode(usrMode);
      ...
      setMode(oldMode);
*/
      if (((word & armStateLDRSTRWriteBackBit) != 0) && // if (WriteBack and
          ((word & armStateLDRSTRPreIndexingBit) == 0)) //     PostIndex)
        System.out.println("Warning : A non-emulated instruction for an OS has been used !");

      if ((word & armStateLDRSTRLoadStoreBit) == 0)  // Store
      {
        int valueToStore = srcDstRegister.get();
        if (srcDstRegister == PC) valueToStore += 8;

        if ((word & armStateLDRSTRByteWordBit) == 0) // Word
        {
          int wordAlignedAdress = newAdress & 0xfffffffc;
          memory.storeWord(wordAlignedAdress, valueToStore);
        }
        else                                         // Byte
          memory.storeByte(newAdress, (byte) valueToStore);
      }
      else                                           // Load
      {
        if ((word & armStateLDRSTRByteWordBit) == 0) // Word
        {
          int wordAlignedAdress = newAdress & 0xfffffffc;
          int rightRotate = (newAdress & 0x3) << 3;
          int value = memory.loadWord(wordAlignedAdress);
          srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
        }
        else                                         // Byte
          srcDstRegister.set(0xff & memory.loadByte(newAdress));
      }

      if ((word & armStateLDRSTRPreIndexingBit) == 0) // PostIndex
        newAdress += offset;

      if (((word & armStateLDRSTRWriteBackBit) != 0) || // if (WriteBack or
          ((word & armStateLDRSTRPreIndexingBit) == 0)) //     PostIndex)
        baseRegister.set(newAdress);
    }

    else if ((word & armStateLDMSTMInstructionMask) == armStateLDMSTMInstructionBits)
    {
      int baseRegisterNumber = (word & armStateLDMSTMRnMask) >>> 16;
      ArmReg baseRegister = getRegister(baseRegisterNumber);
      int stackAdress = baseRegister.get() & 0xfffffffc;

      int nbRegistersToSaveOrToLoad = 0;  // On compte le nombre de registres a sauver/charger (we count the number of registers to save/load).
      for (int i = 0; i <= 15; i++)
        if ((word & (1 << i)) != 0)
          nbRegistersToSaveOrToLoad++;

      int newBaseRegisterValue;
      if ((word & armStateLDMSTMUpDownBit) != 0) // Up
      {
        newBaseRegisterValue = stackAdress + nbRegistersToSaveOrToLoad * 4;
        if ((word & armStateLDMSTMPreIndexingBit) != 0) // Pre-increment
          stackAdress += 4;
      }
      else                    // Down
      {
        newBaseRegisterValue = stackAdress - nbRegistersToSaveOrToLoad * 4;
        stackAdress = newBaseRegisterValue;
        if ((word & armStateLDMSTMPreIndexingBit) == 0) // Post-decrement
          stackAdress += 4;
      }

      if ((word & armStateLDMSTMLoadStoreBit) == 0) // Store
      {
        ArmReg[] savedCurrentRegisters = currentRegisters;
        if ((word & armStateLDMSTMPSRBit) != 0)
          currentRegisters = usrRegisters;

        int i = 0;
        for (; i < 15; i++) // Empile le premier registre : debut du deuxieme cycle
          if ((word & (1 << i)) != 0)
          {
            memory.storeWord(stackAdress, getRegister(i).get());
            stackAdress += 4;
            i++;
            break;
          }

        if ((word & armStateLDMSTMWriteBackBit) != 0) // fin du deuxieme cycle
          baseRegister.set((baseRegister.get() & 0x3) | newBaseRegisterValue); // update

        for (; i < 15; i++) // Empile les autres registres jusqu'a 14
          if ((word & (1 << i)) != 0)
          {
            memory.storeWord(stackAdress, getRegister(i).get());
            stackAdress += 4;
          }

        if ((word & armStateLDMSTMR15Bit) != 0) // Le cas de R15 + 12.
        {
          memory.storeWord(stackAdress, PC.get() + 8);
          stackAdress += 4;
        }

        currentRegisters = savedCurrentRegisters;
      }
      else                            // Load
      {
        if ((word & armStateLDMSTMWriteBackBit) != 0) // Write Back
          baseRegister.set((baseRegister.get() & 0x3) | newBaseRegisterValue);

        ArmReg[] savedCurrentRegisters = currentRegisters;
        if (((word & armStateLDMSTMPSRBit) != 0) &&
            ((word & armStateLDMSTMR15Bit) == 0))
          currentRegisters = usrRegisters;

        for (int i = 0; i < 15; i++) // Depile les registres de 0 a 14
          if ((word & (1 << i)) != 0)
          {
            getRegister(i).set(memory.loadWord(stackAdress));
            stackAdress += 4;
          }

        currentRegisters = savedCurrentRegisters;

        if ((word & armStateLDMSTMR15Bit) != 0)
        {
          PC.set(memory.loadWord(stackAdress));
          if ((word & armStateLDMSTMPSRBit) != 0)
            CPSR.set(getSPSR());
          stackAdress += 4;
        }
      }
    }

    else if ((word & armStateSWIInstructionMask) == armStateSWIInstructionBits)
    {
      svcRegisters[14].set(PC);
      SPSR_svc.set(CPSR);
      setMode(svcModeBits);
      PC.set(softwareInterrupVectorAddress);
      setArmState();
    }
  }

  static final protected int thumbStateF1InstructionMask = 0xe000;
  static final protected int thumbStateF1InstructionBits = 0x0000;
  static final protected int thumbStateF1RsMask          = 0x0038;
  static final protected int thumbStateF1RdMask          = 0x0007;
  static final protected int thumbStateF1OpMask          = 0x1800;
  static final protected int thumbStateF1OffsetMask      = 0x07c0;
  static final protected int thumbStateF1LslBits         = 0x0000;
  static final protected int thumbStateF1LsrBits         = 0x0800;
  static final protected int thumbStateF1AsrBits         = 0x1000;

  static final protected int thumbStateF2InstructionMask = 0xf800;
  static final protected int thumbStateF2InstructionBits = 0x1800;
  static final protected int thumbStateF2RsMask          = 0x0038;
  static final protected int thumbStateF2RdMask          = 0x0007;
  static final protected int thumbStateF2RnMask          = 0x01c0;
  static final protected int thumbStateF2ImmediateBit    = 0x0400;
  static final protected int thumbStateF2OpBit           = 0x0200;

  static final protected int thumbStateF3InstructionMask = 0xe000;
  static final protected int thumbStateF3InstructionBits = 0x2000;
  static final protected int thumbStateF3OffsetMask      = 0x00ff;
  static final protected int thumbStateF3RdMask          = 0x0700;
  static final protected int thumbStateF3OpMask          = 0x1800;
  static final protected int thumbStateF3MovBits         = 0x0000;
  static final protected int thumbStateF3CmpBits         = 0x0800;
  static final protected int thumbStateF3AddBits         = 0x1000;
  static final protected int thumbStateF3SubBits         = 0x1800;

  static final protected int thumbStateF4InstructionMask = 0xfc00;
  static final protected int thumbStateF4InstructionBits = 0x4000;
  static final protected int thumbStateF4OpMask          = 0x03c0;
  static final protected int thumbStateF4RsMask          = 0x0038;
  static final protected int thumbStateF4RdMask          = 0x0007;
  static final protected int thumbStateF4AndBits         = 0x0000;
  static final protected int thumbStateF4EorBits         = 0x0040;
  static final protected int thumbStateF4LslBits         = 0x0080;
  static final protected int thumbStateF4LsrBits         = 0x00c0;
  static final protected int thumbStateF4AsrBits         = 0x0100;
  static final protected int thumbStateF4AdcBits         = 0x0140;
  static final protected int thumbStateF4SbcBits         = 0x0180;
  static final protected int thumbStateF4RorBits         = 0x01c0;
  static final protected int thumbStateF4TstBits         = 0x0200;
  static final protected int thumbStateF4NegBits         = 0x0240;
  static final protected int thumbStateF4CmpBits         = 0x0280;
  static final protected int thumbStateF4CmnBits         = 0x02c0;
  static final protected int thumbStateF4OrrBits         = 0x0300;
  static final protected int thumbStateF4MulBits         = 0x0340;
  static final protected int thumbStateF4BicBits         = 0x0380;
  static final protected int thumbStateF4MvnBits         = 0x03c0;

  static final protected int thumbStateF5InstructionMask = 0xfc00;
  static final protected int thumbStateF5InstructionBits = 0x4400;
  static final protected int thumbStateF5OpMask          = 0x0300;
  static final protected int thumbStateF5AddBits         = 0x0000;
  static final protected int thumbStateF5CmpBits         = 0x0100;
  static final protected int thumbStateF5MovBits         = 0x0200;
  static final protected int thumbStateF5BxBits          = 0x0300;
  static final protected int thumbStateF5H1Bit           = 0x0080;
  static final protected int thumbStateF5H2Bit           = 0x0040;
  static final protected int thumbStateF5RsMask          = 0x0038;
  static final protected int thumbStateF5RdMask          = 0x0007;

  static final protected int thumbStateF6InstructionMask = 0xf800;
  static final protected int thumbStateF6InstructionBits = 0x4800;
  static final protected int thumbStateF6RdMask          = 0x0700;
  static final protected int thumbStateF6ImmMask         = 0x00ff;

  static final protected int thumbStateF7InstructionMask = 0xf200;
  static final protected int thumbStateF7InstructionBits = 0x5000;
  static final protected int thumbStateF7LoadStoreBit    = 0x0800;
  static final protected int thumbStateF7ByteWordBit     = 0x0400;
  static final protected int thumbStateF7RoMask          = 0x01c0;
  static final protected int thumbStateF7RbMask          = 0x0038;
  static final protected int thumbStateF7RdMask          = 0x0007;

  static final protected int thumbStateF8InstructionMask = 0xf200;
  static final protected int thumbStateF8InstructionBits = 0x5200;
  static final protected int thumbStateF8HBit            = 0x0800;
  static final protected int thumbStateF8SignExtendedBit = 0x0400;
  static final protected int thumbStateF8RoMask          = 0x01c0;
  static final protected int thumbStateF8RbMask          = 0x0038;
  static final protected int thumbStateF8RdMask          = 0x0007;

  static final protected int thumbStateF9InstructionMask = 0xe000;
  static final protected int thumbStateF9InstructionBits = 0x6000;
  static final protected int thumbStateF9ByteWordBit     = 0x1000;
  static final protected int thumbStateF9LoadStoreBit    = 0x0800;
  static final protected int thumbStateF9OffsetMask      = 0x07c0;
  static final protected int thumbStateF9RbMask          = 0x0038;
  static final protected int thumbStateF9RdMask          = 0x0007;

  static final protected int thumbStateF10InstructionMask = 0xf000;
  static final protected int thumbStateF10InstructionBits = 0x8000;
  static final protected int thumbStateF10LoadStoreBit    = 0x0800;
  static final protected int thumbStateF10OffsetMask      = 0x07c0;
  static final protected int thumbStateF10RbMask          = 0x0038;
  static final protected int thumbStateF10RdMask          = 0x0007;

  static final protected int thumbStateF11InstructionMask = 0xf000;
  static final protected int thumbStateF11InstructionBits = 0x9000;
  static final protected int thumbStateF11LoadStoreBit    = 0x0800;
  static final protected int thumbStateF11RdMask          = 0x0700;
  static final protected int thumbStateF11ImmMask         = 0x00ff;

  static final protected int thumbStateF12InstructionMask = 0xf000;
  static final protected int thumbStateF12InstructionBits = 0xa000;
  static final protected int thumbStateF12SPBit           = 0x0800;
  static final protected int thumbStateF12RdMask          = 0x0700;
  static final protected int thumbStateF12OffsetMask      = 0x00ff;

  static final protected int thumbStateF13InstructionMask = 0xff00;
  static final protected int thumbStateF13InstructionBits = 0xb000;
  static final protected int thumbStateF13SignBit         = 0x0080;
  static final protected int thumbStateF13ImmediateMask   = 0x007f;

  static final protected int thumbStateF14InstructionMask = 0xf600;
  static final protected int thumbStateF14InstructionBits = 0xb400;
  static final protected int thumbStateF14LoadStoreBit    = 0x0800;
  static final protected int thumbStateF14PcLrBit         = 0x0100;

  static final protected int thumbStateF15InstructionMask = 0xf000;
  static final protected int thumbStateF15InstructionBits = 0xc000;
  static final protected int thumbStateF15LoadStoreBit    = 0x0800;
  static final protected int thumbStateF15RbMask          = 0x0700;

  static final protected int thumbStateF16InstructionMask = 0xf000;
  static final protected int thumbStateF16InstructionBits = 0xd000;
  static final protected int thumbStateF16CondMask        = 0x0f00;
  static final protected int thumbStateF16SOffsetMask     = 0x00ff;
  static final protected int thumbStateF16EQBits          = 0x0000;
  static final protected int thumbStateF16NEBits          = 0x0100;
  static final protected int thumbStateF16CSBits          = 0x0200;
  static final protected int thumbStateF16CCBits          = 0x0300;
  static final protected int thumbStateF16MIBits          = 0x0400;
  static final protected int thumbStateF16PLBits          = 0x0500;
  static final protected int thumbStateF16VSBits          = 0x0600;
  static final protected int thumbStateF16VCBits          = 0x0700;
  static final protected int thumbStateF16HIBits          = 0x0800;
  static final protected int thumbStateF16LSBits          = 0x0900;
  static final protected int thumbStateF16GEBits          = 0x0a00;
  static final protected int thumbStateF16LTBits          = 0x0b00;
  static final protected int thumbStateF16GTBits          = 0x0c00;
  static final protected int thumbStateF16LEBits          = 0x0d00;

  static final protected int thumbStateF17InstructionMask = 0xff00;
  static final protected int thumbStateF17InstructionBits = 0xdf00;

  static final protected int thumbStateF18InstructionMask = 0xf800;
  static final protected int thumbStateF18InstructionBits = 0xe000;
  static final protected int thumbStateF18OffsetMask      = 0x07ff;

  static final protected int thumbStateF19InstructionMask = 0xf000;
  static final protected int thumbStateF19InstructionBits = 0xf000;
  static final protected int thumbStateF19LowHiOffsetBit  = 0x0800;
  static final protected int thumbStateF19OffsetMask      = 0x07ff;

  protected void decodeThumbStateInstruction(short halfWord)
    throws AbortException
  {
    // Format 2
    if ((halfWord & thumbStateF2InstructionMask) == thumbStateF2InstructionBits)
    {
      //System.out.println("Format 2");

      int sourceValue = getRegister((halfWord & thumbStateF2RsMask) >>> 3).get();
      ArmReg destinationRegister = getRegister(halfWord & thumbStateF2RdMask);
      int addValue = (halfWord & thumbStateF2RnMask) >>> 6;

      if ((halfWord & thumbStateF2ImmediateBit) == 0)
        addValue = getRegister(addValue).get();
      if ((halfWord & thumbStateF2OpBit) != 0)
        addValue = -addValue;

      long longResult = sourceValue + addValue;

      CPSR.setBit(cFlagBit, ((0xffffffffL & (long) sourceValue) + (0xffffffffL & (long) addValue) > 0xffffffffL));
      CPSR.setBit(zFlagBit, (longResult == 0));
      CPSR.setBit(nFlagBit, ((longResult & 0x80000000) != 0));
      CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));

      destinationRegister.set((int) longResult);
    }

    // Format 1
    else if ((halfWord & thumbStateF1InstructionMask) == thumbStateF1InstructionBits)
    {
      int sourceValue = getRegister((halfWord & thumbStateF1RsMask) >>> 3).get();
      ArmReg destinationRegister = getRegister(halfWord & thumbStateF1RdMask);
      int shiftType = halfWord & thumbStateF1OpMask;
      int offset = (halfWord & thumbStateF1OffsetMask) >>> 6;

      if (offset != 0)
      {
        if (shiftType == thumbStateF1LslBits)
        {
          CPSR.setBit(cFlagBit, ((sourceValue & (1 << (32 - offset))) != 0));
          sourceValue <<= offset;
        }
        else if (shiftType == thumbStateF1LsrBits)
        {
          CPSR.setBit(cFlagBit, ((sourceValue & (1 << (offset - 1))) != 0));
          sourceValue >>>= offset;
        }
        else if (shiftType == thumbStateF1AsrBits)
        {
          CPSR.setBit(cFlagBit, ((sourceValue & (1 << (offset - 1))) != 0));
          sourceValue >>= offset;
        }
      }

      destinationRegister.set(sourceValue);

      CPSR.setBit(zFlagBit, (sourceValue == 0));
      CPSR.setBit(nFlagBit, (sourceValue < 0));
    }

    // Format 3
    else if ((halfWord & thumbStateF3InstructionMask) == thumbStateF3InstructionBits)
    {
      //System.out.println("Format 3");

      ArmReg srcDstRegister = getRegister((halfWord & thumbStateF3RdMask) >>> 8);
      int operation = halfWord & thumbStateF3OpMask;
      int immediateValue = halfWord & thumbStateF3OffsetMask;
      if (operation == thumbStateF3MovBits)
      {
        srcDstRegister.set(immediateValue);

        CPSR.setBit(zFlagBit, (immediateValue == 0));
        CPSR.setBit(nFlagBit, (immediateValue < 0));
      }
      else if (operation == thumbStateF3CmpBits)
      {
        int sourceValue = srcDstRegister.get();
        long longResult = sourceValue - immediateValue;

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) sourceValue) + (0xffffffffL & (long) (- immediateValue)) > 0xffffffffL));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, ((longResult & 0x80000000) != 0));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
      }
      else if (operation == thumbStateF3AddBits)
      {
        int sourceValue = srcDstRegister.get();
        long longResult = sourceValue + immediateValue;
        srcDstRegister.set((int) longResult);

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) sourceValue) + (0xffffffffL & (long) immediateValue) > 0xffffffffL));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, ((longResult & 0x80000000) != 0));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
      }
      else if (operation == thumbStateF3SubBits)
      {
        //System.out.println("Format 3 (sub)");

        int sourceValue = srcDstRegister.get();
        long longResult = sourceValue - immediateValue;
        srcDstRegister.set((int) longResult);

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) sourceValue) + (0xffffffffL & (long) (- immediateValue)) > 0xffffffffL));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, ((longResult & 0x80000000) != 0));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
      }
    }

    // Format 4
    else if ((halfWord & thumbStateF4InstructionMask) == thumbStateF4InstructionBits)
    {
      int operation = halfWord & thumbStateF4OpMask;
      ArmReg sourceRegister = getRegister((halfWord & thumbStateF4RsMask) >>> 3);
      ArmReg destinationRegister = getRegister(halfWord & thumbStateF4RdMask);

      if (operation == thumbStateF4AndBits)
      {
        int result = destinationRegister.get() & sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4EorBits)
      {
        int result = destinationRegister.get() ^ sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4LslBits)
      {
        CPSR.setBit(cFlagBit, ((sourceRegister.get() & (1 << (32 - sourceRegister.get()))) != 0));
        int result = destinationRegister.get() << sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4LsrBits)
      {
        CPSR.setBit(cFlagBit, ((sourceRegister.get() & (1 << (sourceRegister.get() - 1))) != 0));
        int result = destinationRegister.get() >>> sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4AsrBits)
      {
        CPSR.setBit(cFlagBit, ((sourceRegister.get() & (1 << (sourceRegister.get() - 1))) != 0));
        int result = destinationRegister.get() >> sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4AdcBits)
      {
        int operand1 = destinationRegister.get();
        int operand2 = sourceRegister.get();
        int cValue = (CPSR.get() >> cFlagBitNumber) & 0x00000001;
        long longResult = operand1 + operand2 + cValue;

        CPSR.setBit(cFlagBit, (((0xffffffffL & (long) operand1) + (0xffffffffL & (long) operand2) + (0xffffffffL & (long) cValue)) & 0x100000000L) != 0);
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));

        destinationRegister.set((int) longResult);
      }
      else if (operation == thumbStateF4SbcBits)
      {
        int operand1 = destinationRegister.get();
        int operand2 = - sourceRegister.get();
        int cValue = ((CPSR.get() >> cFlagBitNumber) & 0x00000001) - 1;
        long longResult = operand1 + operand2 + cValue;

        CPSR.setBit(cFlagBit, (((0xffffffffL & (long) operand1) + (0xffffffffL & (long) operand2) + (0xffffffffL & (long) cValue)) & 0x100000000L) != 0);
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));

        destinationRegister.set((int) longResult);
      }
      else if (operation == thumbStateF4RorBits)
      {
        int result = (destinationRegister.get() >> sourceRegister.get()) |
                     (destinationRegister.get() << (32 - sourceRegister.get()));

        if (sourceRegister.get() != 0) CPSR.setBit(cFlagBit, ((result & 0x80000000) != 0));
        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));

        destinationRegister.set(result);
      }
      else if (operation == thumbStateF4TstBits)
      {
        int result = destinationRegister.get() & sourceRegister.get();

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4NegBits)
      {
        int result = - sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4CmpBits)
      {
        int operand1 = destinationRegister.get();
        int operand2 = - sourceRegister.get();
        long longResult = operand1 + operand2;

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) operand1) + (0xffffffffL & (long) operand2) > 0xffffffffL));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));
      }
      else if (operation == thumbStateF4CmnBits)
      {
        int operand1 = destinationRegister.get();
        int operand2 = sourceRegister.get();
        long longResult = operand1 + operand2;

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) operand1) + (0xffffffffL & (long) operand2) > 0xffffffffL));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));
      }
      else if (operation == thumbStateF4OrrBits)
      {
        int result = destinationRegister.get() | sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4MulBits)
      {
        long longResult = destinationRegister.get() * sourceRegister.get();

        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));

        destinationRegister.set((int) longResult);
      }
      else if (operation == thumbStateF4BicBits)
      {
        int result = destinationRegister.get() & ~sourceRegister.get();

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
      else if (operation == thumbStateF4MvnBits)
      {
        int result = ~sourceRegister.get();
        destinationRegister.set(result);

        CPSR.setBit(zFlagBit, (result == 0));
        CPSR.setBit(nFlagBit, (result < 0));
      }
    }

    // Format 5
    else if ((halfWord & thumbStateF5InstructionMask) == thumbStateF5InstructionBits)
    {
      ArmReg sourceRegister = getRegister((halfWord & (thumbStateF5RsMask | thumbStateF5H2Bit)) >>> 3);
      ArmReg destinationRegister = getRegister((halfWord & thumbStateF5RdMask) | ((halfWord & thumbStateF5H1Bit) >>> 4));
      int operation = halfWord & thumbStateF5OpMask;

      if (operation == thumbStateF5AddBits)
      {
        destinationRegister.add(sourceRegister.get());
      }
      else if (operation == thumbStateF5CmpBits)
      {
        int operand1 = destinationRegister.get();
        int operand2 = - sourceRegister.get();
        long longResult = operand1 + operand2;

        CPSR.setBit(cFlagBit, ((0xffffffffL & (long) operand1) + (0xffffffffL & (long) operand2) > 0xffffffffL));
        CPSR.setBit(vFlagBit, ((longResult > 2147483647) || (longResult < -2147483648)));
        CPSR.setBit(zFlagBit, (longResult == 0));
        CPSR.setBit(nFlagBit, (longResult < 0));
      }
      else if (operation == thumbStateF5MovBits)
      {
        destinationRegister.set(sourceRegister.get());
      }
      else if (operation == thumbStateF5BxBits)
      {
        if ((sourceRegister.get() & 0x00000001) == 0) setArmState();
        else setThumbState();
        PC.set(sourceRegister.get() & 0xfffffffe);
      }

    }

    // Format 6
    else if ((halfWord & thumbStateF6InstructionMask) == thumbStateF6InstructionBits)
    {
      ArmReg destinationRegister = getRegister((halfWord & thumbStateF6RdMask) >>> 8);
      int immediateValue = halfWord & thumbStateF6ImmMask;
      destinationRegister.set(memory.loadWord(((PC.get() + 2) & 0xfffffffc) + (immediateValue << 2)));
    }

    // Format 7
    else if ((halfWord & thumbStateF7InstructionMask) == thumbStateF7InstructionBits)
    {
      int offset = getRegister((halfWord & thumbStateF7RoMask) >>> 6).get() +
                   getRegister((halfWord & thumbStateF7RbMask) >>> 3).get();
      ArmReg srcDstRegister = getRegister(halfWord & thumbStateF7RdMask);

      if ((halfWord & thumbStateF7LoadStoreBit) == 0) // store
      {
        if ((halfWord & thumbStateF7ByteWordBit) == 0) // word
          memory.storeWord(offset, srcDstRegister.get());
        else // byte
          memory.storeByte(offset, (byte) srcDstRegister.get());
      }
      else // load
      {
        if ((halfWord & thumbStateF7ByteWordBit) == 0) // word
        {
          int wordAlignedOffset = offset & 0xfffffffc;
          int rightRotate = (offset & 0x2) << 3;
          int value = memory.loadWord(wordAlignedOffset);
          srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
        }
        else // byte
          srcDstRegister.set(0xff & memory.loadByte(offset));
      }
    }

    // Format 8
    else if ((halfWord & thumbStateF8InstructionMask) == thumbStateF8InstructionBits)
    {
      int offset = getRegister((halfWord & thumbStateF8RoMask) >>> 6).get() +
                   getRegister((halfWord & thumbStateF8RbMask) >>> 3).get();
      ArmReg srcDstRegister = getRegister(halfWord & thumbStateF8RdMask);

      if ((halfWord & thumbStateF8SignExtendedBit) == 0)
      {
        if ((halfWord & thumbStateF8HBit) == 0)
          memory.storeHalfWord(offset, (short) srcDstRegister.get());
        else
          srcDstRegister.set(0xffff & memory.loadHalfWord(offset));
      }
      else
      {
        if ((halfWord & thumbStateF8HBit) == 0)
          srcDstRegister.set(memory.loadByte(offset));
        else
          srcDstRegister.set(memory.loadHalfWord(offset));
      }
    }

    // Format 9
    else if ((halfWord & thumbStateF9InstructionMask) == thumbStateF9InstructionBits)
    {
      int baseOffset = getRegister((halfWord & thumbStateF9RbMask) >>> 3).get();
      int offset = (halfWord & thumbStateF9OffsetMask) >>> 6;
      ArmReg srcDstRegister = getRegister(halfWord & thumbStateF9RdMask);

      if ((halfWord & thumbStateF9ByteWordBit) == 0) // word
      {
        if ((halfWord & thumbStateF9LoadStoreBit) == 0) // store
          memory.storeWord(baseOffset + offset * 4, srcDstRegister.get());
        else // load
        {
          baseOffset += (offset << 2);
          int wordAlignedOffset = baseOffset & 0xfffffffc;
          int rightRotate = (baseOffset & 0x2) << 3;
          int value = memory.loadWord(wordAlignedOffset);
          srcDstRegister.set((value >>> rightRotate) | (value << (32 - rightRotate)));
        }
      }
      else // byte
      {
        if ((halfWord & thumbStateF9LoadStoreBit) == 0) // store
          memory.storeByte(baseOffset + offset, (byte) srcDstRegister.get());
        else // load
          srcDstRegister.set(0xff & memory.loadByte(baseOffset + offset));
      }
    }

    // Format 10
    else if ((halfWord & thumbStateF10InstructionMask) == thumbStateF10InstructionBits)
    {
      int offset = getRegister((halfWord & thumbStateF10RbMask) >>> 3).get() +
                   ((halfWord & thumbStateF10OffsetMask) >>> 6) * 2;
      ArmReg srcDstRegister = getRegister(halfWord & thumbStateF10RdMask);

      if ((halfWord & thumbStateF10LoadStoreBit) == 0) // store
        memory.storeHalfWord(offset, (short) srcDstRegister.get());
      else
       srcDstRegister.set(0xffff & memory.loadHalfWord(offset));
    }

    // Format 11
    else if ((halfWord & thumbStateF11InstructionMask) == thumbStateF11InstructionBits)
    {
      ArmReg srcDstRegister = getRegister((halfWord & thumbStateF11RdMask) >>> 8);
      int immediateValue = halfWord & thumbStateF11ImmMask;
      int offset = (getSP().get() & 0xfffffffc) + (immediateValue * 4);

      if ((halfWord & thumbStateF11LoadStoreBit) == 0) //store
        memory.storeWord(offset, srcDstRegister.get());
      else // load
        srcDstRegister.set(memory.loadWord(offset));
    }

    // Format 12
    else if ((halfWord & thumbStateF12InstructionMask) == thumbStateF12InstructionBits)
    {
      int sourceAdress = (((halfWord & thumbStateF12SPBit) == 0) ? (PC.get() + 4) : getSP().get());
      ArmReg destinationRegister = getRegister((halfWord & thumbStateF12RdMask) >>> 8);
      int offset = halfWord & thumbStateF12OffsetMask;
      destinationRegister.set((sourceAdress & 0xfffffffc) + offset * 4);
    }

    // Format 13
    else if ((halfWord & thumbStateF13InstructionMask) == thumbStateF13InstructionBits)
    {
      int offset = halfWord & thumbStateF13ImmediateMask;
      if ((halfWord & thumbStateF13SignBit) != 0) offset = -offset;
      getSP().add(offset * 4);
    }

    // Format 14
    else if ((halfWord & thumbStateF14InstructionMask) == thumbStateF14InstructionBits)
    {
      ArmReg SP = getSP();
      int spValue = SP.get();

      if ((halfWord & thumbStateF14LoadStoreBit) == 0) // push
      {
        if ((halfWord & thumbStateF14PcLrBit) != 0) // push LR too
        {
          spValue -= 4;
          memory.storeWord(spValue, getLR().get());
        }

        for (int i = 7; i >= 0; i--)
          if ((halfWord & (1 << i)) != 0)
          {
            spValue -= 4;
            memory.storeWord(spValue, getRegister(i).get());
          }
      }
      else // pop
      {
        for (int i = 0; i <= 7; i++)
          if ((halfWord & (1 << i)) != 0)
          {
            getRegister(i).set(memory.loadWord(spValue));
            spValue += 4;
          }

        if ((halfWord & thumbStateF14PcLrBit) != 0) // pop PC too
        {
          PC.set(memory.loadWord(spValue));
          spValue += 4;
        }
      }

      SP.set(spValue);
    }

    // Format 15
    else if ((halfWord & thumbStateF15InstructionMask) == thumbStateF15InstructionBits)
    {
      ArmReg baseRegister = getRegister((halfWord & thumbStateF15RbMask) >>> 8);
      int baseValue = baseRegister.get();

      if ((halfWord & thumbStateF15LoadStoreBit) == 0) // push
      {
        for (int i = 7; i >= 0; i--)
          if ((halfWord & (1 << i)) != 0)
          {
            memory.storeWord(baseValue, getRegister(i).get());
            baseValue += 4;
          }
      }
      else // pop
      {
        for (int i = 0; i <= 7; i++)
          if ((halfWord & (1 << i)) != 0)
          {
            getRegister(i).set(memory.loadWord(baseValue));
            baseValue += 4;
          }
      }

      baseRegister.set(baseValue);
    }

    // Format 17
    else if ((halfWord & thumbStateF17InstructionMask) == thumbStateF17InstructionBits)
    {
      svcRegisters[14].set(PC);
      SPSR_svc.set(CPSR);
      setMode(svcModeBits);
      PC.set(softwareInterrupVectorAddress);
      setArmState();
    }

    // Format 16
    else if ((halfWord & thumbStateF16InstructionMask) == thumbStateF16InstructionBits)
    {
      int offset = (int) ((byte) (halfWord & thumbStateF16SOffsetMask));
      int condType = halfWord & thumbStateF16CondMask;
      boolean condition = false; // Si condType indefini, ne saute pas, ne fait rien.

           if (condType == thumbStateF16EQBits) condition =  CPSR.isBitSet(zFlagBit);
      else if (condType == thumbStateF16NEBits) condition = !CPSR.isBitSet(zFlagBit);
      else if (condType == thumbStateF16CSBits) condition =  CPSR.isBitSet(cFlagBit);
      else if (condType == thumbStateF16CCBits) condition = !CPSR.isBitSet(cFlagBit);
      else if (condType == thumbStateF16MIBits) condition =  CPSR.isBitSet(nFlagBit);
      else if (condType == thumbStateF16PLBits) condition = !CPSR.isBitSet(nFlagBit);
      else if (condType == thumbStateF16VSBits) condition =  CPSR.isBitSet(vFlagBit);
      else if (condType == thumbStateF16VCBits) condition = !CPSR.isBitSet(vFlagBit);
      else if (condType == thumbStateF16HIBits) condition =  CPSR.isBitSet(cFlagBit) && !CPSR.isBitSet(zFlagBit);
      else if (condType == thumbStateF16LSBits) condition = !CPSR.isBitSet(cFlagBit) ||  CPSR.isBitSet(zFlagBit);
      else if (condType == thumbStateF16GEBits) condition =  CPSR.isBitSet(nFlagBit) ==  CPSR.isBitSet(vFlagBit);
      else if (condType == thumbStateF16LTBits) condition =  CPSR.isBitSet(nFlagBit) !=  CPSR.isBitSet(vFlagBit);
      else if (condType == thumbStateF16GTBits) condition = !CPSR.isBitSet(zFlagBit) && (CPSR.isBitSet(nFlagBit) == CPSR.isBitSet(vFlagBit));
      else if (condType == thumbStateF16LEBits) condition =  CPSR.isBitSet(zFlagBit) || (CPSR.isBitSet(nFlagBit) != CPSR.isBitSet(vFlagBit));

      if (condition) PC.set(PC.get() + 2 + offset * 2);
    }

    // Format 18
    else if ((halfWord & thumbStateF18InstructionMask) == thumbStateF18InstructionBits)
    {
      int offset = ((halfWord & thumbStateF18OffsetMask) << 21) >> 21;
      PC.set(PC.get() + 2 + offset * 2);
    }

    // Format 19
    else if ((halfWord & thumbStateF19InstructionMask) == thumbStateF19InstructionBits)
    {
      int offset = halfWord & thumbStateF19OffsetMask;
      if ((halfWord & thumbStateF19LowHiOffsetBit) == 0) // High offset
      {
        offset = ((offset << 21) >> 21) << 12;
        getLR().set(offset + 2 + PC.get());
      }
      else // Low offset
      {
        ArmReg LR = getLR();
        LR.add(offset << 1);
        int nextInstrAdress = PC.get();
        PC.set(LR);
        LR.set(nextInstrAdress | 0x01);
      }
    }

  }

}
