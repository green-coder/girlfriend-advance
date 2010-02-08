package gfa.ui;

import gfa.GirlfriendAdvance;
import gfa.cpu.Arm7Tdmi;
import gfa.cpu.ArmReg;
import gfa.util.Hex;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;

public class RegisterViewerPanel extends JScrollPane {

  public RegisterViewerPanel(UserInterface ui, GirlfriendAdvance gfa) {
    super();

    JTable registerTable = new JTable(new RegisterTableModel(ui, gfa));
    CpsrFlagsPanel flagsPanel = new CpsrFlagsPanel(ui, gfa);

    //JPanel panel = new JPanel();
    //panel.add(registerTable);
    //panel.add(flagsPanel);
    //setViewportView(panel);

    setViewportView(registerTable);
    getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
  }
}

class RegisterTableModel extends InternationalTableModel
        implements GfaStatusChangeListener, GfaStateChangeListener {

  protected Arm7Tdmi cpu;
  protected boolean enabled;

  public RegisterTableModel(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "RegisterTableModel");
    cpu = gfa.getCpu();
    enabled = false;
    columnName = new String[]{"", ""}; // {"Name", "Value"}
    ui.addGfaStatusChangeListener(this);
    ui.addGfaStateChangeListener(this);
  }

  public int getRowCount() {
    return 18;
  }
  protected final static String[] name = {
    "r0", "r1", "r2", "r3", "r4", "r5", "r6", "r7", "r8", "r9",
    "r10", "r11", "r12", "r13", "r14", "r15", "cpsr", "spsr"
  };

  public Object getValueAt(int row, int column) {
    if (column == 0)
      return name[row];
    else if (enabled) {
      try {
        return Hex.toString(cpu.getRegister(row).get());
      } catch (NullPointerException e) {
        return "---";
      }
    }
    else
      return "---";
  }

  public void gfaStateChanged() {
    fireTableDataChanged();
  }

  public void gfaStatusChanged(int status) {
    enabled = (status == STATUS_EXECUTION_STOPPED);
  }
}

class CpsrFlagsPanel extends JPanel
        implements GfaStatusChangeListener, GfaStateChangeListener {

  protected JCheckBox n = new JCheckBox("n");
  protected JCheckBox z = new JCheckBox("z");
  protected JCheckBox c = new JCheckBox("c");
  protected JCheckBox v = new JCheckBox("o");
  protected JCheckBox i = new JCheckBox("i");
  protected JCheckBox f = new JCheckBox("f");
  protected JCheckBox t = new JCheckBox("t");
  protected JTextField mode = new JTextField("---");
  protected ArmReg cpsr;
  protected boolean enabled;

  public CpsrFlagsPanel(UserInterface ui, GirlfriendAdvance gfa) {
    super();

    cpsr = gfa.getCpu().CPSR;
    enabled = false;
    ui.addGfaStatusChangeListener(this);
    ui.addGfaStateChangeListener(this);

    setLayout(new GridLayout(4, 2));
    mode.setEditable(false);

    add(n);
    add(z);
    add(c);
    add(v);
    add(i);
    add(f);
    add(t);
    add(mode);

    gfaStateChanged();
  }

  public void gfaStateChanged() {
    if (enabled) {
      n.setSelected((cpsr.get() & Arm7Tdmi.nFlagBit) != 0);
      z.setSelected((cpsr.get() & Arm7Tdmi.zFlagBit) != 0);
      c.setSelected((cpsr.get() & Arm7Tdmi.cFlagBit) != 0);
      v.setSelected((cpsr.get() & Arm7Tdmi.vFlagBit) != 0);
      i.setSelected((cpsr.get() & Arm7Tdmi.iFlagBit) != 0);
      f.setSelected((cpsr.get() & Arm7Tdmi.fFlagBit) != 0);
      t.setSelected((cpsr.get() & Arm7Tdmi.tFlagBit) != 0);

      switch (cpsr.get() & Arm7Tdmi.modeBitsMask) {
        case Arm7Tdmi.usrModeBits:
          mode.setText("usr");
          break;
        case Arm7Tdmi.fiqModeBits:
          mode.setText("fiq");
          break;
        case Arm7Tdmi.irqModeBits:
          mode.setText("irq");
          break;
        case Arm7Tdmi.svcModeBits:
          mode.setText("svc");
          break;
        case Arm7Tdmi.abtModeBits:
          mode.setText("abt");
          break;
        case Arm7Tdmi.undModeBits:
          mode.setText("und");
          break;
        case Arm7Tdmi.sysModeBits:
          mode.setText("sys");
          break;
        default:
          mode.setText("---");
      }
    } else {
      n.setSelected(false);
      z.setSelected(false);
      c.setSelected(false);
      v.setSelected(false);
      i.setSelected(false);
      f.setSelected(false);
      t.setSelected(false);
      mode.setText("---");
    }
  }

  public void gfaStatusChanged(int status) {
    enabled = (status == STATUS_EXECUTION_STOPPED);
  }
  
}
