package com.lemoulinstudio.gfa.nb.breakpoint;

public class IntConst extends IntExpr {

  protected int expr;

  public IntConst(int expr) {
    super();
    this.expr = expr;
  }

  public int evaluation() {
    return expr;
  }
}
