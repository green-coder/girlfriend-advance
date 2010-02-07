package gfa.debug;

import java.awt.*;
import java.awt.event.*;
import gfa.memory.*;
import gfa.util.*;

public class InputFrame
  extends Frame
  implements ItemListener
{
  private Checkbox upPad;
  private Checkbox downPad;
  private Checkbox leftPad;
  private Checkbox rightPad;
  private Checkbox selectPad;
  private Checkbox startPad;
  private Checkbox aPad;
  private Checkbox bPad;
  private Checkbox lPad;
  private Checkbox rPad;

  private IORegisterSpace_8_16_32 ioMem;

  public InputFrame(GfaMMU memory)
  {
    super("Input Frame");
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(0x04);

    setLayout(new FlowLayout());

    Panel panel = new Panel();
    panel.setLayout(new GridLayout(10, 1));

    upPad     = new Checkbox("Up");
    downPad   = new Checkbox("Down");
    leftPad   = new Checkbox("Left");
    rightPad  = new Checkbox("Right");
    selectPad = new Checkbox("Select");
    startPad  = new Checkbox("Start");
    aPad      = new Checkbox("A");
    bPad      = new Checkbox("B");
    lPad      = new Checkbox("L");
    rPad      = new Checkbox("R");

    upPad.addItemListener(this);
    downPad.addItemListener(this);
    leftPad.addItemListener(this);
    rightPad.addItemListener(this);
    selectPad.addItemListener(this);
    startPad.addItemListener(this);
    aPad.addItemListener(this);
    bPad.addItemListener(this);
    lPad.addItemListener(this);
    rPad.addItemListener(this);

    panel.add(upPad);
    panel.add(downPad);
    panel.add(leftPad);
    panel.add(rightPad);
    panel.add(selectPad);
    panel.add(startPad);
    panel.add(aPad);
    panel.add(bPad);
    panel.add(lPad);
    panel.add(rPad);
    add(panel);

    setSize(100, 300);
    setLocation(480, 100);
    show();
  }

  public void itemStateChanged(ItemEvent e)
  {
    short keys = (short)
     ~((aPad.getState() ?      0x0001 : 0) |
       (bPad.getState() ?      0x0002 : 0) |
       (selectPad.getState() ? 0x0004 : 0) |
       (startPad.getState() ?  0x0008 : 0) |
       (rightPad.getState() ?  0x0010 : 0) |
       (leftPad.getState() ?   0x0020 : 0) |
       (upPad.getState() ?     0x0040 : 0) |
       (downPad.getState() ?   0x0080 : 0) |
       (rPad.getState() ?      0x0100 : 0) |
       (lPad.getState() ?      0x0200 : 0));
    System.out.println("keys = " + Hex.toString(keys));
    ioMem.setReg16(0x0130, keys);
  }

}
