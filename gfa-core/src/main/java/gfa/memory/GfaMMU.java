package gfa.memory;

import gfa.time.*;
import gfa.dma.*;
import gfa.gfx.*;
import gfa.debug.*;
import gfa.util.*;

import java.io.*;
import java.net.URL;
import java.util.zip.*;

public class GfaMMU
  implements MemoryInterface
{
  protected MemoryManagementUnit[] memory;
  protected String romFileName = "";
  protected URL romFileUrl = null;

  public GfaMMU()
  {
    memory = new MemoryManagementUnit[16];
    
    memory[0x00] = new SystemROM_8_16_32("System ROM", 0x4000);
    memory[0x02] = new MemoryManagementUnit_8_16_32("External RAM", 0x40000);
    memory[0x03] = new MemoryManagementUnit_8_16_32("Work RAM", 0x8000);
    
    memory[0x04] = new IORegisterSpace_8_16_32("I/O Register Space");
    
    memory[0x05] = new MemoryManagementUnit_16_32("Palette RAM", 0x400);
    memory[0x06] = new MemoryManagementUnit_16_32("Video RAM", 0x18000);
    memory[0x07] = new ObjectAttributMemory_16_32("Sprite RAM", 0x400);
    
    MemoryManagementUnit GameROM_Part1 = new MemoryManagementUnit_8_16_32("GameRom Part 1", 0x0);
    MemoryManagementUnit GameROM_Part2 = new MemoryManagementUnit_8_16_32("GameRom Part 2", 0x0);
    
    memory[0x08] = GameROM_Part1;
    memory[0x09] = GameROM_Part2;
    memory[0x0a] = GameROM_Part1;
    memory[0x0b] = GameROM_Part2;
    memory[0x0c] = GameROM_Part1;
    memory[0x0d] = GameROM_Part2;
    
    memory[0x0e] = new MemoryManagementUnit_8("Cart RAM", 0x10000);
  }

  public void connectToTime(Time time)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToTime(time);
  }

  public void connectToDma0(Dma dma0)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma0(dma0);
  }

  public void connectToDma1(Dma dma1)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma1(dma1);
  }

  public void connectToDma2(Dma dma2)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma2(dma2);
  }

  public void connectToDma3(Dma dma3)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToDma3(dma3);
  }

  public void connectToLcd(Lcd lcd)
  {
    ((IORegisterSpace_8_16_32) memory[0x04]).connectToLcd(lcd);
  }

  static final protected int MemOffsetHiBitsMask = 0xff000000;
  static final protected int MemOffsetLoBitsMask = 0x00ffffff;

  public byte loadByte(int offset)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].loadByte(offset & MemOffsetLoBitsMask);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public short loadHalfWord(int offset)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].loadHalfWord(offset & MemOffsetLoBitsMask);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public int loadWord(int offset)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].loadWord(offset & MemOffsetLoBitsMask);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public void storeByte(int offset, byte value)
    throws AbortException
  {
    try
    {
      memory[offset >>> 24].storeByte(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public void storeHalfWord(int offset, short value)
    throws AbortException
  {
    try
    {
      memory[offset >>> 24].storeHalfWord(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public void storeWord(int offset, int value)
    throws AbortException
  {
    try
    {
      memory[offset >>> 24].storeWord(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public byte swapByte(int offset, byte value)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].swapByte(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public short swapHalfWord(int offset, short value)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].swapHalfWord(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }

  public int swapWord(int offset, int value)
    throws AbortException
  {
    try
    {
      return memory[offset >>> 24].swapWord(offset & MemOffsetLoBitsMask, value);
    }
    catch (NullPointerException e)
    {
      throw new AbortException();
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      throw new AbortException();
    }
  }
    
    /**
     * Return an inputStream from a file or from the first entry in a zip file
     * which ends with ".bin", ".gba" or ".agb".
     */
  protected InputStream openBrutDataInputStream(URL url, String name)
    throws IOException
  {
    url = new URL(url, name);
    InputStream inputStream = url.openStream();
    if (name.endsWith(".zip"))
    {
      ZipInputStream zipInputStream = new ZipInputStream(inputStream);
      while (true)
      {
        ZipEntry zipEntry = zipInputStream.getNextEntry();
	if (zipEntry == null) throw new IOException("End of file reached, but none rom was found in the zip file.");
	String fileName = zipEntry.getName().toLowerCase();
	if (fileName.endsWith(".bin") ||
	    fileName.endsWith(".gba") ||
	    fileName.endsWith(".agb"))
	  return zipInputStream;
      }
    }
    else
      return inputStream;
  }
  
  public void readFully(InputStream inputStream, byte[] buffer)
    throws IOException
  {
      int pos = 0;
      int size = buffer.length;
      int nbByteRead = 0;
      do {
	  pos += nbByteRead;
	  size -= nbByteRead;
	  nbByteRead = inputStream.read(buffer, pos, size);
      } while ((nbByteRead != -1) && (size > 0));
  }
  
  public void loadBios(URL url, String name)
  {
    try
    {
      InputStream inputStream = openBrutDataInputStream(url, name);
      byte[] sys = new byte[0x4000];
      readFully(inputStream, sys);
      inputStream.close();
      ((MemoryManagementUnit_8_16_32) memory[0x00]).setInternalArray(sys);
    }
    catch(IOException e)
    {
      System.out.println("Probleme de chargement du bios " + name + " :");
      e.printStackTrace();
    }
  }

  public void loadRom(URL url, String name)
  {
    try
    {
      InputStream inputStream = openBrutDataInputStream(url, name);
      byte miniBuffer[] = new byte[1024]; // create a 1Kb buffer.
      long fileSize = 0;
      long nbByteRead = inputStream.read(miniBuffer, 0, 1024);
      while (nbByteRead != -1)
      {
	fileSize += nbByteRead;
	nbByteRead = inputStream.read(miniBuffer, 0, 1024);
      }
      inputStream.close();
      
      inputStream = openBrutDataInputStream(url, name);
      long size1;
      long size2;

      if (fileSize <= 0x01000000) // (fileSize <= 16 Megs)
      {
        size1 = fileSize;
        size2 = 0;
      }
      else if (fileSize <= 0x2000000) // (fileSize <= 32 Megs)
      {
        size1 = 0x01000000;
        size2 = fileSize - 0x01000000;
      }
      else // the file is too big and it's not normal : no load !
        throw new IOException("The rom file is too long ! (0x" + Hex.toString(fileSize) + ")");
      
      romFileName = name;
      romFileUrl  = url;
      byte[] part1 = new byte[(int) size1];
      byte[] part2 = new byte[(int) size2];
      ((MemoryManagementUnit_8_16_32) memory[0x08]).setInternalArray(part1);
      ((MemoryManagementUnit_8_16_32) memory[0x09]).setInternalArray(part2);
      readFully(inputStream, part1);
      readFully(inputStream, part2);
      inputStream.close();
    }
    catch(IOException e)
    {
      System.out.println("Probleme de chargement de la rom " + name + " :");
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void reloadRom()
  {
    if (!romFileName.equals(""))
      loadRom(romFileUrl, romFileName);
  }

  public MemoryManagementUnit getMemoryBank(int bankNumber)
  {
    return memory[bankNumber];
  }
  
}
