package com.lemoulinstudio.gfa.nb.filetype.rom;

/**
 *
 * @author vincent
 */
public abstract class StoppableRunner implements Runnable {

  protected boolean stopRequested;

  public void requestStop() {
    stopRequested = true;
  }

}
