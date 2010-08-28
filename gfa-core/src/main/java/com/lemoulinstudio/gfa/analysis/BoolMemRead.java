package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.ReadMemoryListener;
import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public abstract class BoolMemRead
        extends BoolExpr
        implements ReadMemoryListener {

  protected IntExpr offset;
  protected GfaMMU memory;
  protected boolean hasBeenRead;

  public BoolMemRead(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.memory = memory;
    this.offset = offset;
    this.hasBeenRead = false;

    if (memory != null)
      memory.addReadMemoryListener(this);
  }

  public boolean evaluation() {
    return hasBeenRead;
  }

  @Override
  public boolean isConstant() {
    return false;
  }

  @Override
  public void clearStatus() {
    hasBeenRead = false;
    super.clearStatus();
  }
}
