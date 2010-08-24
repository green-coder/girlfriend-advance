package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import java.util.ArrayList;
import java.util.List;

public class ArmRegObserver extends ArmReg {

  protected ArmReg target;
  protected List<ReadRegListener> readers;    // The listeners of a read of the register.
  protected List<WriteRegListener> writers;   // The listeners of a write into the register.

  public ArmRegObserver(ArmReg register) {
    super(0);
    target = register;
    readers = new ArrayList<ReadRegListener>();
    writers = new ArrayList<WriteRegListener>();
  }

  public void addReadRegListener(ReadRegListener obj) {
    readers.add(obj);
  }

  public void removeReadRegListener(ReadRegListener obj) {
    readers.remove(obj);
  }

  public void addWriteRegListener(WriteRegListener obj) {
    writers.add(obj);
  }

  public void removeWriteRegListener(WriteRegListener obj) {
    writers.remove(obj);
  }

  public void removeAllListener() {
    readers.clear();
    writers.clear();
  }

  @Override
  public int get() {
    for (ReadRegListener listener : readers)
      listener.notifyGetRequested();
    
    return target.get();
  }

  @Override
  public void set(int v) {
    for (WriteRegListener listener : writers)
      listener.notifySetRequested(v);
    
    target.set(v);
  }

}
