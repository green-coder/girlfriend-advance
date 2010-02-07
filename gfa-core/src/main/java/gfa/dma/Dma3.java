package gfa.dma;

import gfa.memory.*;

public class Dma3
  extends Dma
{
  public Dma3()
  {
    super("dma3");
    interruptBit = IORegisterSpace_8_16_32.dma3InterruptBit;
    countMaxValue = 0x00010000;
  }

  public void setSrcHRegister(short srcH)
  {src = (src & 0x0000ffff) | ((int) (srcH & 0x0fff) << 16);}

  public void setDstHRegister(short dstH)
  {dst = (dst & 0x0000ffff) | ((int) (dstH & 0x0fff) << 16);}

}
