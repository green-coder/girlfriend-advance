package com.lemoulinstudio.gfa.analysis;

public class IntNeg extends IntExpr {

  protected IntExpr expr;

  public IntNeg(IntExpr expr) {
    super(new ScmExpr[]{expr});
    this.expr = expr;
  }

  public int evaluation() {
    return -expr.evaluation();
  }
}
