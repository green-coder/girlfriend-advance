package gfa.util;

public class HexShort
{
    protected short n;
    
    public HexShort(short i)
    {
	n = i;
    }
    
    public String toString()
    {
	return Hex.toString(n);
    }
}
