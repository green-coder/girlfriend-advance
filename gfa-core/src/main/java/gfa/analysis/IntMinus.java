package gfa.analysis;

public class IntMinus
    extends IntExpr
{
    protected IntExpr expr1;
    protected IntExpr expr2;
    
    public IntMinus(IntExpr expr1, IntExpr expr2)
    {
	super(new ScmExpr[] {expr1, expr2});
	this.expr1 = expr1;
	this.expr2 = expr2;
    }
    
    public int evaluation()
    {
	return expr1.evaluation() - expr2.evaluation();
    }
}
