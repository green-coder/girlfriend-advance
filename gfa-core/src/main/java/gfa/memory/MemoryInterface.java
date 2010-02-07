package gfa.memory;

public interface MemoryInterface
{
  public void reset();

  public byte  loadByte     (int offset);
  public short loadHalfWord (int offset);
  public int   loadWord     (int offset);

  public void storeByte     (int offset, byte  value);
  public void storeHalfWord (int offset, short value);
  public void storeWord     (int offset, int   value);

  public byte  swapByte     (int offset, byte  value);
  public short swapHalfWord (int offset, short value);
  public int   swapWord     (int offset, int   value);
}
