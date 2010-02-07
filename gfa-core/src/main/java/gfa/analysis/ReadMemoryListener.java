package gfa.analysis;

public interface ReadMemoryListener
{
  public void loadByte(int offset);
  public void loadHalfWord(int offset);
  public void loadWord(int offset);
}
