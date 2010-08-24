package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.WriteMemoryListener;
import com.lemoulinstudio.gfa.core.memory.ReadMemoryListener;
import com.lemoulinstudio.gfa.core.memory.MemoryInterface;
import java.util.ArrayList;
import java.util.List;

public class MemoryObserver implements MemoryInterface {

  protected MemoryInterface target; // The memory to observe.
  protected List<ReadMemoryListener> readListeners;   // The listeners of a read of the memory.
  protected List<WriteMemoryListener> writeListeners; // The listeners of a write onto the memory.

  public MemoryObserver(MemoryInterface target) {
    this.target = target;
    readListeners = new ArrayList<ReadMemoryListener>();
    writeListeners = new ArrayList<WriteMemoryListener>();
  }

  public void addReadMemoryListener(ReadMemoryListener obj) {
    readListeners.add(obj);
  }

  public void removeReadMemoryListener(ReadMemoryListener obj) {
    readListeners.remove(obj);
  }

  public void addWriteMemoryListener(WriteMemoryListener obj) {
    writeListeners.add(obj);
  }

  public void removeWriteMemoryListener(WriteMemoryListener obj) {
    writeListeners.remove(obj);
  }

  public void removeAllListener() {
    readListeners.clear();
    writeListeners.clear();
  }

  public void reset() {
    target.reset();
  }

  public byte loadByte(int offset) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadByte(offset);
    return target.loadByte(offset);
  }

  public short loadHalfWord(int offset) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadHalfWord(offset);
    return target.loadHalfWord(offset);
  }

  public int loadWord(int offset) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadWord(offset);
    return target.loadWord(offset);
  }

  public void storeByte(int offset, byte value) {
    for (WriteMemoryListener listener : writeListeners)
      listener.storeByte(offset, value);
    target.storeByte(offset, value);
  }

  public void storeHalfWord(int offset, short value) {
    for (WriteMemoryListener listener : writeListeners)
      listener.storeHalfWord(offset, value);
    target.storeHalfWord(offset, value);
  }

  public void storeWord(int offset, int value) {
    for (WriteMemoryListener listener : writeListeners)
      listener.storeWord(offset, value);
    target.storeWord(offset, value);
  }

  public byte swapByte(int offset, byte value) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadByte(offset);
    for (WriteMemoryListener listener : writeListeners)
      listener.storeByte(offset, value);
    return target.swapByte(offset, value);
  }

  public short swapHalfWord(int offset, short value) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadHalfWord(offset);
    for (WriteMemoryListener listener : writeListeners)
      listener.storeHalfWord(offset, value);
    return target.swapHalfWord(offset, value);
  }

  public int swapWord(int offset, int value) {
    for (ReadMemoryListener listener : readListeners)
      listener.loadWord(offset);
    for (WriteMemoryListener listener : writeListeners)
      listener.storeWord(offset, value);
    return target.swapWord(offset, value);
  }
}
