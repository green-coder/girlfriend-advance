package gfa.analysis;

import gfa.memory.GfaMMU;

public class BoolMem32Written
    extends BoolMemWritten
{
    public BoolMem32Written(IntExpr off, GfaMMU mem)
    {
	super(off, mem);
    }
    
    public void storeByte(int off, byte val)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenWritten = true;
    }
    
    public void storeHalfWord(int off, short val)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenWritten = true;
    }
    
    public void storeWord(int off, int val)
    {
	if ((memory.getInternalOffset(off) & 0xfffffffc) ==
	    (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc))
	    hasBeenWritten = true;
    }
}
