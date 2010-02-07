package gfa.analysis;

public abstract class BoolExpr
    extends ScmExpr
{
    public BoolExpr()
    {
	super();
    }
    
    public BoolExpr(ScmExpr[] childs)
    {
	super(childs);
    }
    
    abstract public boolean evaluation();
}
