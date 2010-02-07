package gfa.analysis;

public class BoolBeginBool
  implements BoolExpr
{
  protected BoolExpr expr1;
  protected BoolExpr expr2;

  public BoolBeginBool(BoolExpr expr1, BoolExpr expr2)
  {
    this.expr1 = expr1;
    this.expr2 = expr2;
  }

  public boolean evaluation()
  {
    expr1.evaluation();
    return expr2.evaluation();
  }

  public void setEvalChangeListener(EvalChangeListener obj)
  {
    expr1.setEvalChangeListener(obj);
    expr2.setEvalChangeListener(obj);
  }
}
