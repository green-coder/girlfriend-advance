package com.lemoulinstudio.gfa.nb.action.run;

import java.awt.Component;
import org.openide.util.actions.Presenter;

/**
 *
 * @author vincent
 */
public class BreakpointToolbarPresenter implements Presenter.Toolbar {
  public Component getToolbarPresenter() {
    return BreakpointPanel.getInstance();
  }
}
