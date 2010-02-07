package gfa.analysis;

import gfa.cpu.ArmReg;

public class IntReg
    extends IntExpr
{
    protected ArmReg reg;
    
    public IntReg(ArmReg reg)
    {
	this.reg = reg;
    }
    
    public int evaluation()
    {
	return reg.get();
    }
    
    public boolean isConstant()
    {
	return false;
    }
}
