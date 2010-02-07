package gfa.analysis;

import gfa.memory.MemoryInterface;

public class IntSMem8
    extends IntExpr
{
    final protected IntExpr offset;
    final protected MemoryInterface mem;
    
    public IntSMem8(IntExpr offset, MemoryInterface mem)
    {
	super(new ScmExpr[] {offset});
	this.offset = offset;
	this.mem = mem;
    }
    
    public int evaluation()
    {
	return mem.loadByte(offset.evaluation());
    }
    
    public boolean isConstant()
    {
	return false;
    }
}
