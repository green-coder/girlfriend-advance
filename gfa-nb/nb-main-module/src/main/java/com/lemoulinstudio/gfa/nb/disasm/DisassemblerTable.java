package com.lemoulinstudio.gfa.nb.disasm;

import com.lemoulinstudio.gfa.core.cpu.ExecutionState;
import com.lemoulinstudio.gfa.core.GfaDevice;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 *
 * @author Vincent Cantin
 */
public class DisassemblerTable extends JTable {

  private DisassemblerTableModel tableModel;
  private JScrollPane parentScrollPane;
  private ProgramTrackingPolicy programTrackingPolicy;

  public DisassemblerTable() {
    this(new DisassemblerTableModel());
  }

  private DisassemblerTable(DisassemblerTableModel tableModel) {
    super(tableModel);
    this.tableModel = tableModel;
  }

  public void setParentScrollPane(JScrollPane parentScrollPane) {
    this.parentScrollPane = parentScrollPane;
  }

  public void setGfaDevice(GfaDevice device) {
    tableModel.setDevice(device);
  }

  public void setExecutionState(ExecutionState executionState) {
    tableModel.setExecutionState(executionState);
  }

  public void setMemoryBank(MemoryBank memoryBank) {
    tableModel.setMemoryBank(memoryBank);
  }

  public void setProgramTrackingPolicy(ProgramTrackingPolicy programTrackingPolicy) {
    this.programTrackingPolicy = programTrackingPolicy;
  }
  
  private int getPcRow() {
    int offset = tableModel.getDevice().getCpu().PC.get();
    MemoryBank memoryBank = MemoryBank.getPhysicalMemoryBank(offset >>> 24);

    if (memoryBank != tableModel.getMemoryBank())
      return -1;

    offset &= 0x00ffffff;
    offset %= memoryBank.getSize();
    int row = offset >>> tableModel.getExecutionState().getInstructionBitWidth();

    return row;
  }

  public void trackInstructionPointer() {
    int yRow = -1;

    ProgramTrackingPolicy ptp = (programTrackingPolicy == ProgramTrackingPolicy.None ?
      ProgramTrackingPolicy.Windowed : programTrackingPolicy);

    switch (ptp) {
      case None:
      break;

      case Centered: {
        tableModel.setMemoryBank(MemoryBank.getPhysicalMemoryBank(tableModel.getDevice().getCpu().PC.get() >>> 24));
        yRow = getPcRow();
        JViewport viewport = parentScrollPane.getViewport();
        Rectangle rect = viewport.getViewRect();
        int y = yRow * getRowHeight() - rect.height / 2;
        if (y < 0) y = 0;
        viewport.setViewSize(new Dimension(getWidth(), tableModel.getRowCount() * getRowHeight()));
        viewport.setViewPosition(new Point(0, y));
      }
      break;

      case Windowed: {
        tableModel.setMemoryBank(MemoryBank.getPhysicalMemoryBank(tableModel.getDevice().getCpu().PC.get() >>> 24));
        yRow = getPcRow();
        JViewport viewport = parentScrollPane.getViewport();
        Rectangle rect = viewport.getViewRect();
        int y = yRow * getRowHeight();
        viewport.setViewSize(new Dimension(getWidth(), tableModel.getRowCount() * getRowHeight()));
        if (!rect.contains(0, y, 1, getRowHeight()))
          viewport.setViewPosition(new Point(0, y));
      }
      break;
    }

    // Select the current instruction row.
    if (yRow >= 0) setRowSelectionInterval(yRow, yRow);
  }

}
