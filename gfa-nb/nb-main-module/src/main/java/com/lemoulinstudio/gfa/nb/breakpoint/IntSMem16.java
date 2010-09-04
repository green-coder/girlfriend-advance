package com.lemoulinstudio.gfa.nb.breakpoint;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class IntSMem16 extends IntExpr {

  final protected IntExpr offset;
  final protected GfaMMU memory;

  public IntSMem16(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.offset = offset;
    this.memory = memory;
  }

  public int evaluation() {
    return memory.directLoadHalfWord(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }

}
