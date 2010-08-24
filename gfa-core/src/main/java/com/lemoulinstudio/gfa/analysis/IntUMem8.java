package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.MemoryInterface;

public class IntUMem8 extends IntExpr {

  final protected IntExpr offset;
  final protected MemoryInterface mem;

  public IntUMem8(IntExpr offset, MemoryInterface mem) {
    super(new ScmExpr[]{offset});
    this.offset = offset;
    this.mem = mem;
  }

  public int evaluation() {
    return 0x000000ff & mem.loadByte(offset.evaluation());
  }

  public boolean isConstant() {
    return false;
  }
}
