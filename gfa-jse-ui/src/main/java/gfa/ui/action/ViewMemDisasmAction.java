package gfa.ui.action;

import gfa.ui.CodeViewer;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class ViewMemDisasmAction extends InternationalAction {

  protected CodeViewer codeViewer;
  protected int bankNumber;
  
  protected static String[] resourceName = {
    "BiosRom",
    "DummyMem",
    "ExternalRam",
    "WorkRam",
    "IoReg",
    "PaletteRam",
    "VideoRam",
    "OamRam",
    "GamepakRom",
    "",
    "",
    "",
    "",
    "",
    "CartRam"
  };

  public ViewMemDisasmAction(UserInterface ui, CodeViewer codeViewer, int bankNumber) {
    super(ui, resourceName[bankNumber] + "DisasmAction");
    this.codeViewer = codeViewer;
    this.bankNumber = bankNumber;
  }

  public void actionPerformed(ActionEvent event) {
    codeViewer.setMemorySetViewed(bankNumber);
  }

}
