package gfa.cpu;

import gfa.cpu.instruction.*;
import gfa.memory.MemoryInterface;
import gfa.util.*;

public class InstructionDecoder
{
  protected ArmStateInstruction armStateBx;
  protected ArmStateInstruction armStateB;
  protected ArmStateInstruction armStateMrs;
  protected ArmStateInstruction armStateMsr1;
  protected ArmStateInstruction armStateMsr2;
  protected ArmStateInstruction armStateMul;
  protected ArmStateInstruction armStateMull;
  protected ArmStateInstruction armStateHsdtro;
  protected ArmStateInstruction armStateHsdtio;
  protected ArmStateInstruction armStateSwp;
  protected ArmStateInstruction armStateAnd;
  protected ArmStateInstruction armStateEor;
  protected ArmStateInstruction armStateSub;
  protected ArmStateInstruction armStateRsb;
  protected ArmStateInstruction armStateAdd;
  protected ArmStateInstruction armStateAdc;
  protected ArmStateInstruction armStateSbc;
  protected ArmStateInstruction armStateRsc;
  protected ArmStateInstruction armStateTst;
  protected ArmStateInstruction armStateTeq;
  protected ArmStateInstruction armStateCmp;
  protected ArmStateInstruction armStateCmn;
  protected ArmStateInstruction armStateOrr;
  protected ArmStateInstruction armStateMov;
  protected ArmStateInstruction armStateBic;
  protected ArmStateInstruction armStateMvn;
  protected ArmStateInstruction armStateUndef;
  protected ArmStateInstruction armStateLdrStr;
  protected ArmStateInstruction armStateLdmStm;
  protected ArmStateInstruction armStateSwi;
  protected ArmStateInstruction armStateUnknown;

  protected ThumbStateInstruction[] thumbInstructions;

  protected ThumbStateInstruction thumbStateF1;
  protected ThumbStateInstruction thumbStateF2;
  protected ThumbStateInstruction thumbStateF3Mov;
  protected ThumbStateInstruction thumbStateF3Cmp;
  protected ThumbStateInstruction thumbStateF3Add;
  protected ThumbStateInstruction thumbStateF3Sub;
  protected ThumbStateInstruction thumbStateF4And;
  protected ThumbStateInstruction thumbStateF4Eor;
  protected ThumbStateInstruction thumbStateF4Lsl;
  protected ThumbStateInstruction thumbStateF4Lsr;
  protected ThumbStateInstruction thumbStateF4Asr;
  protected ThumbStateInstruction thumbStateF4Adc;
  protected ThumbStateInstruction thumbStateF4Sbc;
  protected ThumbStateInstruction thumbStateF4Ror;
  protected ThumbStateInstruction thumbStateF4Tst;
  protected ThumbStateInstruction thumbStateF4Neg;
  protected ThumbStateInstruction thumbStateF4Cmp;
  protected ThumbStateInstruction thumbStateF4Cmn;
  protected ThumbStateInstruction thumbStateF4Orr;
  protected ThumbStateInstruction thumbStateF4Mul;
  protected ThumbStateInstruction thumbStateF4Bic;
  protected ThumbStateInstruction thumbStateF4Mvn;
  protected ThumbStateInstruction thumbStateF5Add;
  protected ThumbStateInstruction thumbStateF5Cmp;
  protected ThumbStateInstruction thumbStateF5Mov;
  protected ThumbStateInstruction thumbStateF5Bx;
  protected ThumbStateInstruction thumbStateF6;
  protected ThumbStateInstruction thumbStateF7;
  protected ThumbStateInstruction thumbStateF8;
  protected ThumbStateInstruction thumbStateF9;
  protected ThumbStateInstruction thumbStateF10;
  protected ThumbStateInstruction thumbStateF11;
  protected ThumbStateInstruction thumbStateF12;
  protected ThumbStateInstruction thumbStateF13;
  protected ThumbStateInstruction thumbStateF14;
  protected ThumbStateInstruction thumbStateF15;
  protected ThumbStateInstruction thumbStateF16;
  protected ThumbStateInstruction thumbStateF17;
  protected ThumbStateInstruction thumbStateF18;
  protected ThumbStateInstruction thumbStateF19;
  protected ThumbStateInstruction thumbStateUnknown;

