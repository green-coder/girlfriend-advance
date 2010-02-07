package gfa.analysis;

import gfa.cpu.*;

public class BoolRegRead
  implements BoolExpr
{
  final protected ArmRegObserver reg;

  public IntReg(ArmReg reg)
  {
    this.reg = reg;
  }

  /**
   * Return true if and only if the register is read.
   */
  public boolean evaluation()
  {
    return false;
  }

  protected EvalChangeListener listener;

  public void setEvalChangeListener(EvalChangeListener obj)
  {
    listener = obj;
  }
}
