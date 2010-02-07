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
    this(reg.get());
  }

  public int get()
  {
    return val;
  }

  public void set(int v)
  {
    val = v;
  }

  public boolean equals(ArmReg reg)
  {
    return get() == reg.get();
  }

  public void set(ArmReg reg)
  {
    set(reg.get());
  }

  public void add(int v)
  {
    set(get() + v);
  }

  public void sub(int v)
  {
    set(get() - v);
  }

  public void setOn(int bitMask)
  {
    set(get() | bitMask);
  }

  public void setOff(int bitMask)
  {
    set(get() & ~bitMask);
  }

  public void setBit(int bitMask, boolean isOn)
  {
    if (isOn) setOn(bitMask);
    else setOff(bitMask);
  }

  public boolean isBitSet(int bitMask)
  {
    return ((get() & bitMask) != 0);
  }

  public String toString()
  {
    return Hex.toString(get());
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
