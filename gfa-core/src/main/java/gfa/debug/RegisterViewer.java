package gfa.debug;

import gfa.cpu.*;
import gfa.util.*;
import java.awt.*;

public class RegisterViewer
  extends Frame
  implements CpuStateChangeListener
{
  private CurrentRegisterObserver[] cro;
  private Arm7Tdmi cpu;

  public RegisterViewer(Arm7Tdmi cpu)
  {
    super("Register Viewer");
    this.cpu = cpu;

    setLayout(new FlowLayout());

    Panel pReg = new Panel();
    pReg.setLayout(new GridLayout(18, 2));

    cro = new CurrentRegisterObserver[18];
    TextField tf;

    for (int i = 0; i <= 15; i++)
    {
      tf = new TextField("", 10);
      //tf.setEnabled(false);
      cro[i] = new CurrentRegisterObserver(cpu, i, tf);
      pReg.add(new Label("R" + i));
      pReg.add(tf);
    }

    tf = new TextField("", 10);
    //tf.setEnabled(false);
    cro[16] = new CurrentRegisterObserver(cpu, 16, tf);
    pReg.add(new Label("CPSR"));
    pReg.add(tf);

    add(pReg);

    setSize(200, 450);
    //pack();
    show();
  }

  public void cpuStateChanged()
  {
    for (int i = 0; i <= 16; i++)
      cro[i].update();
  }

}

class CurrentRegisterObserver
{
  private int index;
  private TextField tf;
  private int oldValue;
  private Arm7Tdmi cpu;

  public CurrentRegisterObserver(Arm7Tdmi cpu, int index, TextField tf)
  {
    this.cpu = cpu;
    this.index = index;
    this.tf = tf;
    oldValue = cpu.getRegister(index).get();
    tf.setText(Hex.toString(oldValue));
  }

  public void update()
  {
    int newValue = cpu.getRegister(index).get();

    if (oldValue != newValue)
    {
      tf.setText(Hex.toString(newValue));
      oldValue = newValue;
    }
  }
}
