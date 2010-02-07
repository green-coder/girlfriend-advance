package gfa.memory;

public class AbortException
  extends Exception
{
  public AbortException()
  {
    super();
  }

  public AbortException(String msg)
  {
    super(msg);
  }

  public String toString()
  {
    return super.toString() + " : " + getMessage();
  }
}
