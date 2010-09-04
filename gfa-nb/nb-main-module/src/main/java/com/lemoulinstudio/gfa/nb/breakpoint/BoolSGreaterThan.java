package com.lemoulinstudio.gfa.nb.breakpoint;

/**
 * Signed comparison.
 */
public class BoolSGreaterThan extends BoolExpr {

  protected IntExpr expr1;
  protected IntExpr expr2;

  public BoolSGreaterThan(IntExpr expr1, IntExpr expr2) {
    super(new ScmExpr[]{expr1, expr2});
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public boolean evaluation() {
    return expr1.evaluation() > expr2.evaluation();
  }
}
