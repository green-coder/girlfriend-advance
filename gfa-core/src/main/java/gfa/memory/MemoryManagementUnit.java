package gfa.memory;

public abstract class MemoryManagementUnit
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

  abstract public byte loadByte(int offset)
    throws MemAccessException;

  abstract public short loadHalfWord(int offset)
    throws MemAccessException;

  abstract public int loadWord(int offset)
    throws MemAccessException;

  abstract public void storeByte(int offset, byte value)
    throws MemAccessException;

  abstract public void storeHalfWord(int offset, short value)
    throws MemAccessException;

  abstract public void storeWord(int offset, int value)
    throws MemAccessException;

  abstract public byte swapByte(int offset, byte value)
    throws MemAccessException;

  abstract public short swapHalfWord(int offset, short value)
    throws MemAccessException;

  abstract public int swapWord(int offset, int value)
    throws MemAccessException;
}