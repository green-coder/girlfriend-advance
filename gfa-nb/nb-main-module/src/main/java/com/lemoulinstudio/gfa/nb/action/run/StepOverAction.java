package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.StepOverable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepOverAction implements ActionListener {

  private StepOverable stepOverable;

  public StepOverAction(StepOverable stepOverable) {
    this.stepOverable = stepOverable;
  }

  public void actionPerformed(ActionEvent e) {
    stepOverable.stepOver();
  }

}
