package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.jse.GirlfriendAdvance;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
import java.awt.event.ActionEvent;

public class ExitAction extends InternationalAction {

  public ExitAction(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "ExitAction");
  }

  public void actionPerformed(ActionEvent event) {
    System.exit(0);
  }

  public void gfaStatusChanged(int status) {
  }
  
}
