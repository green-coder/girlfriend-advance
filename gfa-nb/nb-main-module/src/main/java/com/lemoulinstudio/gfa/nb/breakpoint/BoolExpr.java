package com.lemoulinstudio.gfa.nb.breakpoint;

public abstract class BoolExpr extends ScmExpr {

  public BoolExpr() {
    super();
  }

  public BoolExpr(ScmExpr[] children) {
    super(children);
  }

  abstract public boolean evaluation();

}
