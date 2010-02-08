package gfa.ui.action;

import gfa.ui.CodeViewer;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class HomeDisasmAction extends InternationalAction {

  protected CodeViewer codeViewer;

  public HomeDisasmAction(UserInterface ui, CodeViewer codeViewer) {
    super(ui, "HomeDisasmAction");
    this.codeViewer = codeViewer;
  }

  public void actionPerformed(ActionEvent event) {
    // Force the view to come back onto the current instruction.
    codeViewer.goHome(true);
  }
  
}
