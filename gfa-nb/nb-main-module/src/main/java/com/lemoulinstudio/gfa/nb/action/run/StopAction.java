package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.Stoppable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopAction implements ActionListener {

  private Stoppable stoppable;

  public StopAction(Stoppable stoppable) {
    this.stoppable = stoppable;
  }

  public void actionPerformed(ActionEvent e) {
    stoppable.stop();
  }

}
