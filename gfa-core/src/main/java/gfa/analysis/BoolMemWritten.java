package gfa.analysis;

import gfa.memory.GfaMMU;

public abstract class BoolMemWritten
    extends BoolExpr
    implements WriteMemoryListener
{
    protected IntExpr offset;
    protected GfaMMU memory;
    protected boolean hasBeenWritten;
    
    public BoolMemWritten(IntExpr off, GfaMMU mem)
    {
	super(new ScmExpr[] {off});
	memory = mem;
	offset = off;
	memory.addWriteMemoryListener(this);
	hasBeenWritten = false;
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
