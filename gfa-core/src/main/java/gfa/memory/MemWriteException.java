package gfa.memory;

import gfa.util.Hex;

public class MemWriteException
  extends MemAccessException
{
  private int offset;
  private int value;

  public MemWriteException(String memoryName,
                           String methodName,
                           int offset,
                           int value)
  {
    super(memoryName, methodName);
    this.offset = offset;
    this.value = value;
  }

  public int getOffset()
  {
    return offset;
  }

  public int getValue()
  {
    return value;
  }

  public String toString()
  {
    return "Ecriture de " +
           getValue() + 
           " a l'adresse " +
           Hex.toString(getOffset()) +
           " sur la memoire " +
           getMemoryName() +
           " dans la methode " +
           getMethodName();
  }

}
