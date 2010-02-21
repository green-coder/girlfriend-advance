package com.lemoulinstudio.gfa.nb.disasm;

/**
 *
 * @author Vincent Cantin
 */
public enum MemoryBank {

  BiosRom    (0x00, 0x00004000, "Bios Rom"),
  DummyRam   (0x01, 0x00000100, "Dummy Ram"),
  ExternalRam(0x02, 0x00040000, "External Ram"),
  WorkRam    (0x03, 0x00008000, "Work Ram"),
  IORegisters(0x04, 0x00000400, "IO Registers"),
  PaletteRam (0x05, 0x00000400, "Palette Ram"),
  VideoRam   (0x06, 0x00018000, "Video Ram"),
  OAMRam     (0x07, 0x00000400, "OAM Ram"),
  GamepackRom(0x08, 0x02000000, "Gamepack Rom"),
  CartRam    (0x0e, 0x00010000, "Cart Ram");

  private int index;
  private int size;
  public String name;

  private MemoryBank(int index, int size, String name) {
    this.index = index;
    this.size = size;
    this.name = name;
  }

  public int getIndex() {
    return index;
  }

  public int getSize() {
    return size;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return getName();
  }

  private static MemoryBank[] logicalToPhysicalMemoryBank = new MemoryBank[] {
    BiosRom, DummyRam, ExternalRam, WorkRam,
    IORegisters, PaletteRam, VideoRam, OAMRam,
    GamepackRom, GamepackRom, GamepackRom, GamepackRom,
    GamepackRom, GamepackRom, CartRam, CartRam
  };
  
  public static MemoryBank getPhysicalMemoryBank(int logicalMemoryBankIndex) {
    return logicalToPhysicalMemoryBank[logicalMemoryBankIndex & 0x0f];
  }

}
