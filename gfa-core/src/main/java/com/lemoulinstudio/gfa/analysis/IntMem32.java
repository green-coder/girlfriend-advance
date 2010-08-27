package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class IntMem32 extends IntExpr {

  final protected IntExpr offset;
  final protected GfaMMU memory;

  public IntMem32(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.offset = offset;
    this.memory = memory;
  }

  public int evaluation() {
    return memory.directLoadWord(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }

}
