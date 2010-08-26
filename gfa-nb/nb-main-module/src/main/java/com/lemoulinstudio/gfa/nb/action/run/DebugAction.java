package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.Debuggable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebugAction implements ActionListener {

  private Debuggable debuggable;

  public DebugAction(Debuggable debuggable) {
    this.debuggable = debuggable;
  }

  public void actionPerformed(ActionEvent e) {
    debuggable.debug();
  }

}
