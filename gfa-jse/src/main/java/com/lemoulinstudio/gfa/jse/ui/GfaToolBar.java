package com.lemoulinstudio.gfa.jse.ui;

import com.lemoulinstudio.gfa.core.util.Hex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToolBar;

public class GfaToolBar extends JToolBar
        implements ActionListener {

  protected UserInterface ui;
  protected JButton watchMemButton;
  protected JTextField memPosTextField;

  public GfaToolBar(UserInterface ui) {
    this.ui = ui;

    add(new ToolBarButton(ui.resetAction));
    add(new ToolBarButton(ui.stopAction));
    add(new ToolBarButton(ui.runAction));
    addSeparator();
    add(new ToolBarButton(ui.undoAction));
    add(new ToolBarButton(ui.stepAction));
    add(new ToolBarButton(ui.nextAction));
    addSeparator();
    add(new ToolBarButton(ui.screenShotAction));
    addSeparator();
    watchMemButton = new JButton("Watch");
    watchMemButton.addActionListener(this);
    add(watchMemButton);
    memPosTextField = new JTextField("0x03007ff8");
    add(memPosTextField);
  }

  public void actionPerformed(ActionEvent event) {
    Object source = event.getSource();
    if (source == watchMemButton) {
      try {
        int pos = Long.decode(memPosTextField.getText()).intValue();
        System.out.println(Hex.toString(ui.gfa.getMemory().loadWord(pos)));
      }
      catch (NumberFormatException e) {
        System.out.println("Bad number Format : " + e);
      }
    }
  }

}
