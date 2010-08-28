package com.lemoulinstudio.gfa.core.cpu.test;

public class Instruction {

  public static enum Bit {
    B0, B1, BX;
  }

  private final String name;
  private final int mask;
  private final int bits;

  public Instruction(String name, int mask, int bits) {
    this.name = name;
    this.mask = mask;
    this.bits = bits;
  }

  public String getName() {
    return name;
  }

  public int getMask() {
    return mask;
  }

  public int getBits() {
    return bits;
  }

  public Bit getBit(int index) {
    boolean m = (mask & (1 << index)) == (1 << index);
    boolean b = (bits & (1 << index)) == (1 << index);

    if (m && !b) return Bit.B0;
    else if(m && b) return Bit.B1;
    else return Bit.BX;
  }

  /* Instruction a is prefix of instruction b if and only if
     there exist an instruction b can be decoded as an instruction a. */
  public boolean isPrefixOf(Instruction obj) {
    for (int i = 0; i < 32; i++) {
      Bit a = this.getBit(i);
      Bit b = obj.getBit(i);

      if ((a != Bit.BX) && (b != a))
        return false;
    }

    return true;
  }

}