  public InstructionDecoder(ArmReg[][] regs, MemoryInterface memory)
  {
    armStateBx      = new ArmStateBx(regs, memory);
    armStateB       = new ArmStateB(regs, memory);
    armStateMrs     = new ArmStateMrs(regs, memory);
    armStateMsr1    = new ArmStateMsr1(regs, memory);
    armStateMsr2    = new ArmStateMsr2(regs, memory);
    armStateMul     = new ArmStateMul(regs, memory);
    armStateMull    = new ArmStateMull(regs, memory);
    armStateHsdtro  = new ArmStateHsdtro(regs, memory);
    armStateHsdtio  = new ArmStateHsdtio(regs, memory);
    armStateSwp     = new ArmStateSwp(regs, memory);
    armStateAnd     = new ArmStateAnd(regs, memory);
    armStateEor     = new ArmStateEor(regs, memory);
    armStateSub     = new ArmStateSub(regs, memory);
    armStateRsb     = new ArmStateRsb(regs, memory);
    armStateAdd     = new ArmStateAdd(regs, memory);
    armStateAdc     = new ArmStateAdc(regs, memory);
    armStateSbc     = new ArmStateSbc(regs, memory);
    armStateRsc     = new ArmStateRsc(regs, memory);
    armStateTst     = new ArmStateTst(regs, memory);
    armStateTeq     = new ArmStateTeq(regs, memory);
    armStateCmp     = new ArmStateCmp(regs, memory);
    armStateCmn     = new ArmStateCmn(regs, memory);
    armStateOrr     = new ArmStateOrr(regs, memory);
    armStateMov     = new ArmStateMov(regs, memory);
    armStateBic     = new ArmStateBic(regs, memory);
    armStateMvn     = new ArmStateMvn(regs, memory);
    armStateUndef   = new ArmStateUndef(regs, memory);
    armStateLdrStr  = new ArmStateLdrStr(regs, memory);
    armStateLdmStm  = new ArmStateLdmStm(regs, memory);
    armStateSwi     = new ArmStateSwi(regs, memory);
    armStateUnknown = new ArmStateUnknown(regs, memory);
    
    thumbStateF1      = new ThumbStateF1(regs, memory);
    thumbStateF2      = new ThumbStateF2(regs, memory);
    thumbStateF3Mov   = new ThumbStateF3Mov(regs, memory);
    thumbStateF3Cmp   = new ThumbStateF3Cmp(regs, memory);
    thumbStateF3Add   = new ThumbStateF3Add(regs, memory);
    thumbStateF3Sub   = new ThumbStateF3Sub(regs, memory);
    thumbStateF4And   = new ThumbStateF4And(regs, memory);
    thumbStateF4Eor   = new ThumbStateF4Eor(regs, memory);
    thumbStateF4Lsl   = new ThumbStateF4Lsl(regs, memory);
    thumbStateF4Lsr   = new ThumbStateF4Lsr(regs, memory);
    thumbStateF4Asr   = new ThumbStateF4Asr(regs, memory);
    thumbStateF4Adc   = new ThumbStateF4Adc(regs, memory);
    thumbStateF4Sbc   = new ThumbStateF4Sbc(regs, memory);
    thumbStateF4Ror   = new ThumbStateF4Ror(regs, memory);
    thumbStateF4Tst   = new ThumbStateF4Tst(regs, memory);
    thumbStateF4Neg   = new ThumbStateF4Neg(regs, memory);
    thumbStateF4Cmp   = new ThumbStateF4Cmp(regs, memory);
    thumbStateF4Cmn   = new ThumbStateF4Cmn(regs, memory);
    thumbStateF4Orr   = new ThumbStateF4Orr(regs, memory);
    thumbStateF4Mul   = new ThumbStateF4Mul(regs, memory);
    thumbStateF4Bic   = new ThumbStateF4Bic(regs, memory);
    thumbStateF4Mvn   = new ThumbStateF4Mvn(regs, memory);
    thumbStateF5Add   = new ThumbStateF5Add(regs, memory);
    thumbStateF5Cmp   = new ThumbStateF5Cmp(regs, memory);
    thumbStateF5Mov   = new ThumbStateF5Mov(regs, memory);
    thumbStateF5Bx    = new ThumbStateF5Bx(regs, memory);
    thumbStateF6      = new ThumbStateF6(regs, memory);
    thumbStateF7      = new ThumbStateF7(regs, memory);
    thumbStateF8      = new ThumbStateF8(regs, memory);
    thumbStateF9      = new ThumbStateF9(regs, memory);
    thumbStateF10     = new ThumbStateF10(regs, memory);
    thumbStateF11     = new ThumbStateF11(regs, memory);
    thumbStateF12     = new ThumbStateF12(regs, memory);
    thumbStateF13     = new ThumbStateF13(regs, memory);
    thumbStateF14     = new ThumbStateF14(regs, memory);
    thumbStateF15     = new ThumbStateF15(regs, memory);
    thumbStateF16     = new ThumbStateF16(regs, memory);
    thumbStateF17     = new ThumbStateF17(regs, memory);
    thumbStateF18     = new ThumbStateF18(regs, memory);
    thumbStateF19     = new ThumbStateF19(regs, memory);
    thumbStateUnknown = new ThumbStateUnknown(regs, memory);
    
    thumbInstructions = new ThumbStateInstruction[65536];
    for (int i = 0; i < 65536; i++)
      thumbInstructions[i] = oldFashionDecodeThumbInstruction((short) i);
  }

