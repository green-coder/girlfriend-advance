package com.lemoulinstudio.gfa.core.memory;

import com.lemoulinstudio.gfa.core.dma.Dma;
import com.lemoulinstudio.gfa.core.gfx.Lcd;
import com.lemoulinstudio.gfa.core.time.Time;
import com.lemoulinstudio.gfa.core.util.Hex;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GfaMMU implements MemoryInterface {

  protected MemoryInterface[] memory;

  public GfaMMU() {
    memory = new MemoryInterface[16];

    memory[0x00] = new SystemROM_8_16_32("System ROM", 0x4000);
    memory[0x01] = new MemoryManagementUnit_8_16_32("Dummy memory", 0x100);
    memory[0x02] = new MemoryManagementUnit_8_16_32("External RAM", 0x40000);
    memory[0x03] = new MemoryManagementUnit_8_16_32("Work RAM", 0x8000);
    memory[0x04] = new IORegisterSpace_8_16_32("I/O Register Space", 0x400);
    memory[0x05] = new MemoryManagementUnit_16_32("Palette RAM", 0x400);
    memory[0x06] = new VideoRam_16_32("Video RAM", 0x18000);
    memory[0x07] = new ObjectAttributMemory_16_32("OAM RAM", 0x400);
    memory[0x08] = new GameROM_8_16_32("GameRom Part 1", 0x1);
    memory[0x09] = new GameROM_8_16_32("GameRom Part 2", 0x1);
    memory[0x0a] = memory[0x08];
    memory[0x0b] = memory[0x09];
    memory[0x0c] = memory[0x08];
    memory[0x0d] = memory[0x09];
    memory[0x0e] = new MemoryManagementUnit_8("Cart RAM", 0x10000);
    memory[0x0f] = memory[0x0e];
  }

  public void connectToTime(Time time) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToTime(time);
  }

  public void connectToDma0(Dma dma0) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma0(dma0);
  }

  public void connectToDma1(Dma dma1) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma1(dma1);
  }

  public void connectToDma2(Dma dma2) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma2(dma2);
  }

  public void connectToDma3(Dma dma3) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma3(dma3);
  }

  public void connectToLcd(Lcd lcd) {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToLcd(lcd);
  }
  static final protected int MemOffsetHiBitsMask = 0x0f000000;
  static final protected int MemOffsetLoBitsMask = 0x00ffffff;

  public byte loadByte(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadByte(offset);
  }

  public short loadHalfWord(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadHalfWord(offset);
  }

  public int loadWord(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadWord(offset);
  }

  public void storeByte(int offset, byte value) {
    memory[(offset & MemOffsetHiBitsMask) >>> 24].storeByte(offset, value);
  }

  public void storeHalfWord(int offset, short value) {
    memory[(offset & MemOffsetHiBitsMask) >>> 24].storeHalfWord(offset, value);
  }

  public void storeWord(int offset, int value) {
    memory[(offset & MemOffsetHiBitsMask) >>> 24].storeWord(offset, value);
  }

  public byte swapByte(int offset, byte value) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapByte(offset, value);
  }

  public short swapHalfWord(int offset, short value) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapHalfWord(offset, value);
  }

  public int swapWord(int offset, int value) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapWord(offset, value);
  }

  public byte directLoadByte(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadByte(offset);
  }

  public short directLoadHalfWord(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadHalfWord(offset);
  }

  public int directLoadWord(int offset) {
    return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadWord(offset);
  }

  /**
   * Return an inputStream from a file or from the first entry in a zip file
   * which ends with ".bin", ".gba" or ".agb".
   */
  protected InputStream openBrutDataInputStream(URL url, String name)
          throws IOException {
    InputStream inputStream = url.openStream();
    if (name.toLowerCase().endsWith(".zip")) {
      ZipInputStream zipInputStream = new ZipInputStream(inputStream);
      while (true) {
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        if (zipEntry == null) return null;
        String fileName = zipEntry.getName().toLowerCase();
        if (fileName.endsWith(".bin")
                || fileName.endsWith(".gba")
                || fileName.endsWith(".agb"))
          return zipInputStream;
      }
    }
    else
      return inputStream;
  }

  public void readFully(InputStream inputStream, byte[] buffer) throws IOException {
    int pos = 0;
    int size = buffer.length;
    int nbByteRead = 0;
    do {
      pos += nbByteRead;
      size -= nbByteRead;
      nbByteRead = inputStream.read(buffer, pos, size);
    } while ((nbByteRead != -1) && (size > 0));
  }

  public boolean loadBios(String name) {
    URL url = getClass().getClassLoader().getResource(name);
    return loadBios(url, name);
  }

  public boolean loadBios(URL url, String name) {
    try {
      name = name.replace('\\', '/');
      name = name.substring(name.indexOf(':') + 1);
      InputStream inputStream = openBrutDataInputStream(url, name);
      byte[] sys = ((MemoryManagementUnit_8_16_32) memory[0x00]).createInternalArray(0x4000);
      readFully(inputStream, sys);
      inputStream.close();
    }
    catch (IOException e) {
      System.out.println("Probleme de chargement du bios " + name + " :");
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public void loadRom(InputStream in) throws IOException {
    // Create a dynamic array with 1Mb of initial capacity.
    ByteArrayOutputStream romByteArray = new ByteArrayOutputStream(0x100000);

    byte[] fourK = new byte[4096];
    for (int nbBytes = in.read(fourK); nbBytes >= 0; nbBytes = in.read(fourK))
      romByteArray.write(fourK, 0, nbBytes);

    int romSize = romByteArray.size();
    
    int part1Size = Math.min(romSize, 0x01000000); // 16Mb max
    romSize -= part1Size;
    int part2Size = Math.min(romSize, 0x01000000); // 16Mb max
    romSize -= part2Size;

    if (romSize > 0) // The file is too big and it's not normal: fail the load.
      throw new IOException("The rom file is too long ! (0x" + Hex.toString(romSize) + ")");

    byte[] part1 = ((GameROM_8_16_32) memory[0x08]).createInternalArray((int) part1Size);
    byte[] part2 = ((GameROM_8_16_32) memory[0x09]).createInternalArray((int) part2Size);

    ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(romByteArray.toByteArray());
    arrayInputStream.read(part1);
    arrayInputStream.read(part2);
  }

  public void reset() {
    for (int i = 0; i < memory.length; i++)
      if (memory[i] != null)
        memory[i].reset();
  }

  public MemoryManagementUnit getMemoryBank(int bankNumber) {
    return (MemoryManagementUnit) memory[bankNumber];
  }

  public int getInternalOffset(int offset) {
    return ((MemoryManagementUnit) memory[(offset & MemOffsetHiBitsMask) >>> 24]).getInternalOffset(offset) | (offset & MemOffsetHiBitsMask);
  }

}
