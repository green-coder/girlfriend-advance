package gfa.time;

import gfa.gfx.*;
import gfa.memory.*;
import gfa.debug.*;

public class RenderTimer
{
  private int hValue;
  private int vValue;
  private boolean oldIsHBlank;
  private boolean oldIsVBlank;
  private IORegisterSpace_8_16_32 ioMem;
  private Lcd lcd;

  public RenderTimer()
  {
    reset();
  }

  public void connectToMemory(GfaMMU memory)
  {
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(0x04);
  }

  public void connectToLcd(Lcd lcd)
  {
    this.lcd = lcd;
  }

  public void reset()
  {
    hValue = 0;
    vValue = 0;
    oldIsHBlank = false;
    oldIsVBlank = false;
  }

    /*public int getTime()
  {
    return vValue;
    }*/

  public void addTime(int nbCycles)
  {
    hValue += nbCycles;

    boolean isHBlank = (hValue >= 240 * 4);
    boolean isVBlank = (vValue >= 160);

    ioMem.setHBlank(isHBlank);
    ioMem.setVBlank(isVBlank);

    if (!oldIsHBlank && isHBlank)
    {
      ioMem.genInterrupt(ioMem.hBlankInterruptBit);
      lcd.drawLine(vValue);
    }
    
    if (!oldIsVBlank && isVBlank)
	ioMem.genInterrupt(ioMem.vBlankInterruptBit);
    
    oldIsHBlank = isHBlank;
    oldIsVBlank = isVBlank;

    if (hValue >= 308 * 4)
    {
      hValue -= 308 * 4;

      vValue++;
      if (vValue >= 228)
        vValue = 0;

      ioMem.setYScanline(vValue);
    }
  }

}
