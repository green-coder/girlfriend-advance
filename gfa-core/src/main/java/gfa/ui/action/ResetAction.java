package gfa.ui.action;

import gfa.GirlfriendAdvance;
import gfa.ui.GfaStatusChangeListener;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class ResetAction extends InternationalAction
        implements GfaStatusChangeListener {

  protected GirlfriendAdvance gfa;
  protected UserInterface ui;

  public ResetAction(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "ResetAction");
    this.gfa = gfa;
    this.ui = ui;
    ui.addGfaStatusChangeListener(this);
    setEnabled(false);
  }

  public void reset() {
    gfa.reset();
  }

  public void actionPerformed(ActionEvent event) {
    reset();
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
