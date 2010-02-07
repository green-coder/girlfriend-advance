package gfa.analysis;

public class IntVar
    extends IntExpr
{
    protected int val;
    
    public IntVar(int val)
    {
	this.val = val;
    }
    
    public int evaluation()
    {
	return val;
    }
    
    public void set(int i)
    {
	val = i;
    }
}
