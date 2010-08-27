package com.lemoulinstudio.gfa.analysis;

public abstract class ScmExpr {

  protected ScmExpr[] children;

  public ScmExpr() {
    this(new ScmExpr[] {});
  }

  public ScmExpr(ScmExpr[] children) {
    this.children = children;
  }

  public ScmExpr[] getChildren() {
    return children;
  }

  /* By default, we supposes that all singletons are constant. */
  public boolean isConstant() {
    for (ScmExpr child : children)
      if (!child.isConstant())
        return false;

    return true;
  }

  public void clearStatus() {
    for (ScmExpr child : children)
      child.clearStatus();
  }
  
}
