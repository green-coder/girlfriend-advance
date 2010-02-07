package gfa.memory;

public abstract class MemoryManagementUnit
    implements MemoryInterface
{
  private String memoryName;

  public MemoryManagementUnit(String name)
  {
    memoryName = name;
  }

  public String getName()
  {
    return memoryName;
  }

  abstract public int getInternalOffset(int offset);
}
