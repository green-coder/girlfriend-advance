package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.CpuStepListener;
import java.util.ArrayDeque;
import java.util.Deque;

public class CpuLogger implements CpuStepListener {

  private GfaDevice gfaDevice;

  private Deque<Integer> queue = new ArrayDeque<Integer>();

  public CpuLogger(GfaDevice gfaDevice) {
    this.gfaDevice = gfaDevice;
  }

  public void notifyCpuStepped() {
    //System.out.printf("PC= %08x\n", gfaDevice.getCpu().PC.get());
    queue.addLast(gfaDevice.getCpu().PC.get());

    if (queue.size() > 30)
      queue.removeFirst();
  }

}
