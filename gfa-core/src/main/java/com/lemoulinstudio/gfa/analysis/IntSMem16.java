package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class IntSMem16 extends IntExpr {

  final protected IntExpr offset;
  final protected MemoryInterface mem;

  public IntSMem16(IntExpr offset, MemoryInterface mem) {
    super(new ScmExpr[]{offset});
    this.offset = offset;
    this.mem = mem;
  }

  public int evaluation() {
    return mem.loadHalfWord(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }
}
