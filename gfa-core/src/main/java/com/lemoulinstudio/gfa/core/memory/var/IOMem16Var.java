package com.lemoulinstudio.gfa.core.memory.var;

import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;

public class IOMem16Var implements Mem16Var {

  private IORegisterSpace_8_16_32 ioMem;
  private int address;
  private short setterMask;

  public IOMem16Var(IORegisterSpace_8_16_32 ioMem, int address, short setterMask) {
    this.ioMem = ioMem;
    this.address = address;
    this.setterMask = setterMask;
  }

  public short getValue() {
    return ioMem.getReg16(address);
  }

  public void setValue(short value) {
    ioMem.setReg16(address, (short) (value & setterMask));
  }

}
