package gfa.ui;

import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class ToolBarButton extends JButton
    implements PropertyChangeListener {

  public ToolBarButton(Action action) {
    super((Icon) action.getValue(Action.SMALL_ICON));
    setMargin(new Insets(0, 0, 0, 0));
    addActionListener(action);

    // Choose and set the toolTip text.
    String toolTip = (String) action.getValue(Action.SHORT_DESCRIPTION);
    if (toolTip == null) toolTip = (String) action.getValue(Action.NAME);
    if (toolTip != null) setToolTipText(toolTip);

    // Subscribe interest to the state of the action.
    action.addPropertyChangeListener(this);
    // Synchronize with the current action state.
    setEnabled(action.isEnabled());
  }
 
  public void propertyChange(PropertyChangeEvent e) {
    String propertyName = e.getPropertyName();

    if (propertyName.equals(Action.SHORT_DESCRIPTION)) {
      String text = (String) e.getNewValue();
      setToolTipText(text);
    } else if (propertyName.equals("enabled")) {
      Boolean enabledState = (Boolean) e.getNewValue();
      setEnabled(enabledState.booleanValue());
      repaint();
    } else if (propertyName.equals(Action.SMALL_ICON)) {
      Icon icon = (Icon) e.getNewValue();
      setIcon(icon);
      invalidate();
      repaint();
    }
  }

}
