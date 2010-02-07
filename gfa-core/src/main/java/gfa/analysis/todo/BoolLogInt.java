package gfa.analysis;

import java.io.*;

import gfa.util.Hex;

public class BoolLogInt
  implements BoolExpr
{
  protected String name;
  protected IntExpr expr;

  static public PrintStream debug;

  static
  {
      try {debug = new PrintStream(new FileOutputStream("debug.txt"));}
      catch (Exception e) {debug = System.out;}
  }


  public BoolLogInt(String name, IntExpr expr)
  {
    this.name = name;
    this.expr = expr;
  }

  public boolean evaluation()
  {
    debug.println(name + " = " + Hex.toString(expr.evaluation()));
    return false;
  }

  public void setEvalChangeListener(EvalChangeListener obj)
  {
    expr.setEvalChangeListener(obj);
  }
}
