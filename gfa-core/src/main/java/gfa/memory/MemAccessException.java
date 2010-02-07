package gfa.memory;

public class MemAccessException
  extends AbortException
{
  private String memoryName;
  private String methodName;

  public MemAccessException(String memoryName, String methodName)
  {
    super(memoryName + " " + methodName);
    this.memoryName = memoryName;
    this.methodName = methodName;
  }

  public String getMemoryName()
  {
    return memoryName;
  }

  public String getMethodName()
  {
    return methodName;
  }
}
