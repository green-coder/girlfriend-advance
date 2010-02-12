package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.jse.ui.CodeViewer;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
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
