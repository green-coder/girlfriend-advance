package gfa.ui.action;

import gfa.ui.UserInterface;

import java.awt.event.ActionEvent;

public class DoNothingAction
    extends InternationalAction
{
    public DoNothingAction(UserInterface ui, String key)
    {
	super(ui, key);
    }
    
    public void actionPerformed(ActionEvent event)
    {
    }
}
