package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.DebugBackable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DebugBackAction implements ActionListener {

  private DebugBackable debugBackable;

  public DebugBackAction(DebugBackable debugBackable) {
    this.debugBackable = debugBackable;
  }

  public void actionPerformed(ActionEvent e) {
    debugBackable.debugBack();
  }

}
