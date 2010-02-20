package com.lemoulinstudio.gfa.nb.disasm;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.util.Hex;
import javax.swing.table.AbstractTableModel;

public class DisassemblerTableModel extends AbstractTableModel {

  private GfaDevice gfaDevice;
  
  private String[] columnName;
  private int memorySetViewed;
  private boolean enabled;
  private boolean armState;

  public DisassemblerTableModel() {
    enabled = false;
    armState = false;
    memorySetViewed = 0x00;
    columnName = new String[] {"Offset", "Opcode", "Instruction"};
  }

  public void setGfaDevice(GfaDevice gfaDevice) {
    this.gfaDevice = gfaDevice;
    enabled = gfaDevice != null;
  }

  @Override
  public String getColumnName(int columnNumber) {
    return columnName[columnNumber];
  }

  public int getColumnCount() {
    return columnName.length;
  }

  protected int[] userComplientMap = {
    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
    0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x0e, 0x0e
  };

  public void setViewedMemoryBank(int memorySetToView) {
    memorySetToView &= 0x0f;
    if (memorySetViewed != userComplientMap[memorySetToView]) {
      memorySetViewed = userComplientMap[memorySetToView];
      fireTableDataChanged();
    }
  }

  public void viewCurrentlyExecutedMemoryBank() {
    setViewedMemoryBank(gfaDevice.getCpu().PC.get() >>> 24);
  }

  public int getPcRow() {
    int offset = gfaDevice.getCpu().PC.get();
    offset &= 0x0fffffff;
    if (memorySetViewed != userComplientMap[offset >>> 24])
      return -1;
    else {
      offset -= (userComplientMap[offset >>> 24] << 24);
      return offset >>> (armState ? 2 : 1);
    }
  }

  private final int[] memorySize = {
    0x00004000, // Bios ROM
    0x00000100, // Dummy RAM
    0x00040000, // External RAM
    0x00008000, // Work RAM
    0x00000400, // I/O Register Space
    0x00000400, // Palette RAM
    0x00018000, // Video RAM
    0x00000400, // OAM RAM
    0x02000000, // Game ROM Part 1
    0x00000000, // Game ROM Part 2
    0x00000000, // Game ROM Part 1
    0x00000000, // Game ROM Part 2
    0x00000000, // Game ROM Part 1
    0x00000000, // Game ROM Part 2
    0x00010000, // Cart RAM
    0x00000000, // Cart RAM
  };

  public int getRowCount() {
    return memorySize[memorySetViewed] >>> (armState ? 2 : 1);
  }

  public Object getValueAt(int row, int column) {
    if (armState) {
      int address = (memorySetViewed << 24) + (row << 2);
      switch (column) {
        // The offset
        case 0:
          return Hex.toString(address);

        // The opcode
        case 1:
          if (enabled) return Hex.toString(gfaDevice.getMemory().loadWord(address));
          else return "---";

        // The disassembled instruction
        case 2:
          if (enabled) return gfaDevice.getCpu().disassembleArmInstruction(address);
          else return "---";

        default: return null;
      }
    }
    else {
      int address = (memorySetViewed << 24) + (row << 1);
      switch (column) {
        // The offset
        case 0:
          return Hex.toString(address);
          
        // The opcode
        case 1:
          if (enabled) return Hex.toString(gfaDevice.getMemory().loadHalfWord(address));
          else return "---";
          
        // The disassembled instruction
        case 2:
          if (enabled) return gfaDevice.getCpu().disassembleThumbInstruction(address);
          else return "---";
        
        default: return null;
      }
    }
  }

}
