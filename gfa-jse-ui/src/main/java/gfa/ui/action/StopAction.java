package gfa.ui.action;

import gfa.GirlfriendAdvance;
import gfa.ui.GfaStatusChangeListener;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class StopAction extends InternationalAction
        implements GfaStatusChangeListener {

  protected GirlfriendAdvance gfa;
  protected UserInterface ui;

  public StopAction(UserInterface ui, GirlfriendAdvance gfa) {
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
