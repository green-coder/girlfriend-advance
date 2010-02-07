package gfa.analysis;

import java.util.Vector;
import java.util.Enumeration;

import gfa.cpu.ArmReg;

public class ArmRegObserver
  extends ArmReg
{
  protected ArmReg target;
  protected Vector readers;   // The listeners of a read of the register.
  protected Vector writers;   // The listeners of a write into the register.
  protected ReadRegListener[] aReaders; // The array form of listeners.
  protected WriteRegListener[] aWriters; // The array form of listeners.

  public ArmRegObserver(ArmReg register)
  {
    super(0);
    target = register;
    readers = new Vector();
    writers = new Vector();
    aReaders = new ReadRegListener[0];
    aWriters = new WriteRegListener[0];
  }

  public void addReadRegListener(ReadRegListener obj)
  {
    readers.add(obj);
    aReaders = new ReadRegListener[readers.size()];
    for (int i = 0; i < readers.size(); i++)
      aReaders[i] = (ReadRegListener) readers.get(i);
  }

  public void removeReadRegListener(ReadRegListener obj)
  {
    readers.remove(obj);
    aReaders = new ReadRegListener[readers.size()];
    for (int i = 0; i < readers.size(); i++)
      aReaders[i] = (ReadRegListener) readers.get(i);
  }

  public void addWriteRegListener(WriteRegListener obj)
  {
    writers.add(obj);
    aWriters = new WriteRegListener[writers.size()];
    for (int i = 0; i < writers.size(); i++)
      aWriters[i] = (WriteRegListener) writers.get(i);
  }

  public void removeWriteRegListener(WriteRegListener obj)
  {
    writers.remove(obj);
    aWriters = new WriteRegListener[writers.size()];
    for (int i = 0; i < writers.size(); i++)
      aWriters[i] = (WriteRegListener) writers.get(i);
  }

  public void removeAllListener()
  {
    readers.clear();
    writers.clear();
    aReaders = new ReadRegListener[0];
    aWriters = new WriteRegListener[0];
  }

  public int get()
  {
    for (int i = 0; i < aReaders.length; i++)
      aReaders[i].notifyGetRequested();
    return target.get();
  }

  public void set(int v)
  {
    for (int i = 0; i < aWriters.length; i++)
      aWriters[i].notifySetRequested(v);
    target.set(v);
  }
}
