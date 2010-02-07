package gfa.ui.action;

import gfa.*;
import gfa.ui.*;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ExitAction
    extends InternationalAction
{
    public ExitAction(UserInterface ui, GirlfriendAdvance gfa)
    {
	super(ui, "ExitAction");
    }
    
    public void actionPerformed(ActionEvent event)
    {
	System.exit(0);
    }
    
    public void gfaStatusChanged(int status)
    {
    }
}
