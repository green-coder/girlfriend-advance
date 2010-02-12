package com.lemoulinstudio.gfa.core.memory;

public class MemoryManagementUnit_16_32 extends MemoryManagementUnit {

  protected byte[] memory;
  protected final int size;
  protected final int mirrorMask;

  public MemoryManagementUnit_16_32(String name, int size) {
    super(name);
    memory = new byte[size];
    this.size = size;
    mirrorMask = size - 1;
  }

  public void reset() {
    for (int i = 0; i < memory.length; i++)
      memory[i] = 0;
  }

  public byte loadByte(int offset) {
    offset &= 0xfffffffe;
    return read(offset);
  }

  public short loadHalfWord(int offset) {
    offset &= 0xfffffffe;
    return (short) ((read(offset + 1) << 8) |
                    (0xff & read(offset)));
  }

  public int loadWord(int offset) {
    offset &= 0xfffffffc;
    return ((read(offset + 3) << 24) |
            ((0xff & read(offset + 2)) << 16) |
            ((0xff & read(offset + 1)) <<  8) |
             (0xff & read(offset)));
  }

  public void storeByte(int offset, byte value) {
    offset &= 0xfffffffe;
    write(offset, value);
    write(offset + 1, value);
  }

  public void storeHalfWord(int offset, short value) {
    offset &= 0xfffffffe;
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
  }

  public void storeWord(int offset, int value) {
    offset &= 0xfffffffc;
    write(offset + 3, (byte) (value >> 24));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
  }

  public byte swapByte(int offset, byte value) {
    offset &= 0xfffffffe;
    byte result = read(offset);
    write(offset, value);
    write(offset + 1, value);
    return result;
  }

  public short swapHalfWord(int offset, short value) {
    offset &= 0xfffffffe;
    short result = (short) ((read(offset + 1) << 8) |
                             read(offset));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
    return result;
  }

  public int swapWord(int offset, int value) {
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

  protected byte read(int offset) {
    offset = getInternalOffset(offset);
    return memory[offset];
  }

  protected void write(int offset, byte value) {
    offset = getInternalOffset(offset);
    memory[offset] = value;
  }

  public int getInternalOffset(int offset) {
    return offset & mirrorMask;
  }

  public byte hardwareAccessLoadByte(int offset) {
    return memory[offset];
  }

  public short hardwareAccessLoadHalfWord(int offset) {
    return (short) ((memory[offset + 1] << 8) | (0xff & memory[offset]));
  }
  
}
