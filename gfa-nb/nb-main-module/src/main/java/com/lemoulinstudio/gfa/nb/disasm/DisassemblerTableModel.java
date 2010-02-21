package com.lemoulinstudio.gfa.nb.disasm;

import com.lemoulinstudio.gfa.core.cpu.ExecutionState;
import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.Arm7Tdmi;
import com.lemoulinstudio.gfa.core.util.Hex;
import javax.swing.table.AbstractTableModel;

public class DisassemblerTableModel extends AbstractTableModel {

  private static final String[] columnName = new String[] {"Offset", "Opcode", "Instruction"};
  
  private GfaDevice device;
  private ExecutionState executionState;
  private MemoryBank memoryBank;

  public void setDevice(GfaDevice device) {
    if (this.device != device) {
      this.device = device;

      if (device == null) {
        executionState = null;
        memoryBank = null;
      }
      else {
        Arm7Tdmi cpu = device.getCpu();
        executionState = cpu.getExecutionState();
        memoryBank = MemoryBank.getPhysicalMemoryBank(cpu.PC.get() >>> 24);
      }
      
      fireTableDataChanged();
    }
  }

  public GfaDevice getDevice() {
    return device;
  }

  void setExecutionState(ExecutionState executionState) {
    if (this.executionState != executionState) {
      this.executionState = executionState;
      fireTableDataChanged();
    }
  }

  public ExecutionState getExecutionState() {
    return executionState;
  }

  public void setMemoryBank(MemoryBank memoryBank) {
    if (this.memoryBank != memoryBank) {
      this.memoryBank = memoryBank;
      fireTableDataChanged();
    }
  }

  public MemoryBank getMemoryBank() {
    return memoryBank;
  }

  @Override
  public String getColumnName(int columnNumber) {
    return columnName[columnNumber];
  }

  public int getColumnCount() {
    return columnName.length;
  }

  public int getRowCount() {
    if (device == null) return 0;
    else return memoryBank.getSize() >>> executionState.getInstructionBitWidth();
  }

  public Object getValueAt(int row, int column) {
    if (device == null)
      return "";

    int address = (memoryBank.getIndex() << 24) + (row << executionState.getInstructionBitWidth());

    switch (column) {
      // The offset
      case 0:
        return Hex.toString(address);

      // The opcode
      case 1:
        if (executionState == ExecutionState.Thumb)
          return Hex.toString(device.getMemory().loadHalfWord(address));
        else
          return Hex.toString(device.getMemory().loadWord(address));

      // The disassembled instruction
      case 2:
          return device.getCpu().disassembleInstruction(address, executionState);

      default:
        return null;
    }
  }

}
