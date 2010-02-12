package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.jse.ui.GfaStatusChangeListener;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
import java.awt.event.ActionEvent;

public class NextAction extends InternationalAction
        implements GfaStatusChangeListener, Runnable {

  protected GfaDevice gfa;
  protected UserInterface ui;

  public NextAction(UserInterface ui, GfaDevice gfa) {
    super(ui, "NextAction");
    this.gfa = gfa;
    this.ui = ui;
    ui.addGfaStatusChangeListener(this);
    setEnabled(false);
  }

  public synchronized void actionPerformed(ActionEvent event) {
    if (isEnabled()) {
      ui.fireGfaStatusChanged(STATUS_EXECUTION_RUNNING);
      Thread t = new Thread(this);
      t.setPriority(Thread.NORM_PRIORITY);
      t.start();
    }
  }

  public void run() {
    try {
      gfa.getCpu().stepOver();
    } catch (Exception e) {
      e.printStackTrace();
    }
    ui.fireGfaStatusChanged(STATUS_EXECUTION_STOPPED);
    ui.fireGfaStateChanged();
  }

  public void gfaStatusChanged(int status) {
    switch (status) {
      case STATUS_EXECUTION_STOPPED:
        setEnabled(true);
        break;
      case STATUS_NO_GAMEPAK_PLUGGED:
      case STATUS_EXECUTION_RUNNING:
        setEnabled(false);
        break;
      default:
    }
  }

}
