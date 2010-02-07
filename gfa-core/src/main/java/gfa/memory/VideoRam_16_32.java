package gfa.memory;

public class VideoRam_16_32
  extends MemoryManagementUnit_16_32
{
  public VideoRam_16_32(String name, int size)
  {
    super(name, size);
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
    return ((offset & 0xffffff) % size);
  }

}
