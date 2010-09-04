package com.lemoulinstudio.gfa.nb.breakpoint;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class IntSMem8 extends IntExpr {

  final protected IntExpr offset;
  final protected GfaMMU memory;

  public IntSMem8(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.offset = offset;
    this.memory = memory;
  }

  public int evaluation() {
    return memory.directLoadByte(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }
  
}
