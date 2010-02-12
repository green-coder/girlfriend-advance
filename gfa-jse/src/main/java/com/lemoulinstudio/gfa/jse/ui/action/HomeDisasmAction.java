package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.jse.ui.CodeViewer;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
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
