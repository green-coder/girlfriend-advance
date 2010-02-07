package gfa.analysis;

public class BoolRegWritten
    extends BoolExpr
    implements WriteRegListener
{
    protected boolean hasBeenWritten;
    
    public BoolRegWritten(ArmRegObserver registerObserver)
    {
	super(new ScmExpr[] {});
	registerObserver.addWriteRegListener(this);
	hasBeenWritten = false;
    }
    
    public void notifySetRequested(int value)
    {
	hasBeenWritten = true;
    }
    
    public boolean evaluation()
    {
	return hasBeenWritten;
    }
    
    public boolean isConstant()
    {
	return false;
    }
    
    public void clearStatus()
    {
	hasBeenWritten = false;
	super.clearStatus();
    }
}
