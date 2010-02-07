package gfa.ui.action;

import gfa.*;
import gfa.ui.*;

import java.awt.event.*;
import javax.swing.*;

public class HomeDisasmAction
    extends InternationalAction
{
    protected CodeViewer codeViewer;
    
    public HomeDisasmAction(UserInterface ui, CodeViewer codeViewer)
    {
	super(ui, "HomeDisasmAction");
	this.codeViewer = codeViewer;
    }
    
    public void actionPerformed(ActionEvent event)
    {
	// Force the view to come back onto the current instruction.
	codeViewer.goHome(true);
    }
}
