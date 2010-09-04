package com.lemoulinstudio.gfa.nb.breakpoint;

public class BoolAnd extends BoolExpr {

  protected BoolExpr expr1;
  protected BoolExpr expr2;

  public BoolAnd(BoolExpr expr1, BoolExpr expr2) {
    super(new ScmExpr[] {expr1, expr2});
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public boolean evaluation() {
    boolean b1 = expr1.evaluation();
    boolean b2 = expr2.evaluation();
    return b1 && b2;
  }

}
