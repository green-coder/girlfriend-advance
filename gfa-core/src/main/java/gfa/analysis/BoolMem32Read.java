package gfa.analysis;

import gfa.memory.GfaMMU;

public class BoolMem32Read
    extends BoolMemRead
{
    public BoolMem32Read(IntExpr off, GfaMMU mem)
    {
	super(off, mem);
    }
    
    public void loadByte(int off)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenRead = true;
    }
    
    public void loadHalfWord(int off)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenRead = true;
    }
    
    public void loadWord(int off)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenRead = true;
    }
}
