package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.Steppable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepAction implements ActionListener {

  private Steppable steppable;

  public StepAction(Steppable steppable) {
    this.steppable = steppable;
  }

  public void actionPerformed(ActionEvent e) {
    steppable.step();
  }

}
