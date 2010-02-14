package com.lemoulinstudio.gfa.nb.disasm;

import com.lemoulinstudio.gfa.core.GfaDevice;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

@Deprecated // This class is not needed and will be deleted.
public class DisassemblerView extends JScrollPane {

  protected DisassemblerTableModel tableModel;
  protected JTable table;
  protected int trackingPolicy;

  public static final int NO_TRACKING = 0;
  public static final int CENTER_TRACKING = 1;
  public static final int WINDOW_TRACKING = 2;

  public DisassemblerView(/*UserInterface ui,*/ GfaDevice gfa) {
    super();

    tableModel = new DisassemblerTableModel();
    table = new JTable(tableModel);
    table.setSelectionBackground(Color.pink);

    setViewportView(table);
    getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
  }

  public void setMemorySetViewed(int memorySetToView) {
    tableModel.setMemorySetViewed(memorySetToView);
  }

  public void goHome() {
    goHome(false);
  }

  public void goHome(boolean force) {
    int tracking = trackingPolicy;
    if (force && (tracking == NO_TRACKING))
        tracking = CENTER_TRACKING;

    int yRow = -1;
    Rectangle rect;
    JViewport viewport;
    int rowHeight;

    switch (tracking) {
      case NO_TRACKING:
        yRow = tableModel.getPcRow();
        break;

      case CENTER_TRACKING:
        tableModel.goHome();
        yRow = tableModel.getPcRow();
        viewport = getViewport();
        rect = viewport.getViewRect();
        rowHeight = table.getRowHeight();
        int y = yRow * rowHeight - rect.height / 2;
        if (y < 0) y = 0;
        viewport.setViewPosition(new Point(0, y));
        break;

      case WINDOW_TRACKING:
        tableModel.goHome();
        yRow = tableModel.getPcRow();
        viewport = getViewport();
        rect = viewport.getViewRect();
        rowHeight = table.getRowHeight();
        if (!rect.contains(0, yRow * rowHeight, 1, rowHeight))
          viewport.setViewPosition(new Point(0, yRow * rowHeight));
        break;

      default:
    }

    // Select the current instruction row.
    if (yRow >= 0) table.setRowSelectionInterval(yRow, yRow);
  }

  public void setTrackingPolicy(int trackingPolicy) {
    this.trackingPolicy = trackingPolicy;
    goHome();
  }

}
