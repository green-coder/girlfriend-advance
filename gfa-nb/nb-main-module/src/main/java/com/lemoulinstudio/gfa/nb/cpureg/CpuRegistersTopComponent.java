package com.lemoulinstudio.gfa.nb.cpureg;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.Arm7Tdmi;
import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.util.Hex;
import com.lemoulinstudio.gfa.nb.GfaContext;
import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.StoppedState;
import java.util.logging.Logger;
import javax.swing.JTextField;
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
final class CpuRegistersTopComponent extends TopComponent {

  private static CpuRegistersTopComponent instance;
  /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
  private static final String PREFERRED_ID = "CpuRegistersTopComponent";

  private GfaDevice device;
  private Lookup.Result stoppedStateResult;
  
  private CpuRegistersTopComponent() {
    initComponents();
    setName(NbBundle.getMessage(CpuRegistersTopComponent.class, "CTL_CpuRegistersTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

    stoppedStateResult = GfaContext.getLookup().lookupResult(StoppedState.class);
    stoppedStateResult.addLookupListener(new LookupListener() {
      public void resultChanged(LookupEvent ev) {
        onEvent(GfaContext.getLookup().lookup(StoppedState.class));
      }
    });
    onEvent(GfaContext.getLookup().lookup(StoppedState.class));
  }

  @Override
  public Lookup getLookup() {
    return GfaContext.getLookup();
  }

  private void onEvent(StoppedState stoppedState) {
    device = (stoppedState == null ? null : stoppedState.getRomDataObject().getGfaDevice());
    updateUIContent();
  }

  private void updateUIContent() {
    JTextField[] textFields = new JTextField[] {
        r0TextField, r1TextField, r2TextField, r3TextField,
        r4TextField, r5TextField, r6TextField, r7TextField,
        r8TextField, r9TextField, r10TextField, r11TextField,
        r12TextField, r13TextField, r14TextField, r15TextField,
        cpsrTextField, spsrTextField, cpuTimeTextField
    };

    if (device == null) {
      for (JTextField tf : textFields) {
        tf.setText("");
        tf.setEditable(false);
      }

    }
    else {
      Arm7Tdmi cpu = device.getCpu();
      
      for (int i = 0; i < 16; i++) {
        JTextField tf = textFields[i];
        tf.setText(Hex.toString(cpu.getRegister(i).get()));
        tf.setEditable(false);
      }

      cpsrTextField.setText(Hex.toString(cpu.CPSR.get()));
      ArmReg spsr = cpu.getRegister(17);
      spsrTextField.setText(spsr == null ? "" : Hex.toString(spsr.get()));
      modeTextField.setText(cpu.getModeName());
      codeTextField.setText(cpu.getExecutionState().name());

      nCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.nFlagBit) != 0);
      zCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.zFlagBit) != 0);
      cCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.cFlagBit) != 0);
      vCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.vFlagBit) != 0);
      iCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.iFlagBit) != 0);
      fCheckBox.setSelected((cpu.CPSR.get() & Arm7Tdmi.fFlagBit) != 0);

      cpuTimeTextField.setText("" + cpu.getTime().getTime());
    }
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    r0TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
    r8TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
    r1TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
    r9TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
    r2TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
    r10TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
    r3TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
    r11TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
    r4TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
    r12TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
    r5TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel12 = new javax.swing.JLabel();
    r13TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel13 = new javax.swing.JLabel();
    r6TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel14 = new javax.swing.JLabel();
    r14TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel15 = new javax.swing.JLabel();
    r7TextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel16 = new javax.swing.JLabel();
    r15TextField = new javax.swing.JTextField();
    cpsrTextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel17 = new javax.swing.JLabel();
    modeTextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel18 = new javax.swing.JLabel();
    spsrTextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel19 = new javax.swing.JLabel();
    codeTextField = new javax.swing.JTextField();
    javax.swing.JLabel jLabel20 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel28 = new javax.swing.JLabel();
    cCheckBox = new javax.swing.JCheckBox();
    javax.swing.JLabel jLabel29 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel30 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel31 = new javax.swing.JLabel();
    zCheckBox = new javax.swing.JCheckBox();
    nCheckBox = new javax.swing.JCheckBox();
    javax.swing.JLabel jLabel32 = new javax.swing.JLabel();
    javax.swing.JLabel jLabel33 = new javax.swing.JLabel();
    iCheckBox = new javax.swing.JCheckBox();
    javax.swing.JLabel jLabel34 = new javax.swing.JLabel();
    fCheckBox = new javax.swing.JCheckBox();
    vCheckBox = new javax.swing.JCheckBox();
    cpuTimeLabel = new javax.swing.JLabel();
    cpuTimeTextField = new javax.swing.JTextField();

    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel1.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel2.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel3.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel4.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel5.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel6.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel7.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel8.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel9.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel10.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel11.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel12.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel13.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel14.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel15.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel16, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel16.text")); // NOI18N

    cpsrTextField.setEnabled(false);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel17, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel17.text")); // NOI18N

    modeTextField.setEnabled(false);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel18, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel18.text")); // NOI18N

    spsrTextField.setEnabled(false);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel19, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel19.text")); // NOI18N

    codeTextField.setEnabled(false);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel20, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel20.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel28, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel28.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel29, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel29.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel30, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel30.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel31, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel31.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel32, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel32.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel33, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel33.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(jLabel34, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.jLabel34.text")); // NOI18N

    org.openide.awt.Mnemonics.setLocalizedText(cpuTimeLabel, org.openide.util.NbBundle.getMessage(CpuRegistersTopComponent.class, "CpuRegistersTopComponent.cpuTimeLabel.text")); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(modeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r4TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r0TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(codeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r6TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r5TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r2TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r7TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
              .addComponent(r3TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(cpsrTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r12TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r11TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r8TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(spsrTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r13TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r14TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r9TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r10TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
              .addComponent(r15TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel29)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel32)
              .addComponent(nCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel30)
              .addComponent(zCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel28)
              .addComponent(cCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel33)
              .addComponent(vCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel34)
              .addComponent(iCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabel31)
              .addComponent(fCheckBox)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(cpuTimeLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(cpuTimeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(r0TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2)
          .addComponent(r8TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(r1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4)
          .addComponent(r9TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel5)
          .addComponent(r2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6)
          .addComponent(r10TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(r3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel8)
          .addComponent(r11TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel9)
          .addComponent(r4TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel10)
          .addComponent(r12TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel11)
          .addComponent(r5TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel12)
          .addComponent(r13TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel13)
          .addComponent(r6TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel14)
          .addComponent(r14TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel15)
          .addComponent(r7TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel16)
          .addComponent(r15TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel18)
          .addComponent(modeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel17)
          .addComponent(cpsrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel20)
          .addComponent(codeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel19)
          .addComponent(spsrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel31)
          .addComponent(jLabel34)
          .addComponent(jLabel33)
          .addComponent(jLabel28)
          .addComponent(jLabel30)
          .addComponent(jLabel32))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(nCheckBox)
          .addComponent(jLabel29)
          .addComponent(fCheckBox)
          .addComponent(iCheckBox)
          .addComponent(vCheckBox)
          .addComponent(zCheckBox)
          .addComponent(cCheckBox))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(cpuTimeLabel)
          .addComponent(cpuTimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(22, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox cCheckBox;
  private javax.swing.JTextField codeTextField;
  private javax.swing.JTextField cpsrTextField;
  private javax.swing.JLabel cpuTimeLabel;
  private javax.swing.JTextField cpuTimeTextField;
  private javax.swing.JCheckBox fCheckBox;
  private javax.swing.JCheckBox iCheckBox;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JTextField modeTextField;
  private javax.swing.JCheckBox nCheckBox;
  private javax.swing.JTextField r0TextField;
  private javax.swing.JTextField r10TextField;
  private javax.swing.JTextField r11TextField;
  private javax.swing.JTextField r12TextField;
  private javax.swing.JTextField r13TextField;
  private javax.swing.JTextField r14TextField;
  private javax.swing.JTextField r15TextField;
  private javax.swing.JTextField r1TextField;
  private javax.swing.JTextField r2TextField;
  private javax.swing.JTextField r3TextField;
  private javax.swing.JTextField r4TextField;
  private javax.swing.JTextField r5TextField;
  private javax.swing.JTextField r6TextField;
  private javax.swing.JTextField r7TextField;
  private javax.swing.JTextField r8TextField;
  private javax.swing.JTextField r9TextField;
  private javax.swing.JTextField spsrTextField;
  private javax.swing.JCheckBox vCheckBox;
  private javax.swing.JCheckBox zCheckBox;
  // End of variables declaration//GEN-END:variables
  /**
   * Gets default instance. Do not use directly: reserved for *.settings files only,
   * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
   * To obtain the singleton instance, use {@link #findInstance}.
   */
  public static synchronized CpuRegistersTopComponent getDefault() {
    if (instance == null) {
      instance = new CpuRegistersTopComponent();
    }
    return instance;
  }

  /**
   * Obtain the CpuRegistersTopComponent instance. Never call {@link #getDefault} directly!
   */
  public static synchronized CpuRegistersTopComponent findInstance() {
    TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
    if (win == null) {
      Logger.getLogger(CpuRegistersTopComponent.class.getName()).warning(
              "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
      return getDefault();
    }
    if (win instanceof CpuRegistersTopComponent) {
      return (CpuRegistersTopComponent) win;
    }
    Logger.getLogger(CpuRegistersTopComponent.class.getName()).warning(
            "There seem to be multiple components with the '" + PREFERRED_ID
            + "' ID. That is a potential source of errors and unexpected behavior.");
    return getDefault();
  }

  @Override
  public int getPersistenceType() {
    return TopComponent.PERSISTENCE_ALWAYS;
  }

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }

}
