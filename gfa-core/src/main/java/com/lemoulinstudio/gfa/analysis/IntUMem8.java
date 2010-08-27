package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class IntUMem8 extends IntExpr {

  protected final IntExpr offset;
  protected final GfaMMU memory;

  public IntUMem8(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.offset = offset;
    this.memory = memory;
  }

  public int evaluation() {
    return 0x000000ff & memory.directLoadByte(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }
  
}