  static final protected int thumbStateF1InstructionMask  = 0x0000e000;
  static final protected int thumbStateF1InstructionBits  = 0x00000000;
  static final protected int thumbStateF2InstructionMask  = 0x0000f800;
  static final protected int thumbStateF2InstructionBits  = 0x00001800;
  static final protected int thumbStateF3InstructionMask  = 0x0000e000;
  static final protected int thumbStateF3OpMask           = 0x00001800;
  static final protected int thumbStateF3MovBits          = 0x00000000;
  static final protected int thumbStateF3CmpBits          = 0x00000800;
  static final protected int thumbStateF3AddBits          = 0x00001000;
  static final protected int thumbStateF3SubBits          = 0x00001800;
  static final protected int thumbStateF3InstructionBits  = 0x00002000;
  static final protected int thumbStateF4InstructionMask  = 0x0000fc00;
  static final protected int thumbStateF4InstructionBits  = 0x00004000;
  static final protected int thumbStateF4OpMask           = 0x000003c0;
  static final protected int thumbStateF4AndBits          = 0x00000000;
  static final protected int thumbStateF4EorBits          = 0x00000040;
  static final protected int thumbStateF4LslBits          = 0x00000080;
  static final protected int thumbStateF4LsrBits          = 0x000000c0;
  static final protected int thumbStateF4AsrBits          = 0x00000100;
  static final protected int thumbStateF4AdcBits          = 0x00000140;
  static final protected int thumbStateF4SbcBits          = 0x00000180;
  static final protected int thumbStateF4RorBits          = 0x000001c0;
  static final protected int thumbStateF4TstBits          = 0x00000200;
  static final protected int thumbStateF4NegBits          = 0x00000240;
  static final protected int thumbStateF4CmpBits          = 0x00000280;
  static final protected int thumbStateF4CmnBits          = 0x000002c0;
  static final protected int thumbStateF4OrrBits          = 0x00000300;
  static final protected int thumbStateF4MulBits          = 0x00000340;
  static final protected int thumbStateF4BicBits          = 0x00000380;
  static final protected int thumbStateF4MvnBits          = 0x000003c0;
  static final protected int thumbStateF5InstructionMask  = 0x0000fc00;
  static final protected int thumbStateF5InstructionBits  = 0x00004400;
  static final protected int thumbStateF5OpMask           = 0x00000300;
  static final protected int thumbStateF5AddBits          = 0x00000000;
  static final protected int thumbStateF5CmpBits          = 0x00000100;
  static final protected int thumbStateF5MovBits          = 0x00000200;
  static final protected int thumbStateF5BxBits           = 0x00000300;
  static final protected int thumbStateF6InstructionMask  = 0x0000f800;
  static final protected int thumbStateF6InstructionBits  = 0x00004800;
  static final protected int thumbStateF7InstructionMask  = 0x0000f200;
  static final protected int thumbStateF7InstructionBits  = 0x00005000;
  static final protected int thumbStateF8InstructionMask  = 0x0000f200;
  static final protected int thumbStateF8InstructionBits  = 0x00005200;
  static final protected int thumbStateF9InstructionMask  = 0x0000e000;
  static final protected int thumbStateF9InstructionBits  = 0x00006000;
  static final protected int thumbStateF10InstructionMask = 0x0000f000;
  static final protected int thumbStateF10InstructionBits = 0x00008000;
  static final protected int thumbStateF11InstructionMask = 0x0000f000;
  static final protected int thumbStateF11InstructionBits = 0x00009000;
  static final protected int thumbStateF12InstructionMask = 0x0000f000;
  static final protected int thumbStateF12InstructionBits = 0x0000a000;
  static final protected int thumbStateF13InstructionMask = 0x0000ff00;
  static final protected int thumbStateF13InstructionBits = 0x0000b000;
  static final protected int thumbStateF14InstructionMask = 0x0000f600;
  static final protected int thumbStateF14InstructionBits = 0x0000b400;
  static final protected int thumbStateF15InstructionMask = 0x0000f000;
  static final protected int thumbStateF15InstructionBits = 0x0000c000;
  static final protected int thumbStateF16InstructionMask = 0x0000f000;
  static final protected int thumbStateF16InstructionBits = 0x0000d000;
  static final protected int thumbStateF17InstructionMask = 0x0000ff00;
  static final protected int thumbStateF17InstructionBits = 0x0000df00;
  static final protected int thumbStateF18InstructionMask = 0x0000f800;
  static final protected int thumbStateF18InstructionBits = 0x0000e000;
  static final protected int thumbStateF19InstructionMask = 0x0000f000;
  static final protected int thumbStateF19InstructionBits = 0x0000f000;

