package gfa.memory;

public class MemoryManagementUnit_16_32
  extends MemoryManagementUnit
{
  protected byte[] memory;

  public MemoryManagementUnit_16_32(String name, int size)
  {
    super(name);
    memory = new byte[size];
  }

  public byte loadByte(int offset)
    throws MemAccessException
  {
    //throw new MemReadException(getName(), "load byte", offset);
    offset &= 0xfffffffe;
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
    //throw new MemWriteException(getName(), "store byte", offset, value);
    offset &= 0xfffffffe;
    write(offset, value);
    write(offset + 1, value);
  }

  public void storeHalfWord(int offset, short value)
    throws MemAccessException
  {
    offset &= 0xfffffffe;
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
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
    throw new MemWriteException(getName(), "swap byte", offset, value);
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
    return memory[offset];
  }

  protected void write(int offset, byte value)
  {
    memory[offset] = value;
  }

  public byte hardwareAccessLoadByte(int offset)
  {
    return memory[offset];
  }

  public short hardwareAccessLoadHalfWord(int offset)
  {
    return (short) ((memory[offset + 1] << 8) | (0xff & memory[offset]));
  }
}
