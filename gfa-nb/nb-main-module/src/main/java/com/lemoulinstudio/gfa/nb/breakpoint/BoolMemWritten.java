package com.lemoulinstudio.gfa.nb.breakpoint;

import com.lemoulinstudio.gfa.core.memory.WriteMemoryListener;
import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public abstract class BoolMemWritten
        extends BoolExpr
        implements WriteMemoryListener {

  protected IntExpr offset;
  protected GfaMMU memory;
  protected boolean hasBeenWritten;

  public BoolMemWritten(IntExpr offset, GfaMMU memory) {
    super(new ScmExpr[] {offset});
    this.memory = memory;
    this.offset = offset;
    this.hasBeenWritten = false;

    if (memory != null)
      memory.addWriteMemoryListener(this);
  }

  public boolean evaluation() {
    return hasBeenWritten;
  }

  @Override
  public boolean isConstant() {
    return false;
  }

  @Override
  public void clearStatus() {
    hasBeenWritten = false;
    super.clearStatus();
  }
}
