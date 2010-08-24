package com.lemoulinstudio.gfa.core.memory;

public interface ReadMemoryListener {
  public void loadByte(int offset);
  public void loadHalfWord(int offset);
  public void loadWord(int offset);
}
