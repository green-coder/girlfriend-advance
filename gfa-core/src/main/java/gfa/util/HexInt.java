package gfa.util;

public class HexInt
{
    protected int n;
    
    public HexInt(int i)
    {
	n = i;
    }
    
    public String toString()
    {
	return Hex.toString(n);
    }
}
