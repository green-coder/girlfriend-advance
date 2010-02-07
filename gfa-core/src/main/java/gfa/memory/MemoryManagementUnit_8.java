package gfa.memory;

public class MemoryManagementUnit_8
  extends MemoryManagementUnit
{
  protected byte[] memory;
  protected int mirrorMask;

  public MemoryManagementUnit_8(String name, int size)
  {
    super(name);
    memory = new byte[size];
    mirrorMask = size - 1;
  }

  public void reset()
  {
    for (int i = 0; i < memory.length; i++)
      memory[i] = 0;
  }

  public byte loadByte(int offset)
  {
    return read(offset);
  }

  public short loadHalfWord(int offset)
  {
    return 0;
  }

  public int loadWord(int offset)
  {
    return 0;
  }

  public void storeByte(int offset, byte value)
  {
    write(offset, value);
  }

  public void storeHalfWord(int offset, short value)
  {
  }

  public void storeWord(int offset, int value)
  {
  }

  public byte swapByte(int offset, byte value)
  {
    byte result = read(offset);
    write(offset, value);
    return result;
  }

  public short swapHalfWord(int offset, short value)
  {
    return 0;
  }

  public int swapWord(int offset, int value)
  {
    return 0;
  }

  protected byte read(int offset)
  {
    offset = getInternalOffset(offset);
    return memory[offset];
  }

  protected void write(int offset, byte value)
  {
    offset = getInternalOffset(offset);
    memory[offset] = value;
  }

  public int getInternalOffset(int offset)
  {
    return offset & mirrorMask;
  }

}
