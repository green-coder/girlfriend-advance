package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.jse.ui.GfaStatusChangeListener;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
import java.awt.event.ActionEvent;

public class StopAction extends InternationalAction
        implements GfaStatusChangeListener {

  protected GfaDevice gfa;
  protected UserInterface ui;

  public StopAction(UserInterface ui, GfaDevice gfa) {
    super(ui, "StopAction");
    this.gfa = gfa;
    this.ui = ui;
    ui.addGfaStatusChangeListener(this);
    setEnabled(false);
  }

  public void actionPerformed(ActionEvent event) {
    gfa.getCpu().stopPlease();
  }

  public void gfaStatusChanged(int status) {
    switch (status) {
      case STATUS_NO_GAMEPAK_PLUGGED:
      case STATUS_EXECUTION_STOPPED:
        setEnabled(false);
        break;
      case STATUS_EXECUTION_RUNNING:
        setEnabled(true);
        break;
      default:
    }
  }

}
