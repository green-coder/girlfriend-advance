package com.lemoulinstudio.gfa.analysis;

public abstract class ScmExpr {

  protected ScmExpr[] childs;

  public ScmExpr() {
    this(new ScmExpr[0]);
  }

  public ScmExpr(ScmExpr[] childs) {
    this.childs = childs;
  }

  public ScmExpr[] getChilds() {
    return childs;
  }

  /* By default, we supposes that all signetons are constant. */
  public boolean isConstant() {
    for (int i = 0; i < childs.length; i++) {
      if (!childs[i].isConstant()) {
        return false;
      }
    }

    return true;
  }

  public void clearStatus() {
    for (int i = 0; i < childs.length; i++) {
      childs[i].clearStatus();
    }
  }
}
