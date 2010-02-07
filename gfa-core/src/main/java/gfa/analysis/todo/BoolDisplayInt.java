package gfa.analysis;

import gfa.util.Hex;

public class BoolDisplayInt
  implements BoolExpr
{
  protected String name;
  protected IntExpr expr;

  public BoolDisplayInt(String name, IntExpr expr)
  {
    this.name = name;
    this.expr = expr;
  }

  public boolean evaluation()
  {
    System.out.println(name + " = " + Hex.toString(expr.evaluation()));
    return false;
  }

  public void setEvalChangeListener(EvalChangeListener obj)
  {
    expr.setEvalChangeListener(obj);
  }
}
