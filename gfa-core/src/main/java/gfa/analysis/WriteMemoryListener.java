package gfa.analysis;

public interface WriteMemoryListener
{
  public void storeByte(int offset, byte value);
  public void storeHalfWord(int offset, short value);
  public void storeWord(int offset, int value);
}
