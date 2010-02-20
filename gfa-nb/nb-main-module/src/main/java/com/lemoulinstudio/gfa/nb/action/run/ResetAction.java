package com.lemoulinstudio.gfa.nb.action.run;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject.Resetable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetAction implements ActionListener {

  private Resetable resetable;

  public ResetAction(Resetable resetable) {
    this.resetable = resetable;
  }

  public void actionPerformed(ActionEvent e) {
    resetable.reset();
  }
  
}