  public ThumbStateInstruction decodeThumbInstruction(short opcode)
  {
      ThumbStateInstruction instruction = thumbInstructions[0x0000ffff & opcode];
      instruction.setOpcode(opcode);
      return instruction;
  }
  
  public ThumbStateInstruction oldFashionDecodeThumbInstruction(short opcode)
  {
    ThumbStateInstruction instruction;

         if ((opcode & thumbStateF2InstructionMask)  == thumbStateF2InstructionBits)
      instruction = thumbStateF2;
    else if ((opcode & thumbStateF1InstructionMask)  == thumbStateF1InstructionBits)
      instruction = thumbStateF1;
    else if ((opcode & thumbStateF3InstructionMask)  == thumbStateF3InstructionBits)
    {
      switch (opcode & thumbStateF3OpMask)
      {
      case thumbStateF3MovBits: instruction = thumbStateF3Mov; break;
      case thumbStateF3CmpBits: instruction = thumbStateF3Cmp; break;
      case thumbStateF3AddBits: instruction = thumbStateF3Add; break;
      case thumbStateF3SubBits: instruction = thumbStateF3Sub; break;
      default: instruction = null; // it can't happen
      }
    }
    else if ((opcode & thumbStateF4InstructionMask)  == thumbStateF4InstructionBits)
    {
      switch (opcode & thumbStateF4OpMask)
      {
      case thumbStateF4AndBits: instruction = thumbStateF4And; break;
      case thumbStateF4EorBits: instruction = thumbStateF4Eor; break;
      case thumbStateF4LslBits: instruction = thumbStateF4Lsl; break;
      case thumbStateF4LsrBits: instruction = thumbStateF4Lsr; break;
      case thumbStateF4AsrBits: instruction = thumbStateF4Asr; break;
      case thumbStateF4AdcBits: instruction = thumbStateF4Adc; break;
      case thumbStateF4SbcBits: instruction = thumbStateF4Sbc; break;
      case thumbStateF4RorBits: instruction = thumbStateF4Ror; break;
      case thumbStateF4TstBits: instruction = thumbStateF4Tst; break;
      case thumbStateF4NegBits: instruction = thumbStateF4Neg; break;
      case thumbStateF4CmpBits: instruction = thumbStateF4Cmp; break;
      case thumbStateF4CmnBits: instruction = thumbStateF4Cmn; break;
      case thumbStateF4OrrBits: instruction = thumbStateF4Orr; break;
      case thumbStateF4MulBits: instruction = thumbStateF4Mul; break;
      case thumbStateF4BicBits: instruction = thumbStateF4Bic; break;
      case thumbStateF4MvnBits: instruction = thumbStateF4Mvn; break;
      default: instruction = null; // it can't happen
      }
    }
    else if ((opcode & thumbStateF5InstructionMask)  == thumbStateF5InstructionBits)
    {
      switch (opcode & thumbStateF5OpMask)
      {
      case thumbStateF5AddBits: instruction = thumbStateF5Add; break;
      case thumbStateF5CmpBits: instruction = thumbStateF5Cmp; break;
      case thumbStateF5MovBits: instruction = thumbStateF5Mov; break;
      case thumbStateF5BxBits:  instruction = thumbStateF5Bx;  break;
      default: instruction = null; // it can't happen
      }
    }
    else if ((opcode & thumbStateF6InstructionMask)  == thumbStateF6InstructionBits)
      instruction = thumbStateF6;
    else if ((opcode & thumbStateF7InstructionMask)  == thumbStateF7InstructionBits)
      instruction = thumbStateF7;
    else if ((opcode & thumbStateF8InstructionMask)  == thumbStateF8InstructionBits)
      instruction = thumbStateF8;
    else if ((opcode & thumbStateF9InstructionMask)  == thumbStateF9InstructionBits)
      instruction = thumbStateF9;
    else if ((opcode & thumbStateF10InstructionMask) == thumbStateF10InstructionBits)
      instruction = thumbStateF10;
    else if ((opcode & thumbStateF11InstructionMask) == thumbStateF11InstructionBits)
      instruction = thumbStateF11;
    else if ((opcode & thumbStateF12InstructionMask) == thumbStateF12InstructionBits)
      instruction = thumbStateF12;
    else if ((opcode & thumbStateF13InstructionMask) == thumbStateF13InstructionBits)
      instruction = thumbStateF13;
    else if ((opcode & thumbStateF14InstructionMask) == thumbStateF14InstructionBits)
      instruction = thumbStateF14;
    else if ((opcode & thumbStateF15InstructionMask) == thumbStateF15InstructionBits)
      instruction = thumbStateF15;
    else if ((opcode & thumbStateF17InstructionMask) == thumbStateF17InstructionBits)
      instruction = thumbStateF17;
    else if ((opcode & thumbStateF16InstructionMask) == thumbStateF16InstructionBits)
      instruction = thumbStateF16;
    else if ((opcode & thumbStateF18InstructionMask) == thumbStateF18InstructionBits)
      instruction = thumbStateF18;
    else if ((opcode & thumbStateF19InstructionMask) == thumbStateF19InstructionBits)
      instruction = thumbStateF19;
    else
      instruction = thumbStateUnknown;
    
    instruction.setOpcode(opcode);
    return instruction;
  }

