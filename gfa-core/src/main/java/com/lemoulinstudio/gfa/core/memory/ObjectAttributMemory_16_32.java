package com.lemoulinstudio.gfa.core.memory;

public class ObjectAttributMemory_16_32 extends MemoryManagementUnit_16_32 {

  public ObjectAttributMemory_16_32(String name, int size) {
    super(name, size);
  }

  public final int ObjPriorityMask = 0x0c;

  public int getObjPriority(int objNumber) {
    int base = objNumber * 4 * 2; // 2 is the size in byte of a halfword
    return (memory[base + 5] & ObjPriorityMask) >>> 2;
  }

  public final int ObjHiSizeMask = 0x000000c0;
  public final int ObjLoSizeMask = 0x000000c0;

  /**
   * Return the number of tile which horizontally compose this sprite.
   */
  public int getObjXTile(int objNumber) {
    int base = objNumber * 4 * 2;
    int sizeCode = (((memory[base + 1] & ObjHiSizeMask) >>> 4) |
                    ((memory[base + 3] & ObjLoSizeMask) >>> 6));
    switch (sizeCode) {
      case 0: case 8: case 9:          return 1;
      case 1: case 4: case 10:         return 2;
      case 2: case 5: case 6: case 11: return 4;
      case 3: case 7:                  return 8;
      default: return 0; // is not used - unknown
    }
  }

  // Return the number of tile which vertically compose this sprite.
  public int getObjYTile(int objNumber) {
    int base = objNumber * 8;
    int sizeCode = (((memory[base + 1] & ObjHiSizeMask) >>> 4) |
                    ((memory[base + 3] & ObjLoSizeMask) >>> 6));
    switch (sizeCode) {
      case 0: case 4: case 5:          return 1;
      case 1: case 6: case 8:          return 2;
      case 2: case 7: case 9: case 10: return 4;
      case 3: case 11:                 return 8;
      default: return 0; // is not used - unknown
    }
  }

  public int getObjXPos(int objNumber) {
    int base = objNumber * 4 * 2;
    return (0x000000ff & memory[base + 2]) | ((memory[base + 3] << 31) >> 23);
  }

  public int getObjYPos(int objNumber) {
    int base = objNumber * 4 * 2;
    return (0x000000ff & memory[base + 0]);
  }

  public int getTileNumber(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 5] & 0x00000003) << 8) | (memory[base + 4] & 0x000000ff);
  }

  public boolean is256Color(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x20) != 0);
  }

  public int getPal16Number(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 5] & 0x000000f0) >>> 4);
  }

  public boolean isRotScalEnabled(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x01) != 0);
  }

  public boolean isDoubleSizeEnabled(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x02) != 0);
  }

  public boolean isSemiTransparent(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x0c) == 0x04);
  }

  public boolean isObjWindow(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x0c) == 0x08);
  }

  public boolean isMosaicEnabled(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 1] & 0x10) != 0);
  }

  public boolean isHFlipEnabled(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 3] & 0x10) != 0);
  }

  public boolean isVFlipEnabled(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 3] & 0x20) != 0);
  }

  public int getRotScalIndex(int objNumber) {
    int base = objNumber * 4 * 2;
    return ((memory[base + 3] & 0x0000003e) >>> 1);
  }

  public short getPA(int index) {
    int base = index * 4 * 2 * 4 + 6 + 0 * 8;
    return (short) ((memory[base + 1] << 8) | (memory[base] & 0x000000ff));
  }

  public short getPB(int index) {
    int base = index * 4 * 2 * 4 + 6 + 1 * 8;
    return (short) ((memory[base + 1] << 8) | (memory[base] & 0x000000ff));
  }

  public short getPC(int index) {
    int base = index * 4 * 2 * 4 + 6 + 2 * 8;
    return (short) ((memory[base + 1] << 8) | (memory[base] & 0x000000ff));
  }
  
  public short getPD(int index) {
    int base = index * 4 * 2 * 4 + 6 + 3 * 8;
    return (short) ((memory[base + 1] << 8) | (memory[base] & 0x000000ff));
  }
  
}
