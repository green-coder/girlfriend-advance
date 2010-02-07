package gfa.analysis;

public class BoolSetInt
  implements BoolExpr
{
  protected IntVar var;
  protected IntExpr expr;
  protected BoolExpr cond;

  public BoolSetInt(IntVar var, IntExpr expr, BoolExpr cond)
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
    expr.setEvalChangeListener(obj);
    cond.setEvalChangeListener(obj);
  }
}
