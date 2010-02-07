package gfa.ui;

import gfa.GirlfriendAdvance;
import gfa.memory.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InputPanel
  extends JPanel
  implements ItemListener
{
  private JCheckBox upPad;
  private JCheckBox downPad;
  private JCheckBox leftPad;
  private JCheckBox rightPad;
  private JCheckBox selectPad;
  private JCheckBox startPad;
  private JCheckBox aPad;
  private JCheckBox bPad;
  private JCheckBox lPad;
  private JCheckBox rPad;

  private IORegisterSpace_8_16_32 ioMem;

  public InputPanel(GirlfriendAdvance gfa)
  {
    super(new GridLayout(10, 1), false);
    ioMem = (IORegisterSpace_8_16_32) gfa.getMemory().getMemoryBank(0x04);
    
    upPad     = new JCheckBox("Up");
    downPad   = new JCheckBox("Down");
    leftPad   = new JCheckBox("Left");
    rightPad  = new JCheckBox("Right");
    selectPad = new JCheckBox("Select");
    startPad  = new JCheckBox("Start");
    aPad      = new JCheckBox("A");
    bPad      = new JCheckBox("B");
    lPad      = new JCheckBox("L");
    rPad      = new JCheckBox("R");

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

    add(upPad);
    add(downPad);
    add(leftPad);
    add(rightPad);
    add(selectPad);
    add(startPad);
    add(aPad);
    add(bPad);
    add(lPad);
    add(rPad);
  }

  public void itemStateChanged(ItemEvent e)
  {
    short keys = (short)
     ~((aPad.isSelected() ?      0x0001 : 0) |
       (bPad.isSelected() ?      0x0002 : 0) |
       (selectPad.isSelected() ? 0x0004 : 0) |
       (startPad.isSelected() ?  0x0008 : 0) |
       (rightPad.isSelected() ?  0x0010 : 0) |
       (leftPad.isSelected() ?   0x0020 : 0) |
       (upPad.isSelected() ?     0x0040 : 0) |
       (downPad.isSelected() ?   0x0080 : 0) |
       (rPad.isSelected() ?      0x0100 : 0) |
       (lPad.isSelected() ?      0x0200 : 0));
    ioMem.setReg16(0x0130, keys);
  }

}
