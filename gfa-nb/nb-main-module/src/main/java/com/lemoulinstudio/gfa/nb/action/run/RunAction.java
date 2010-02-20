package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.Runnable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunAction implements ActionListener {

  private Runnable runnable;

  public RunAction(Runnable runnable) {
    this.runnable = runnable;
  }

  public void actionPerformed(ActionEvent e) {
    runnable.run();
  }
  
}
