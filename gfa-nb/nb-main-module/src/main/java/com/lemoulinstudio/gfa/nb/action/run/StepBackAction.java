package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.StepBackable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StepBackAction implements ActionListener {

  private StepBackable stepBackable;

  public StepBackAction(StepBackable stepBackable) {
    this.stepBackable = stepBackable;
  }

  public void actionPerformed(ActionEvent e) {
    stepBackable.stepBack();
  }

}
