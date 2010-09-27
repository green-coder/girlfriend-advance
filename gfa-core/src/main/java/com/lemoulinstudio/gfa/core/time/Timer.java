package com.lemoulinstudio.gfa.core.time;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import com.lemoulinstudio.gfa.core.memory.var.Mem16Var;

public class Timer {

  private final static int CrFreqMask   = 0x0003;
  private final static int CrCascadeBit = 0x0004;
  private final static int CrIRQBit     = 0x0040;
  private final static int CrEnabledBit = 0x0080;

  private final static int[] periodTable = new int[] {1, 64, 256, 1024};

  public Mem16Var data;
  public Mem16Var cr;

  private final int timerNumber;
  private int rest;   // Number of cycles from last value change.
  private int period; // Number of cycles between 2 differents values of this timer.

  private Timer nextTimer;     // The timer where to add carry bit if it is in cascade mode.
  private boolean cascadeEnabled; // if true, receive the previous timer overflow.
  private boolean irqEnabled;     // If true, throw an IRQ when an overflow occurs.
  private boolean timerEnabled;   // If false, the timer is halted.

  private IORegisterSpace_8_16_32 ioMem;

  public Timer(Timer nextTimer, int timerNumber) {
    this.nextTimer = nextTimer;
    this.timerNumber = timerNumber;
  }

  public void connectToMemory(GfaMMU memory) {
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(4);

    data = new Mem16Var() {
      public short getValue() {
        return ioMem.getReg16(IORegisterSpace_8_16_32.TimerDataAdress[timerNumber]);
      }

      public void setValue(short value) {
        ioMem.setReg16(IORegisterSpace_8_16_32.TimerDataAdress[timerNumber], value);
        rest = 0;
      }
    };

    cr = new Mem16Var() {
      public short getValue() {
        return ioMem.getReg16(IORegisterSpace_8_16_32.TimerCrAdress[timerNumber]);
      }

      public void setValue(short value) {
        ioMem.setReg16(IORegisterSpace_8_16_32.TimerCrAdress[timerNumber], (short) (value & 0xc7));
        period         = periodTable[value & CrFreqMask];
        cascadeEnabled = ((value & CrCascadeBit) != 0);
        irqEnabled     = ((value & CrIRQBit) != 0);
        timerEnabled   = ((value & CrEnabledBit) != 0);
      }
    };
  }
  
  public final void reset() {
    data.setValue((short) 0);
    period         = 1;
    cascadeEnabled = false;
    irqEnabled     = false;
    timerEnabled   = false;
  }

  public void addTime(int n) {
    if (timerEnabled) {
      rest += n;
      while (rest > period) {
        rest -= period;
        
        int value = 0xffff & data.getValue();
        value++;
        data.setValue((short) value);
        
        if (value > 0xffff) {
          if (nextTimer != null)
            nextTimer.addOverflowTime(value >>> 16);

          if (irqEnabled)
            ioMem.genInterrupt(IORegisterSpace_8_16_32.timerInterruptBit[timerNumber]);
        }
      }
    }
  }

  public void addOverflowTime(int n) {
    if (cascadeEnabled)
      addTime(period);
  }

}
