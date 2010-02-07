package gfa.time;

import gfa.memory.*;

public class Timer
{
  private int value;  // Is in reality used as a short.
  private int rest;   // Number of cycles from last value change.
  private int period; // Number of cycles between 2 differents values of this timer.

  private Timer nextTimer;     // The timer where to add carry bit if it is in cascade mode.
  private boolean cascadeEnabled; // if true, receive the previous timer overflow.
  private boolean irqEnabled;     // If true, throw an IRQ when an overflow occurs.
  private boolean timerEnabled;   // If false, the timer is halted.

  private String timerName; // For debug purpose : to know where the IRQ come from.
  private short interruptBit;
  private IORegisterSpace_8_16_32 ioMem;

  public Timer(Timer nextTimer, int timerNumber)
  {
    this.nextTimer = nextTimer;
    timerName = "timer" + timerNumber;
    switch (timerNumber)
    {
      case 0:  interruptBit = IORegisterSpace_8_16_32.timer0InterruptBit; break;
      case 1:  interruptBit = IORegisterSpace_8_16_32.timer1InterruptBit; break;
      case 2:  interruptBit = IORegisterSpace_8_16_32.timer2InterruptBit; break;
      case 3:  interruptBit = IORegisterSpace_8_16_32.timer3InterruptBit; break;
      default: throw new Error("time number incorrect.");
    }
    reset();
  }

  public void connectToMemory(GfaMMU memory)
  {
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(4);
  }
  
  public void reset()
  {
    value          = 0;
    rest           = 0;
    period         = 1;
    cascadeEnabled = false;
    irqEnabled     = false;
    timerEnabled   = false;
  }

  public void setTime(short n)
  {
    value = (int) (0xffff & n);
    rest = 0;
  }

  public short getTime()
  {
    return (short) value;
  }

  public void addTime(int n)
  {
    if (timerEnabled)
    {
      rest += n;
      while (rest > period)
      {
        value++;
        rest -= period;
        if (value > 0xffff)
        {
          if (nextTimer != null)
            nextTimer.addOverflowTime(value >>> 16);

          value &= 0xffff;

          if (irqEnabled)
	      ioMem.genInterrupt(interruptBit);
        }
        // do something
      }
    }
  }

  public void addOverflowTime(int n)
  {
    if (cascadeEnabled)
      addTime(period);
  }

  public void setPeriod(int i)
  {
    period = i;
  }

  public void setCascadeEnabled(boolean b)
  {
    cascadeEnabled = b;
  }

  public void setIRQEnabled(boolean b)
  {
    irqEnabled = b;
  }

  public void setTimerEnabled(boolean b)
  {
    timerEnabled = b;
  }

}
