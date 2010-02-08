package gfa.ui.action;

import gfa.GirlfriendAdvance;
import gfa.cpu.Arm7Tdmi;
import gfa.memory.GfaMMU;
import gfa.time.Time;
import gfa.ui.GfaStatusChangeListener;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class UndoAction extends InternationalAction
        implements GfaStatusChangeListener {

  protected GirlfriendAdvance gfa;
  protected UserInterface ui;

  public UndoAction(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "UndoAction");
    this.gfa = gfa;
    this.ui = ui;
    ui.addGfaStatusChangeListener(this);
    setEnabled(false);
  }

  public void actionPerformed(ActionEvent event) {
    GfaMMU mem = gfa.getMemory();
    Arm7Tdmi cpu = gfa.getCpu();
    Time time = gfa.getTime();
    long t = time.getTime();
    gfa.reset();

    while (time.getTime() < t - 4) {
      cpu.step();
    }

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
