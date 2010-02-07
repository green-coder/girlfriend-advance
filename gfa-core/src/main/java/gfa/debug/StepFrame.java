package gfa.debug;

import gfa.cpu.*;
import gfa.memory.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class StepFrame
  extends Frame
  implements ActionListener, Runnable
{
  private Button resetButton;
  private Button stepButton;
  private Button nextButton;
  private Button breakButton;
  private TextField breakField;
  private Button runButton;
  private Button stopButton;

  private Arm7Tdmi cpu;
  private GfaMMU mem;
  private Vector cpuStateChangeListeners;

  public StepFrame(Arm7Tdmi cpu, GfaMMU memory)
  {
    super("Step Frame");
    this.cpu = cpu;
    mem = memory;

    cpuStateChangeListeners = new Vector();

    resetButton = new Button("Reset");
    stepButton = new Button("Step");
    nextButton = new Button("Next");
    breakButton = new Button("Break");
    breakField = new TextField("08000384");
    runButton = new Button("Run");
    stopButton = new Button("Stop");

    setLayout(new FlowLayout());

    stepButton.addActionListener(this);
    resetButton.addActionListener(this);
    nextButton.addActionListener(this);
    breakButton.addActionListener(this);
    runButton.addActionListener(this);
    stopButton.addActionListener(this);

    add(resetButton);
    add(stepButton);
    add(nextButton);
    add(breakButton);
    add(breakField);
    add(runButton);
    add(stopButton);

    setSize(220, 100);
    setLocation(200, 0);
    show();
  }

  public void actionPerformed(ActionEvent e)
  {
    Object obj = e.getSource();

    if (launcher != null)
    {
      if (obj == stopButton)
      {
        wantToStop = true;
        try {launcher.join();}
        catch(InterruptedException e2) {}
      }
    }
    else
    {
      if (obj == resetButton)
      {
	cpu.reset();
        mem.reloadRom();
	broadcastCpuStateChangeEvent();
      }
      else if (obj == stepButton)
      {
	cpu.step();
	broadcastCpuStateChangeEvent();
      }
      else if (obj != stopButton)
      {
        launcher = new Thread(this);
        command = obj;
        launcher.start();
      }
    }

  }

  protected boolean wantToStop;
  protected Object command = null;
  protected Thread launcher = null;

  public synchronized void run()
  {
    try
    {
      wantToStop = false;
      int offset = 0;

      if (command == breakButton)
        offset = Integer.parseInt(breakField.getText(), 16);
      else if (command == nextButton)
        offset = cpu.PC.get() + cpu.currentInstructionSize();
      else if (command == runButton)
        offset = -1;
      else
      {
        launcher = null;
        return;
      }

      do
      {
	cpu.step();
      } while ((!wantToStop) && (cpu.PC.get() != offset));

      broadcastCpuStateChangeEvent();
    }
    catch (NumberFormatException nfe) {}

    launcher = null;
  }

  public void addCpuStateChangeListener(CpuStateChangeListener listener)
  {
    cpuStateChangeListeners.addElement(listener);
  }
    
  public void broadcastCpuStateChangeEvent()
  {
    for (int i = 0; i < cpuStateChangeListeners.size(); i++)
      ((CpuStateChangeListener) cpuStateChangeListeners.elementAt(i)).cpuStateChanged();
  }
}
