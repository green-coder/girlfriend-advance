package gfa.analysis;

import gfa.memory.GfaMMU;

public abstract class BoolMemRead
    extends BoolExpr
    implements ReadMemoryListener
{
    protected IntExpr offset;
    protected GfaMMU memory;
    protected boolean hasBeenRead;
    
    public BoolMemRead(IntExpr off, GfaMMU mem)
    {
	super(new ScmExpr[] {off});
	memory = mem;
	offset = off;
	memory.addReadMemoryListener(this);
	hasBeenRead = false;
    }
    
    public boolean evaluation()
    {
	return hasBeenRead;
    }
    
    public boolean isConstant()
    {
	return false;
    }
    
    public void clearStatus()
    {
	hasBeenRead = false;
	super.clearStatus();
    }
}
