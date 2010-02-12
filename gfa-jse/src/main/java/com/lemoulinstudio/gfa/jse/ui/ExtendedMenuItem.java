package com.lemoulinstudio.gfa.jse.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class ExtendedMenuItem extends JMenuItem {

  public ExtendedMenuItem(Action action) {
    super(action);
  }

  /**
   * Factory method which sets the ActionEvent source's properties
   * according to values from the Action instance.  The properties
   * which are set may differ for subclasses.
   *
   * @param a the Action from which to get the properties, or null
   */
  protected void configurePropertiesFromAction(Action a) {
    if (a == null) {
      setText(null);
      setIcon(null);
      setEnabled(true);
      setAccelerator(null);
    }
    else {
      setText((String) a.getValue(Action.NAME));
      setIcon((Icon) a.getValue(Action.SMALL_ICON));
      setEnabled(a.isEnabled());
      Integer i = (Integer) a.getValue(Action.MNEMONIC_KEY);
      setMnemonic((i != null) ? i.intValue() : 0);
      setAccelerator((KeyStroke) a.getValue(Action.ACCELERATOR_KEY));
    }
  }

  /**
   * Factory method which creates the PropertyChangeListener
   * used to update the ActionEvent source as properties change on
   * its Action instance.
   */
  protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
    return new AbstractActionPropertyChangeListener(this, a) {

      public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        JMenuItem mi = (JMenuItem) getTarget();
        if (mi == null) {   //WeakRef GC'ed in 1.2
          Action action = (Action) e.getSource();
          action.removePropertyChangeListener(this);
        }
        else {
          if (propertyName.equals(Action.NAME)) {
            String text = (String) e.getNewValue();
            mi.setText(text);
            mi.repaint();
          }
          else if (propertyName.equals("enabled")) {
            Boolean enabledState = (Boolean) e.getNewValue();
            mi.setEnabled(enabledState.booleanValue());
            mi.repaint();
          }
          else if (propertyName.equals(Action.SMALL_ICON)) {
            Icon icon = (Icon) e.getNewValue();
            mi.setIcon(icon);
            mi.invalidate();
            mi.repaint();
          }
          else if (propertyName.equals(Action.MNEMONIC_KEY)) {
            Integer mn = (Integer) e.getNewValue();
            mi.setMnemonic((mn != null) ? mn.intValue() : 0);
            mi.invalidate();
            mi.repaint();
          }
          else if (propertyName.equals(Action.ACCELERATOR_KEY)) {
            KeyStroke accelerator = (KeyStroke) e.getNewValue();
            mi.setAccelerator(accelerator);
            mi.invalidate();
            mi.repaint();
          }
        }
      }
    };
  }
  
}
