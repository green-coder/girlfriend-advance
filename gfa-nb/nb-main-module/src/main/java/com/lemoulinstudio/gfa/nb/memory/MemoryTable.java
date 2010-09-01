package com.lemoulinstudio.gfa.nb.memory;

import com.lemoulinstudio.gfa.core.GfaDevice;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 *
 * @author vincent
 */
public class MemoryTable extends JTable {

  private MemoryTableModel model;
  private JScrollPane parentScrollPane;

  public MemoryTable() {
    this(new MemoryTableModel());
  }

  public MemoryTable(MemoryTableModel model) {
    super(model);
    this.model = model;
  }

  public void setParentScrollPane(JScrollPane parentScrollPane) {
    this.parentScrollPane = parentScrollPane;
  }

  public void setDevice(GfaDevice device) {
    model.setDevice(device);
  }

  public void setViewedAddress(int viewedAddress) {
    model.setViewedAddress(viewedAddress);
    centerView();
  }

  public void setNbBytes(int nbBytes) {
    model.setNbBytes(nbBytes);
    centerView();
  }

  public void centerView() {
    JViewport viewport = parentScrollPane.getViewport();
    Rectangle rect = viewport.getViewRect();

    int yRow = model.getViewedAddress() / model.getNbBytes();
    yRow = Math.min(yRow, model.getRowCount() / 2);

    int y = yRow * getRowHeight() - rect.height / 2;
    if (y < 0) y = 0;
    viewport.setViewSize(new Dimension(getWidth(), model.getRowCount() * getRowHeight()));
    viewport.setViewPosition(new Point(0, y));

    // Select the viewed row.
    if (yRow >= 0) setRowSelectionInterval(yRow, yRow);
  }

}
