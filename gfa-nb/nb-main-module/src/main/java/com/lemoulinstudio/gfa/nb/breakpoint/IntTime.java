package com.lemoulinstudio.gfa.nb.breakpoint;

import com.lemoulinstudio.gfa.core.time.Time;

public class IntTime extends IntExpr {

  protected Time time;

  public IntTime(Time t) {
    time = t;
  }

  public int evaluation() {
    long t = time.getTime();
    if (t >= 0x100000000L) {
      System.out.println("IntTime.evaluation() : Depassement de temps !");
    }
    return (int) t;
  }

  @Override
  public boolean isConstant() {
    return false;
  }
}
