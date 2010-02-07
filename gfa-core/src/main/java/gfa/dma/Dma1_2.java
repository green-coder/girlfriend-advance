package gfa.dma;

import gfa.memory.*;
import gfa.debug.*;
import gfa.util.*;

public class Dma1_2
  extends Dma
{
  public Dma1_2(String name)
  {
    super(name);
  }

  public void setSrcHRegister(short srcH)
  {src = (src & 0x0000ffff) | ((int) (srcH & 0x0fff) << 16);}

  public void setDstHRegister(short dstH)
  {dst = (dst & 0x0000ffff) | ((int) (dstH & 0x07ff) << 16);}

  public void setCountRegister(short count)
  {this.count = (short) (count & 0x3fff);}

}
