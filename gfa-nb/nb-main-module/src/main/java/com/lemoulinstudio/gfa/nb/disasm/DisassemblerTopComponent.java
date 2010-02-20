package com.lemoulinstudio.gfa.nb.disasm;

import com.lemoulinstudio.gfa.nb.GfaContext;
import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;
import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.StoppedState;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JViewport;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
public final class DisassemblerTopComponent extends TopComponent {

  public enum MemoryBank {

    BiosRom    (0x00, "Bios Rom"),
    ExternalRam(0x02, "External Ram"),
    WorkRam    (0x03, "Work Ram"),
    IORegisters(0x04, "IO Registers"),
    PaletteRam (0x05, "Palette Ram"),
    VideoRam   (0x06, "Video Ram"),
    OAMRam     (0x07, "OAM Ram"),
    GamepackRom(0x08, "Gamepack Rom"),
    CartRam    (0x0e, "Cart Ram");

    public int bankIndex;
    public String name;

    MemoryBank(int bankIndex, String name) {
      this.bankIndex = bankIndex;
      this.name = name;
    }

    public int getBankIndex() {
      return bankIndex;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return getName();
    }

  }
  public enum TrackingPolicy {
    None,
    Centered,
    Window;
  }
  
  private static DisassemblerTopComponent instance;
  /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
  private static final String PREFERRED_ID = "DisassemblerTopComponent";

  private DisassemblerTableModel tableModel;
  private ComboBoxModel memoryBankComboBoxModel;
  private ComboBoxModel executionTrackingComboBoxModel;
  private TrackingPolicy trackingPolicy;

  private Lookup.Result stoppedStateResult;

  private DisassemblerTopComponent() {
    tableModel = new DisassemblerTableModel();

    memoryBankComboBoxModel = new DefaultComboBoxModel(MemoryBank.values());
    executionTrackingComboBoxModel = new DefaultComboBoxModel(TrackingPolicy.values());

    initComponents();

    memoryBankComboBox.setSelectedItem(MemoryBank.GamepackRom);
    executionTrackingComboBox.setSelectedItem(TrackingPolicy.Window);
    
    setName(NbBundle.getMessage(DisassemblerTopComponent.class, "CTL_DisassemblerTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

    stoppedStateResult = GfaContext.getLookup().lookupResult(StoppedState.class);
    stoppedStateResult.addLookupListener(new LookupListener() {
      public void resultChanged(LookupEvent ev) {
        onEvent(GfaContext.getLookup().lookup(StoppedState.class));
      }
    });
    onEvent(GfaContext.getLookup().lookup(StoppedState.class));
  }

  private void onEvent(StoppedState stoppedState) {
    if (stoppedState == null) {
       tableModel.setGfaDevice(null);
    }
    else {
      tableModel.setGfaDevice(stoppedState.getRomDataObject().getGfaDevice());
      trackInstructionPointer();
    }
  }

  public void trackInstructionPointer() {
    trackInstructionPointer(trackingPolicy);
  }

  public void trackInstructionPointer(TrackingPolicy trackingPolicy) {
    int yRow = -1;
    int rowHeight;

    switch (trackingPolicy) {
      case None: {
        yRow = tableModel.getPcRow();
      }
      break;

      case Centered: {
        tableModel.viewCurrentlyExecutedMemoryBank();
        yRow = tableModel.getPcRow();
        JViewport viewport = scrollPane.getViewport();
        Rectangle rect = viewport.getViewRect();
        rowHeight = table.getRowHeight();
        int y = yRow * rowHeight - rect.height / 2;
        if (y < 0) y = 0;
        viewport.setViewPosition(new Point(0, y));
      }
      break;

      case Window: {
        tableModel.viewCurrentlyExecutedMemoryBank();
        yRow = tableModel.getPcRow();
        JViewport viewport = scrollPane.getViewport();
        Rectangle rect = viewport.getViewRect();
        rowHeight = table.getRowHeight();
        if (!rect.contains(0, yRow * rowHeight, 1, rowHeight))
          viewport.setViewPosition(new Point(0, yRow * rowHeight));
      }
      break;
    }

    // Select the current instruction row.
    if (yRow >= 0) table.setRowSelectionInterval(yRow, yRow);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    scrollPane = new javax.swing.JScrollPane();
    scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    table = new javax.swing.JTable();
    memoryBankComboBox = new javax.swing.JComboBox();
    executionTrackingComboBox = new javax.swing.JComboBox();
    javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel2 = new javax.swing.JLabel();

    table.setModel(tableModel);
    table.setSelectionBackground(java.awt.Color.pink);
    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    scrollPane.setViewportView(table);

    memoryBankComboBox.setModel(memoryBankComboBoxModel);
    memoryBankComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        memoryBankComboBoxActionPerformed(evt);
      }
    });

    executionTrackingComboBox.setModel(executionTrackingComboBoxModel);
    executionTrackingComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        executionTrackingComboBoxActionPerformed(evt);
      }
    });

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DisassemblerTopComponent.class, "DisassemblerTopComponent.jLabel1.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(DisassemblerTopComponent.class, "DisassemblerTopComponent.jLabel2.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(memoryBankComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(executionTrackingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(memoryBankComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2)
          .addComponent(executionTrackingComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void memoryBankComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memoryBankComboBoxActionPerformed
    tableModel.setViewedMemoryBank(((MemoryBank) memoryBankComboBoxModel.getSelectedItem()).bankIndex);
  }//GEN-LAST:event_memoryBankComboBoxActionPerformed

  private void executionTrackingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionTrackingComboBoxActionPerformed
    trackingPolicy = (TrackingPolicy) executionTrackingComboBoxModel.getSelectedItem();
  }//GEN-LAST:event_executionTrackingComboBoxActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox executionTrackingComboBox;
  private javax.swing.JComboBox memoryBankComboBox;
  private javax.swing.JScrollPane scrollPane;
  private javax.swing.JTable table;
  // End of variables declaration//GEN-END:variables
  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized DisassemblerTopComponent getDefault() {
    if (instance == null) {
      instance = new DisassemblerTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the DisassemblerTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized DisassemblerTopComponent findInstance() {
    TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      Logger.getLogger(DisassemblerTopComponent.class.getName()).warning(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof DisassemblerTopComponent) {
      return (DisassemblerTopComponent) win;
    }
    Logger.getLogger(DisassemblerTopComponent.class.getName()).warning(
            "There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
    return getDefault();
  }

  @Override
  public int getPersistenceType() {
    return TopComponent.PERSISTENCE_NEVER;
  }

  @Override
  public void componentOpened() {
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed() {
    // TODO add custom code on component closing
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }

}