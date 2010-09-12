package com.lemoulinstudio.gfa.nb.breakpoint;

public class BoolNotEqualBoolBool extends BoolExpr {

  protected BoolExpr expr1;
  protected BoolExpr expr2;

  public BoolNotEqualBoolBool(BoolExpr expr1, BoolExpr expr2) {
    super(new ScmExpr[]{expr1, expr2});
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public boolean evaluation() {
    return (expr1.evaluation() != expr2.evaluation());
  }
}
