package com.lemoulinstudio.gfa.core.memory;

public class VideoRam_16_32 extends MemoryManagementUnit_16_32 {

  public VideoRam_16_32(String name, int size) {
    super(name, size);
  }

  @Override
  protected byte read(int offset) {
    offset = getInternalOffset(offset);
    return memory[offset];
  }

  @Override
  protected void write(int offset, byte value) {
    offset = getInternalOffset(offset);
    memory[offset] = value;
  }

  @Override
  public int getInternalOffset(int offset) {
    return ((offset & 0xffffff) % size);
  }
  
}
