package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;

public class IntReg extends IntExpr {

  protected ArmReg reg;

  public IntReg(ArmReg reg) {
    this.reg = reg;
  }

  public int evaluation() {
    return reg.get();
  }

  @Override
  public boolean isConstant() {
    return false;
  }
}
