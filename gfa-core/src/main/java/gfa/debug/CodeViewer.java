package gfa.debug;

import java.awt.*;
import gfa.cpu.*;
import gfa.memory.*;
import gfa.util.*;

public class CodeViewer
  extends Frame
  implements CpuStateChangeListener
{
  private TextField tf;
  private Arm7Tdmi cpu;
  private GfaMMU mem;

  public CodeViewer(Arm7Tdmi cpu, GfaMMU memory)
  {
    this.cpu = cpu;
    mem = memory;
    setTitle("Code Viewer");
    setLayout(new FlowLayout());
    tf = new TextField("", 40);
    //tf.setEnabled(false);
    add(tf);
    cpuStateChanged();
    setSize(400, 100);
    setLocation(0, 450);
    show();
  }

  public void cpuStateChanged()
  {
    try
    {
      int offset = cpu.PC.get();
      if (cpu.isInArmState())
        tf.setText("PC = " + cpu.PC +
                   " Instr = " + Hex.toString(mem.loadWord(cpu.PC.get())));
      else
        tf.setText("PC = " + cpu.PC +
                   " Instr = 0x" + Hex.toString(mem.loadHalfWord(cpu.PC.get())));
    }
    catch (AbortException e)
    {
      tf.setText("Abort Exception");
    }
  }
}
