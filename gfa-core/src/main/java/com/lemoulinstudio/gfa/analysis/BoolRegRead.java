package com.lemoulinstudio.gfa.analysis;

public class BoolRegRead extends BoolExpr
        implements ReadRegListener {

  protected boolean hasBeenRead;

  public BoolRegRead(ArmRegObserver registerObserver) {
    super(new ScmExpr[]{});
    registerObserver.addReadRegListener(this);
    hasBeenRead = false;
  }

  public void notifyGetRequested() {
    hasBeenRead = true;
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
