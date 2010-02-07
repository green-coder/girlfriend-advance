package gfa.memory;

import gfa.time.*;
import gfa.dma.*;
import gfa.gfx.*;
import gfa.util.*;
import gfa.analysis.MemoryObserver;
import gfa.analysis.ReadMemoryListener;
import gfa.analysis.WriteMemoryListener;

import java.io.*;
import java.net.*;
import java.util.zip.*;

public class GfaMMU
    implements MemoryInterface
{
    protected MemoryInterface[] trueMemory;
    protected MemoryInterface[] spyMemory;
    protected MemoryInterface[] memory;
    protected String biosFileName = "";
    protected String romFileName = "";
    protected URL biosFileUrl = null;
    protected URL romFileUrl = null;
    
    public GfaMMU()
    {
	trueMemory = new MemoryInterface[16];
	spyMemory = new MemoryInterface[16];
	memory = trueMemory; // For no use of conditionnal breakpoints.
	//memory = spyMemory; // For using conditionnal breakpoints.
	
	trueMemory[0x00] = new SystemROM_8_16_32("System ROM", 0x4000);
	trueMemory[0x01] = new MemoryManagementUnit_8_16_32("Dummy memory", 0x100);
	trueMemory[0x02] = new MemoryManagementUnit_8_16_32("External RAM", 0x40000);
	trueMemory[0x03] = new MemoryManagementUnit_8_16_32("Work RAM", 0x8000);
	trueMemory[0x04] = new IORegisterSpace_8_16_32("I/O Register Space", 0x400);
	trueMemory[0x05] = new MemoryManagementUnit_16_32("Palette RAM", 0x400);
	trueMemory[0x06] = new VideoRam_16_32("Video RAM", 0x18000);
	trueMemory[0x07] = new ObjectAttributMemory_16_32("OAM RAM", 0x400);
	trueMemory[0x08] = new GameROM_8_16_32("GameRom Part 1", 0x1);
	trueMemory[0x09] = new GameROM_8_16_32("GameRom Part 2", 0x1);
	trueMemory[0x0a] = trueMemory[0x08];
	trueMemory[0x0b] = trueMemory[0x09];
	trueMemory[0x0c] = trueMemory[0x08];
	trueMemory[0x0d] = trueMemory[0x09];
	trueMemory[0x0e] = new MemoryManagementUnit_8("Cart RAM", 0x10000);
	trueMemory[0x0f] = trueMemory[0x0e];
	
	for (int i = 0; i < 16; i++)
	    spyMemory[i] = new MemoryObserver(trueMemory[i]);
    }
    
    public void setSpyModeEnabled(boolean b)
    {
	if (b) memory = spyMemory;
	else memory = trueMemory;
    }
    
    public void connectToTime(Time time)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToTime(time);
    }
    
    public void connectToDma0(Dma dma0)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToDma0(dma0);
    }
    
    public void connectToDma1(Dma dma1)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToDma1(dma1);
    }
    
    public void connectToDma2(Dma dma2)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToDma2(dma2);
    }
    
    public void connectToDma3(Dma dma3)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToDma3(dma3);
    }
    
    public void connectToLcd(Lcd lcd)
    {
	((IORegisterSpace_8_16_32) trueMemory[0x04]).connectToLcd(lcd);
    }
    
    static final protected int MemOffsetHiBitsMask = 0x0f000000;
    static final protected int MemOffsetLoBitsMask = 0x00ffffff;
    
    public byte loadByte(int offset)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadByte(offset);}
    
    public short loadHalfWord(int offset)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadHalfWord(offset);}
    
    public int loadWord(int offset)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].loadWord(offset);}
    
    public void storeByte(int offset, byte value)
    {memory[(offset & MemOffsetHiBitsMask) >>> 24].storeByte(offset, value);}
    
    public void storeHalfWord(int offset, short value)
    {memory[(offset & MemOffsetHiBitsMask) >>> 24].storeHalfWord(offset, value);}
    
    public void storeWord(int offset, int value)
    {memory[(offset & MemOffsetHiBitsMask) >>> 24].storeWord(offset, value);}
    
    public byte swapByte(int offset, byte value)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapByte(offset, value);}
    
    public short swapHalfWord(int offset, short value)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapHalfWord(offset, value);}
    
    public int swapWord(int offset, int value)
    {return memory[(offset & MemOffsetHiBitsMask) >>> 24].swapWord(offset, value);}
    
    public byte directLoadByte(int offset)
    {return trueMemory[(offset & MemOffsetHiBitsMask) >>> 24].loadByte(offset);}
    
    public short directLoadHalfWord(int offset)
    {return trueMemory[(offset & MemOffsetHiBitsMask) >>> 24].loadHalfWord(offset);}
    
    public int directLoadWord(int offset)
    {return trueMemory[(offset & MemOffsetHiBitsMask) >>> 24].loadWord(offset);}
    
    /**
     * Return an inputStream from a file or from the first entry in a zip file
     * which ends with ".bin", ".gba" or ".agb".
     */
    protected InputStream openBrutDataInputStream(URL url, String name)
	throws IOException
    {
	url = new URL(url, name);
	InputStream inputStream = url.openStream();
	if (name.toLowerCase().endsWith(".zip"))
	    {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		while (true)
		    {
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			if (zipEntry == null) return null;
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
    
    public boolean loadBios(String name)
    {
	try {return loadBios(new URL("file:"), name);}
	catch (MalformedURLException e) {return false;}
    }
    
    public boolean loadBios(URL url, String name)
    {
	try
	    {
		name = filterNameForJava(name);
		InputStream inputStream = openBrutDataInputStream(url, name);
		byte[] sys = ((MemoryManagementUnit_8_16_32) trueMemory[0x00]).createInternalArray(0x4000);
		readFully(inputStream, sys);
		inputStream.close();
		biosFileName = name;
		biosFileUrl  = url;
		
	    }
	catch(IOException e)
	    {
		System.out.println("Probleme de chargement du bios " + name + " :");
		e.printStackTrace();
		return false;
	    }
	
	return true;
    }
    
    public boolean loadRom(String name)
    {
	try {return loadRom(new URL("file:"), name);}
	catch (MalformedURLException e) {return false;}
    }
    
    public boolean loadRom(URL url, String name)
    {
	try
	    {
		name = filterNameForJava(name);
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
		else // the file is too big and it's not normal : don't load !
		    throw new IOException("The rom file is too long ! (0x" + Hex.toString(fileSize) + ")");
		
		romFileName = name;
		romFileUrl  = url;
		byte[] part1 = ((GameROM_8_16_32) trueMemory[0x08]).createInternalArray((int) size1);
		byte[] part2 = ((GameROM_8_16_32) trueMemory[0x09]).createInternalArray((int) size2);
		readFully(inputStream, part1);
		readFully(inputStream, part2);
		inputStream.close();
	    }
	catch(IOException e)
	    {
		System.out.println("Probleme de chargement de la rom " + name + " :");
		e.printStackTrace();
		return false;
	    }
	
	return true;
    }
    
    public void reloadBios()
    {
	if (!biosFileName.equals(""))
	    loadBios(biosFileUrl, biosFileName);
    }
    
    public void reloadRom()
    {
	if (!romFileName.equals(""))
	    loadRom(romFileUrl, romFileName);
    }
    
    public String getRomFileName()
    {
	return romFileName;
    }
    
    public String filterNameForJava(String s)
    {
	char[] c = new char[s.length()];
	for (int i = 0; i < s.length(); i++)
	    c[i] = ((s.charAt(i) == '\\') ? '/' : s.charAt(i));
	s = new String(c);
	int index = s.indexOf(':');
	if (index >= 0) s = s.substring(index + 1);
	return s;
    }
    
    public void reset()
    {
	for (int i = 0; i < trueMemory.length; i++)
	    if (trueMemory[i] != null)
		trueMemory[i].reset();
    }
    
    public MemoryManagementUnit getMemoryBank(int bankNumber)
    {
	return (MemoryManagementUnit) trueMemory[bankNumber];
    }
    
    public void setObserverEnabled(boolean letSpy)
    {
	if (letSpy) memory = spyMemory;
	else memory = trueMemory;
    }
    
    public void addReadMemoryListener(ReadMemoryListener obj)
    {
	for (int i = 0; i < 16; i++)
	    ((MemoryObserver) spyMemory[i]).addReadMemoryListener(obj);
    }
    
    public void removeReadMemoryListener(ReadMemoryListener obj)
    {
	for (int i = 0; i < 16; i++)
	    ((MemoryObserver) spyMemory[i]).removeReadMemoryListener(obj);
    }
    
    public void addWriteMemoryListener(WriteMemoryListener obj)
    {
	for (int i = 0; i < 16; i++)
	    ((MemoryObserver) spyMemory[i]).addWriteMemoryListener(obj);
    }
    
    public void removeWriteMemoryListener(WriteMemoryListener obj)
    {
	for (int i = 0; i < 16; i++)
	    ((MemoryObserver) spyMemory[i]).removeWriteMemoryListener(obj);
    }
    
    public void addReadMemoryListener(ReadMemoryListener obj, int offset)
    {
	MemoryObserver mo = (MemoryObserver) spyMemory[(offset & MemOffsetHiBitsMask) >>> 24];
	mo.addReadMemoryListener(obj);
    }
    
    public void removeReadMemoryListener(ReadMemoryListener obj, int offset)
    {
	MemoryObserver mo = (MemoryObserver) spyMemory[(offset & MemOffsetHiBitsMask) >>> 24];
	mo.removeReadMemoryListener(obj);
    }
    
    public void addWriteMemoryListener(WriteMemoryListener obj, int offset)
    {
	MemoryObserver mo = (MemoryObserver) spyMemory[(offset & MemOffsetHiBitsMask) >>> 24];
	mo.addWriteMemoryListener(obj);
    }
    
    public void removeWriteMemoryListener(WriteMemoryListener obj, int offset)
    {
	MemoryObserver mo = (MemoryObserver) spyMemory[(offset & MemOffsetHiBitsMask) >>> 24];
	mo.removeWriteMemoryListener(obj);
    }
    
    public void removeAllListener()
    {
	for (int i = 0; i < 16; i++)
	    ((MemoryObserver) spyMemory[i]).removeAllListener();
    }
    
    public int getInternalOffset(int offset)
    {
	return ((MemoryManagementUnit) trueMemory[(offset & MemOffsetHiBitsMask) >>> 24]).getInternalOffset(offset) | (offset & MemOffsetHiBitsMask);
    }
}
