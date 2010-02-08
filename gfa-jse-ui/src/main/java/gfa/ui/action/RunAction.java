package gfa.ui.action;

import gfa.GirlfriendAdvance;
import gfa.ui.GfaStatusChangeListener;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class RunAction extends InternationalAction
        implements GfaStatusChangeListener, Runnable {

  protected GirlfriendAdvance gfa;
  protected UserInterface ui;

  public RunAction(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "RunAction");
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
      gfa.getCpu().run();
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
