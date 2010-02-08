package gfa.ui.action;

import gfa.ui.CodeViewer;
import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;

public class TrackingDisasmAction extends InternationalAction {

  protected CodeViewer codeViewer;
  protected int trackingPolicy;

  public TrackingDisasmAction(UserInterface ui, String key,
          CodeViewer codeViewer, int trackingPolicy) {
    super(ui, key + "TrackingDisasmAction");
    this.codeViewer = codeViewer;
    this.trackingPolicy = trackingPolicy;
  }

  public void actionPerformed(ActionEvent event) {
    codeViewer.setTrackingPolicy(trackingPolicy);
  }

}
