package com.lemoulinstudio.gfa.analysis;

public class IntOr extends IntExpr {

  protected IntExpr expr1;
  protected IntExpr expr2;

  public IntOr(IntExpr expr1, IntExpr expr2) {
    super(new ScmExpr[]{expr1, expr2});
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public int evaluation() {
    return expr1.evaluation() | expr2.evaluation();
  }
}
