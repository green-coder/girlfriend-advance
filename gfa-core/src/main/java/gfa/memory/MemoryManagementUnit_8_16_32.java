package gfa.memory;

public class MemoryManagementUnit_8_16_32 extends MemoryManagementUnit {

  protected byte[] memory;
  protected int size;
  protected int mirrorMask;

  public MemoryManagementUnit_8_16_32(String name, int size) {
    super(name);
    createInternalArray(size);
  }

  public byte[] createInternalArray(int s) {
    size = s;
    int i = 1;
    while (i < size) i <<= 1;
    size = i;
    mirrorMask = size - 1;
    memory = new byte[size];
    return memory;
  }

  public void reset() {
    for (int i = 0; i < memory.length; i++)
      memory[i] = 0;
  }

  public byte loadByte(int offset) {
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
    write(offset, value);
  }

  public void storeHalfWord(int offset, short value) {
    offset &= 0xfffffffe;
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
  }

  public void storeWord(int offset, int value) {
    offset &= 0xfffffffc;
    write(offset + 3, (byte) (value >> 24));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 1, (byte) (value >> 8));
    write(offset, (byte) value);
  }

  public byte swapByte(int offset, byte value) {
    byte result = read(offset);
    write(offset, value);
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

}
