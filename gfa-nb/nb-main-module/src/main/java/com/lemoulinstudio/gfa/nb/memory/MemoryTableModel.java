package com.lemoulinstudio.gfa.nb.memory;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.util.Hex;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author vincent
 */
public class MemoryTableModel extends AbstractTableModel {

  private static final String[] columnName = new String[] {"Offset", "Value"};

  private GfaDevice device;
  private int viewedAddress;
  private int nbBytes;

  public MemoryTableModel() {
    this.viewedAddress = 0;
    this.nbBytes = 4;
  }

  public void setDevice(GfaDevice device) {
    if (this.device != device) {
      this.device = device;

      fireTableDataChanged();
    }
  }

  public GfaDevice getDevice() {
    return device;
  }

  public void setViewedAddress(int viewedAddress) {
    if (this.viewedAddress != viewedAddress) {
      this.viewedAddress = viewedAddress;

      fireTableDataChanged();
    }
  }

  public int getViewedAddress() {
    return viewedAddress;
  }

  public void setNbBytes(int nbBytes) {
    if (this.nbBytes != nbBytes) {
      this.nbBytes = nbBytes;
      
      fireTableDataChanged();
    }
  }

  public int getNbBytes() {
    return nbBytes;
  }

  @Override
  public String getColumnName(int columnNumber) {
    return columnName[columnNumber];
  }

  public int getColumnCount() {
    return columnName.length;
  }
  
  public int getRowCount() {
    return 256;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if (device == null)
      return "";

    int startAddress = viewedAddress - (getRowCount() / 2) * nbBytes;
    if (startAddress < 0) startAddress = 0;
    int address = startAddress + rowIndex * nbBytes;

    switch (columnIndex) {
      // The offset
      case 0:
        return Hex.toString(address);

      // The opcode
      case 1:
        if (nbBytes == 1)
          return Hex.toString(device.getMemory().loadByte(address));
        if (nbBytes == 2)
          return Hex.toString(device.getMemory().loadHalfWord(address));
        if (nbBytes == 4)
          return Hex.toString(device.getMemory().loadWord(address));

        StringBuilder sb = new StringBuilder(nbBytes * 2);
        int nbWords = nbBytes / 4;
        for (int i = 0; i < nbWords; i++)
          sb.append(Hex.toString(device.getMemory().loadWord(address + i * 4)));
        return sb.toString();

      default:
        return null;

    }
  }

}
