package gfa.memory;

public interface MemoryInterface
{
  public byte  loadByte(int offset)     throws AbortException;
  public short loadHalfWord(int offset) throws AbortException;
  public int   loadWord(int offset)     throws AbortException;

  public void storeByte(int offset, byte value)      throws AbortException;
  public void storeHalfWord(int offset, short value) throws AbortException;
  public void storeWord(int offset, int value)       throws AbortException;

  public byte  swapByte(int offset, byte value)      throws AbortException;
  public short swapHalfWord(int offset, short value) throws AbortException;
  public int   swapWord(int offset, int value)       throws AbortException;
}
