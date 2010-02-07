package gfa.debug;

import gfa.cpu.*;
import java.io.*;

public class DebugArmReg
  extends ArmReg
{
  private String name;
  private PrintStream out;

  public DebugArmReg(int value, String name, PrintStream out)
  {
    super(value);
    this.name = name;
    this.out = out;
  }

  public void set(int v)
  {
    out.println(name + "(" + val + ") <- " + v +
                " PC = " + Ref.cpu.PC);
    val = v;
  }

  public void set(ArmReg reg)
  {
    out.println(name + "(" + val + ") <- R? (" + reg.val + ")" +
                " PC = " + Ref.cpu.PC);
    val = reg.val;
  }

  public void add(int v)
  {
    out.println(name + "(" + val + ") <- " + name + " + " + v +
                " PC = " + Ref.cpu.PC);
    val += v;
  }

  public void sub(int v)
  {
    out.println(name + "(" + val + ") <- " + name + " - " + v +
                " PC = " + Ref.cpu.PC);
    val -= v;
  }

}
