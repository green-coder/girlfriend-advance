package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class IntUMem16 extends IntExpr {

  protected final IntExpr offset;
  protected final GfaMMU memory;

  public IntUMem16(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.offset = offset;
    this.memory = memory;
  }

  public int evaluation() {
    return 0x0000ffff & memory.directLoadHalfWord(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }
  
}
