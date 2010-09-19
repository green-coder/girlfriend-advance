package com.lemoulinstudio.gfa.core.memory.var;

import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;

public class IOMem32Var implements Mem32Var {

  private IORegisterSpace_8_16_32 ioMem;
  private int address;
  private int setterMask;

  public IOMem32Var(IORegisterSpace_8_16_32 ioMem, int address, int setterMask) {
    this.ioMem = ioMem;
    this.address = address;
    this.setterMask = setterMask;
  }

  public int getValue() {
    return ioMem.getReg32(address);
  }

  public void setValue(int value) {
    ioMem.setReg32(address, value & setterMask);
  }

}
