package gfa.analysis;

import java.util.Vector;
import java.util.Enumeration;
import gfa.memory.MemoryInterface;
import gfa.memory.MemoryManagementUnit;

public class MemoryObserver
  implements MemoryInterface
{
  protected MemoryInterface target; // The memory to observe.
  protected Vector readers;         // The listeners of a read of the memory.
  protected Vector writers;         // The listeners of a write onto the memory.
  protected ReadMemoryListener[] aReaders; // The array form of listeners.
  protected WriteMemoryListener[] aWriters; // The array form of listeners.

  public MemoryObserver(MemoryInterface target)
  {
    this.target = target;
    readers = new Vector();
    writers = new Vector();
    aReaders = new ReadMemoryListener[0];
    aWriters = new WriteMemoryListener[0];
  }

  public void addReadMemoryListener(ReadMemoryListener obj)
  {
    readers.add(obj);
    aReaders = new ReadMemoryListener[readers.size()];
    for (int i = 0; i < readers.size(); i++)
      aReaders[i] = (ReadMemoryListener) readers.get(i);
  }

  public void removeReadMemoryListener(ReadMemoryListener obj)
  {
    readers.remove(obj);
    aReaders = new ReadMemoryListener[readers.size()];
    for (int i = 0; i < readers.size(); i++)
      aReaders[i] = (ReadMemoryListener) readers.get(i);
  }

  public void addWriteMemoryListener(WriteMemoryListener obj)
  {
    writers.add(obj);
    aWriters = new WriteMemoryListener[writers.size()];
    for (int i = 0; i < writers.size(); i++)
      aWriters[i] = (WriteMemoryListener) writers.get(i);
  }

  public void removeWriteMemoryListener(WriteMemoryListener obj)
  {
    writers.remove(obj);
    aWriters = new WriteMemoryListener[writers.size()];
    for (int i = 0; i < writers.size(); i++)
      aWriters[i] = (WriteMemoryListener) writers.get(i);
  }

  public void removeAllListener()
  {
    readers.clear();
    writers.clear();
    aReaders = new ReadMemoryListener[0];
    aWriters = new WriteMemoryListener[0];
  }

  public void reset()
  {
    target.reset();
  }

  public byte loadByte(int offset)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadByte(offset);
    return target.loadByte(offset);
  }

  public short loadHalfWord(int offset)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadHalfWord(offset);
    return target.loadHalfWord(offset);
  }

  public int loadWord(int offset)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadWord(offset);
    return target.loadWord(offset);
  }

  public void storeByte(int offset, byte value)
  {
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeByte(offset, value);
    target.storeByte(offset, value);
  }

  public void storeHalfWord(int offset, short value)
  {
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeHalfWord(offset, value);
    target.storeHalfWord(offset, value);
  }

  public void storeWord(int offset, int value)
  {
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeWord(offset, value);
    target.storeWord(offset, value);
  }

  public byte swapByte(int offset, byte value)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadByte(offset);
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeByte(offset, value);
    return target.swapByte(offset, value);
  }

  public short swapHalfWord(int offset, short value)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadHalfWord(offset);
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeHalfWord(offset, value);
    return target.swapHalfWord(offset, value);
  }

  public int swapWord(int offset, int value)
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].loadWord(offset);
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].storeWord(offset, value);
    return target.swapWord(offset, value);
  }
}
