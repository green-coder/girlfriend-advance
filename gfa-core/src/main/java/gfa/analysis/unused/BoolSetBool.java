package gfa.analysis;

public class BoolSetBool
  implements BoolExpr
{
  protected BoolVar var;
  protected BoolExpr expr;
  protected BoolExpr cond;

  public BoolSetBool(BoolVar var, BoolExpr expr, BoolExpr cond)
  {
    this.var = var;
    this.expr = expr;
    this.cond = cond;
  }

  public boolean evaluation()
  {
    boolean b = cond.evaluation();
    if (b) var.set(expr.evaluation());
    return b;
  }

  public void setEvalChangeListener(EvalChangeListener obj)
  {
    cond.setEvalChangeListener(obj);
  }
}
