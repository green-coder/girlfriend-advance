package com.lemoulinstudio.gfa.core.memory.var;

import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;

public class IOMem32Var implements Mem32Var {

  private IORegisterSpace_8_16_32 ioMem;
  private int address;
  private int mask;

  public IOMem32Var(IORegisterSpace_8_16_32 ioMem, int address, int mask) {
    this.ioMem = ioMem;
    this.address = address;
    this.mask = mask;
  }

  public int getValue() {
    return ioMem.getReg32(address) & mask;
  }

  public void setValue(int value) {
    ioMem.setReg32(address, value & mask);
  }

}
