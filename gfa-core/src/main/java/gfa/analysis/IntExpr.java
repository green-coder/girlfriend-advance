package gfa.analysis;

public abstract class IntExpr
    extends ScmExpr
{
    public IntExpr()
    {
	super();
    }
    
    public IntExpr(ScmExpr[] childs)
    {
	super(childs);
    }
    
    abstract public int evaluation();
}
