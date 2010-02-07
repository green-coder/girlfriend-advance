package gfa.time;

import gfa.memory.*;
import gfa.gfx.*;
import gfa.dma.Dma;

/**
  * This class symbolize the time notion.
  */
public class Time
{
  private long currentCPUCycle = 0;

  public Timer timer0;
  public Timer timer1;
  public Timer timer2;
  public Timer timer3;
  public RenderTimer renderTimer;

  public Time()
  {
    timer3 = new Timer(null,   3); // The timer 3 isn't chained to another timer.
    timer2 = new Timer(timer3, 2); // The timer 2 is chained to the timer 3.
    timer1 = new Timer(timer2, 1); // The timer 1 is chained to the timer 2.
    timer0 = new Timer(timer1, 0); // The timer 0 is chained to the timer 1.
    renderTimer = new RenderTimer();
  }

  public void connectToMemory(GfaMMU memory)
  {
    timer0.connectToMemory(memory);
    timer1.connectToMemory(memory);
    timer2.connectToMemory(memory);
    timer3.connectToMemory(memory);
    renderTimer.connectToMemory(memory);
  }

  public void connectToLcd(Lcd lcd)
  {
    renderTimer.connectToLcd(lcd);
  }

  public void connectToDma0(Dma dma0)
  {
    renderTimer.connectToDma0(dma0);
  }
  
  public void connectToDma1(Dma dma1)
  {
    renderTimer.connectToDma1(dma1);
  }
  
  public void connectToDma2(Dma dma2)
  {
    renderTimer.connectToDma2(dma2);
  }
  
  public void connectToDma3(Dma dma3)
  {
    renderTimer.connectToDma3(dma3);
  }
  
  public void reset()
  {
    currentCPUCycle = 0;

    timer0.reset();
    timer1.reset();
    timer2.reset();
    timer3.reset();

    renderTimer.reset();
  }

  public void addTime(long nbCycles)
  {
    // Update the global counter (used by the emulator only).
    currentCPUCycle += nbCycles;

    // Update the timers.
    timer0.addTime((int) nbCycles);
    timer1.addTime((int) nbCycles);
    timer2.addTime((int) nbCycles);
    timer3.addTime((int) nbCycles);

    // Update the raytrace registers.
    renderTimer.addTime((int) nbCycles);
  }

  /**
   * Can be used by the emulator for determining
   * the gba cpu time elapsed since the last reset.
   */
  public long getTime()
  {
    return currentCPUCycle;
  }
}
