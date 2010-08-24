package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class IntUMem16 extends IntExpr {

  final protected IntExpr offset;
  final protected MemoryInterface mem;

  public IntUMem16(IntExpr offset, MemoryInterface mem) {
    super(new ScmExpr[]{offset});
    this.offset = offset;
    this.mem = mem;
  }

  public int evaluation() {
    return 0x0000ffff & mem.loadHalfWord(offset.evaluation());
  }

  @Override
  public boolean isConstant() {
    return false;
  }
}