  static final protected int armStateBXInstructionMask             = 0x0ffffff0;
  static final protected int armStateBXInstructionBits             = 0x012fff10;
  static final protected int armStateBInstructionMask              = 0x0e000000;
  static final protected int armStateBInstructionBits              = 0x0a000000;
  static final protected int armStateMRSInstructionMask            = 0x0fbf0fff;
  static final protected int armStateMRSInstructionBits            = 0x010f0000;
  static final protected int armStateMSR1InstructionMask           = 0x0fbffff0;
  static final protected int armStateMSR1InstructionBits           = 0x0129f000;
  static final protected int armStateMSR2InstructionMask           = 0x0dbff000;
  static final protected int armStateMSR2InstructionBits           = 0x0128f000;
  static final protected int armStateMULInstructionMask            = 0x0fc000f0;
  static final protected int armStateMULInstructionBits            = 0x00000090;
  static final protected int armStateMULLInstructionMask           = 0x0f8000f0;
  static final protected int armStateMULLInstructionBits           = 0x00800090;
  static final protected int armStateLDRSTRInstructionMask         = 0x0c000000;
  static final protected int armStateLDRSTRInstructionBits         = 0x04000000;
  static final protected int armStateHSDTROInstructionMask         = 0x0e400f90;
  static final protected int armStateHSDTROInstructionBits         = 0x00000090;
  static final protected int armStateHSDTIOInstructionMask         = 0x0e400090;
  static final protected int armStateHSDTIOInstructionBits         = 0x00400090;
  static final protected int armStateLDMSTMInstructionMask         = 0x0e000000;
  static final protected int armStateLDMSTMInstructionBits         = 0x08000000;
  static final protected int armStateSWPInstructionMask            = 0x0fb00ff0;
  static final protected int armStateSWPInstructionBits            = 0x01000090;
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
  static final protected int armStateSWIInstructionMask            = 0x0f000000;
  static final protected int armStateSWIInstructionBits            = 0x0f000000;
  static final protected int armStateUndefInstructionMask          = 0x0e000010;
  static final protected int armStateUndefInstructionBits          = 0x06000010;

