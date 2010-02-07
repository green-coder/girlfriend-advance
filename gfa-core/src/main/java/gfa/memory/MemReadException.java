package gfa.memory;

import gfa.util.Hex;

public class MemReadException
  extends MemAccessException
{
  private int offset;

  public MemReadException(String memoryName,
                          String methodName,
                          int offset)
  {
    super(memoryName, methodName);
    this.offset = offset;
  }

  public int getOffset()
  {
    return offset;
  }

  public String toString()
  {
    return "Lecture a l'adresse " +
           Hex.toString(getOffset()) +
           " sur la memoire " +
           getMemoryName() +
           " dans la methode " +
           getMethodName();
  }

}
