package gfa.ui.action;

import gfa.*;
import gfa.cpu.Arm7Tdmi;
import gfa.ui.*;
import gfa.analysis.*;

import java.awt.event.*;
import javax.swing.*;

public class BreakNextAction
    extends InternationalAction
    implements GfaStatusChangeListener,
	       Runnable
{
    protected GirlfriendAdvance gfa;
    protected UserInterface ui;
    
    public BreakNextAction(UserInterface ui, GirlfriendAdvance gfa)
    {
	super(ui, "BreakNextAction");
	this.gfa = gfa;
	this.ui = ui;
	ui.addGfaStatusChangeListener(this);
	setEnabled(false);
    }
    
    public synchronized void actionPerformed(ActionEvent event)
    {
	if (isEnabled())
	    {
		ui.fireGfaStatusChanged(STATUS_EXECUTION_RUNNING);
		Thread t = new Thread(this);
		t.setPriority(Thread.NORM_PRIORITY);
		t.start();
	    }
    }
    
    public void run()
    {
	try {
	    gfa.getMemory().setSpyModeEnabled(true);
	    gfa.getMemory().removeAllListener();
	    gfa.getCpu().removeAllRegListener();
	    gfa.getCpu().stopPlease(false);
	    BoolExpr breakpointExpression = ui.parser.parse(ui.breakCondTextField.getText());
	    Arm7Tdmi cpu = gfa.getCpu();
	    do {
		breakpointExpression.clearStatus();
		cpu.step();
	    } while (!breakpointExpression.evaluation() &&
		     !cpu.isStopPolitelyRequested());
	}
	catch (Exception e) {e.printStackTrace();}
	gfa.getMemory().setSpyModeEnabled(false);
	ui.fireGfaStatusChanged(STATUS_EXECUTION_STOPPED);
	ui.fireGfaStateChanged();
    }
    
    public void gfaStatusChanged(int status)
    {
	switch(status)
	    {
	    case STATUS_EXECUTION_STOPPED:
		setEnabled(true);
		break;
	    case STATUS_NO_GAMEPAK_PLUGGED:
	    case STATUS_EXECUTION_RUNNING:
		setEnabled(false);
		break;
	    default:
	    }
    }
}
