package gfa.memory;

public class MemoryManagementUnit_8_16_32
  extends MemoryManagementUnit
{
  protected byte[] memory;
  protected int size;
  protected int sizeMask;

  public MemoryManagementUnit_8_16_32(String name, int size)
  {
    super(name);
    setInternalArray(new byte[size]);
  }

  public void setInternalArray(byte[] byteArray)
  {
    size = byteArray.length;
    if (size == 0) size = 1;
    sizeMask = size - 1;
    memory = byteArray;
  }

  public byte loadByte(int offset)
    throws MemAccessException
  {
    return read(offset);
  }

  public short loadHalfWord(int offset)
    throws MemAccessException
  {
    offset &= 0xfffffffe;
    return (short) ((read(offset + 1) << 8) |
                    (0xff & read(offset)));
  }

  public int loadWord(int offset)
    throws MemAccessException
  {
    offset &= 0xfffffffc;
    return ((read(offset + 3) << 24) |
            ((0xff & read(offset + 2)) << 16) |
            ((0xff & read(offset + 1)) <<  8) |
             (0xff & read(offset)));
  }

  public void storeByte(int offset, byte value)
    throws MemAccessException
  {
    write(offset, value);
  }

  public void storeHalfWord(int offset, short value)
    throws MemAccessException
  {
    offset &= 0xfffffffe;
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
  }

  public void storeWord(int offset, int value)
    throws MemAccessException
  {
    offset &= 0xfffffffc;
    write(offset + 3, (byte) (value >> 24));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
  }

  public byte swapByte(int offset, byte value)
    throws MemAccessException
  {
    byte result = read(offset);
    write(offset, value);
    return result;
  }

  public short swapHalfWord(int offset, short value)
    throws MemAccessException
  {
    offset &= 0xfffffffe;
    short result = (short) ((read(offset + 1) << 8) |
                             read(offset));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
    return result;
  }

  public int swapWord(int offset, int value)
    throws MemAccessException
  {
    offset &= 0xfffffffc;
    int result = ((read(offset + 3) << 24) |
                  ((0xff & read(offset + 2)) << 16) |
                  ((0xff & read(offset + 1)) << 8)  |
                   (0xff & read(offset)));
    write(offset + 3, (byte) (value >> 24));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
    return result;
  }

  protected byte read(int offset)
  {
    return memory[offset % size];
  }

  protected void write(int offset, byte value)
  {
    memory[offset % size] = value;
  }

}
