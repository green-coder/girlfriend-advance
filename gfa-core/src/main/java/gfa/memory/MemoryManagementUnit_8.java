package gfa.memory;

import gfa.debug.*;

public class MemoryManagementUnit_8
  extends MemoryManagementUnit
{
  protected byte[] memory;

  public MemoryManagementUnit_8(String name, int size)
  {
    super(name);
    memory = new byte[size];
  }

  public byte loadByte(int offset)
    throws MemAccessException
  {
    return read(offset);
  }

  public short loadHalfWord(int offset)
    throws MemAccessException
  {
    throw new MemReadException(getName(), "load halfWord", offset);
  }

  public int loadWord(int offset)
    throws MemAccessException
  {
    throw new MemReadException(getName(), "load word", offset);
  }

  public void storeByte(int offset, byte value)
    throws MemAccessException
  {
    write(offset, value);
  }

  public void storeHalfWord(int offset, short value)
    throws MemAccessException
  {
    throw new MemWriteException(getName(), "store halfWord", offset, value);
  }

  public void storeWord(int offset, int value)
    throws MemAccessException
  {
    throw new MemWriteException(getName(), "store word", offset, value);
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
    throw new MemWriteException(getName(), "swap halfWord", offset, value);
  }

  public int swapWord(int offset, int value)
    throws MemAccessException
  {
    throw new MemWriteException(getName(), "swap word", offset, value);
  }

  protected byte read(int offset)
  {
    return memory[offset];
  }

  protected void write(int offset, byte value)
  {
    memory[offset] = value;
  }

}
