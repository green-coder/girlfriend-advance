package com.lemoulinstudio.gfa.nb.cpureg;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows CpuRegisters component.
 */
public class CpuRegistersAction extends AbstractAction {

  public CpuRegistersAction() {
    super(NbBundle.getMessage(CpuRegistersAction.class, "CTL_CpuRegistersAction"));
    //putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(CpuRegistersTopComponent.ICON_PATH, true)));
  }

  public void actionPerformed(ActionEvent evt) {
    TopComponent win = CpuRegistersTopComponent.findInstance();
    win.open();
    win.requestActive();
  }
}
