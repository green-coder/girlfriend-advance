package gfa.cpu;

import gfa.util.*;

public class ArmReg
{
  protected int val;

  public ArmReg(int v)
  {
    val = v;
  }

  public ArmReg(ArmReg reg)
  {
    val = reg.val;
  }

  public int get()
  {
    return val;
  }

  public void set(int v)
  {
    val = v;
  }

  public void set(ArmReg reg)
  {
    val = reg.val;
  }

  public void add(int v)
  {
    val += v;
  }

  public void sub(int v)
  {
    val -= v;
  }

  public void setOn(int bitMask)
  {
    val |= bitMask;
  }

  public void setOff(int bitMask)
  {
    val &= ~bitMask;
  }

  public void setBit(int bitMask, boolean isOn)
  {
    if (isOn) setOn(bitMask);
    else setOff(bitMask);
  }

  public boolean isBitSet(int bitMask)
  {
    return ((val & bitMask) != 0);
  }

  public String toString()
  {
    return Hex.toString(val);
  }

  // Code adapted from the elogba emu (from eloist) : from the file opcodes.c
  public void setCVFlagsForSub(int op1, int op2, int res)
  {
    boolean a = (op1 < 0);
    boolean b = (op2 < 0);
    boolean c = (res < 0);
    setBit(Arm7Tdmi.cFlagBit, (a && !b) || (a && !c) || (!b && !c));
    setBit(Arm7Tdmi.vFlagBit, (a && !b && !c) || (!a && b && c));
  }

  // Code adapted from the elogba emu (from eloist) : from the file opcodes.c
  public void setCVFlagsForAdd(int op1, int op2, int res)
  {
    boolean a = (op1 < 0);
    boolean b = (op2 < 0);
    boolean c = (res < 0);
    setBit(Arm7Tdmi.cFlagBit, (a && b) || (a && !c) || (b && !c));
    setBit(Arm7Tdmi.vFlagBit, (a && b && !c) || (!a && !b && c));
  }

}
