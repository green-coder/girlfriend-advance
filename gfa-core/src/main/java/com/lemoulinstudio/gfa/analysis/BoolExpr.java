package com.lemoulinstudio.gfa.analysis;

public abstract class BoolExpr extends ScmExpr {

  public BoolExpr() {
    super();
  }

  public BoolExpr(ScmExpr[] children) {
    super(children);
  }

  abstract public boolean evaluation();

}
