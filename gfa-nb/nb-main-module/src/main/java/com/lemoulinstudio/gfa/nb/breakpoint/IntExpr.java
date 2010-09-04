package com.lemoulinstudio.gfa.nb.breakpoint;

public abstract class IntExpr extends ScmExpr {

  public IntExpr() {
    super();
  }

  public IntExpr(ScmExpr[] childs) {
    super(childs);
  }

  abstract public int evaluation();
}
