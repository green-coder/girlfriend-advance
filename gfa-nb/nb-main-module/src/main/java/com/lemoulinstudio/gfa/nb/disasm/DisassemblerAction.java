package com.lemoulinstudio.gfa.nb.disasm;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Disassembler component.
 */
public class DisassemblerAction extends AbstractAction {

  public DisassemblerAction() {
    super(NbBundle.getMessage(DisassemblerAction.class, "CTL_DisassemblerAction"));
    //putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(DisassemblerTopComponent.ICON_PATH, true)));
  }

  public void actionPerformed(ActionEvent evt) {
    TopComponent win = DisassemblerTopComponent.findInstance();
    win.open();
    win.requestActive();
  }

}