  public ArmStateInstruction decodeArmInstruction(int opcode)
  {
    ArmStateInstruction instruction;
    
         if ((opcode & armStateBXInstructionMask)             == armStateBXInstructionBits)
      instruction = armStateBx;
    else if ((opcode & armStateBInstructionMask)              == armStateBInstructionBits)
      instruction = armStateB;
    else if ((opcode & armStateMRSInstructionMask)            == armStateMRSInstructionBits)
      instruction = armStateMrs;
    else if ((opcode & armStateMSR1InstructionMask)           == armStateMSR1InstructionBits)
      instruction = armStateMsr1;
    else if ((opcode & armStateMSR2InstructionMask)           == armStateMSR2InstructionBits)
      instruction = armStateMsr2;
    else if ((opcode & armStateMULInstructionMask)            == armStateMULInstructionBits)
      instruction = armStateMul;
    else if ((opcode & armStateMULLInstructionMask)           == armStateMULLInstructionBits)
      instruction = armStateMull;
    else if ((opcode & armStateHSDTROInstructionMask)         == armStateHSDTROInstructionBits)
      instruction = armStateHsdtro;
    else if ((opcode & armStateHSDTIOInstructionMask)         == armStateHSDTIOInstructionBits)
      instruction = armStateHsdtio;
    else if ((opcode & armStateSWPInstructionMask)            == armStateSWPInstructionBits)
      instruction = armStateSwp;
    else if ((opcode & armStateDataProcessingInstructionMask) == armStateDataProcessingInstructionBits)
    {
      switch (opcode & armStateDataProcessingOpcodeMask)
      {
      case armStateDataProcessingANDBits: instruction = armStateAnd; break;
      case armStateDataProcessingEORBits: instruction = armStateEor; break;
      case armStateDataProcessingSUBBits: instruction = armStateSub; break;
      case armStateDataProcessingRSBBits: instruction = armStateRsb; break;
      case armStateDataProcessingADDBits: instruction = armStateAdd; break;
      case armStateDataProcessingADCBits: instruction = armStateAdc; break;
      case armStateDataProcessingSBCBits: instruction = armStateSbc; break;
      case armStateDataProcessingRSCBits: instruction = armStateRsc; break;
      case armStateDataProcessingTSTBits: instruction = armStateTst; break;
      case armStateDataProcessingTEQBits: instruction = armStateTeq; break;
      case armStateDataProcessingCMPBits: instruction = armStateCmp; break;
      case armStateDataProcessingCMNBits: instruction = armStateCmn; break;
      case armStateDataProcessingORRBits: instruction = armStateOrr; break;
      case armStateDataProcessingMOVBits: instruction = armStateMov; break;
      case armStateDataProcessingBICBits: instruction = armStateBic; break;
      case armStateDataProcessingMVNBits: instruction = armStateMvn; break;
      default: instruction = null;
      }
    }
    else if ((opcode & armStateUndefInstructionMask)  == armStateUndefInstructionBits)
      instruction = armStateUndef;
    else if ((opcode & armStateLDRSTRInstructionMask) == armStateLDRSTRInstructionBits)
      instruction = armStateLdrStr;
    else if ((opcode & armStateLDMSTMInstructionMask) == armStateLDMSTMInstructionBits)
      instruction = armStateLdmStm;
    else if ((opcode & armStateSWIInstructionMask)    == armStateSWIInstructionBits)
      instruction = armStateSwi;
    else
      instruction = armStateUnknown;
    
    instruction.setOpcode(opcode);
    return instruction;
  }
}
