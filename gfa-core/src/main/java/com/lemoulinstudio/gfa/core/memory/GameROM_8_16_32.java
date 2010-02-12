package com.lemoulinstudio.gfa.core.memory;

public class GameROM_8_16_32 extends MemoryManagementUnit_8_16_32 {

  public GameROM_8_16_32(String name, int size) {
    super(name, size);
  }
  
  public void reset() {
  }

  protected void write(int offset, byte value) {
  }
  
}
