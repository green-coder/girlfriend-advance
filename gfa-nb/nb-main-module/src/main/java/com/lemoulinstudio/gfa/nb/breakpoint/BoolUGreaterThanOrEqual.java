package com.lemoulinstudio.gfa.nb.breakpoint;

/**
 * Unsigned comparison or equal.
 */
public class BoolUGreaterThanOrEqual extends BoolExpr {

  protected IntExpr expr1;
  protected IntExpr expr2;

  public BoolUGreaterThanOrEqual(IntExpr expr1, IntExpr expr2) {
    super(new ScmExpr[]{expr1, expr2});
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public boolean evaluation() {
    long l1 = (((long) expr1.evaluation()) & 0xffffffffL);
    long l2 = (((long) expr2.evaluation()) & 0xffffffffL);
    return l1 >= l2;
  }
}
