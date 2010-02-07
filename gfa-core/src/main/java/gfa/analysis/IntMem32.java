package gfa.analysis;

import gfa.memory.MemoryInterface;

public class IntMem32
    extends IntExpr
{
    final protected IntExpr offset;
    final protected MemoryInterface mem;
    
    public IntMem32(IntExpr offset, MemoryInterface mem)
    {
	super(new ScmExpr[] {offset});
	this.offset = offset;
	this.mem = mem;
    }
    
    public int evaluation()
    {
	return mem.loadWord(offset.evaluation());
    }
    
    public boolean isConstant()
    {
	return false;
    }
}
