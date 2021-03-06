package com.lemoulinstudio.gfa.nb.breakpoint;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class BoolMem8Read extends BoolMemRead {

  public BoolMem8Read(IntExpr off, GfaMMU mem) {
    super(off, mem);
  }

  public void loadByte(int off) {
    if (memory.getInternalOffset(off)
            == memory.getInternalOffset(offset.evaluation())) {
      hasBeenRead = true;
    }
  }

  public void loadHalfWord(int off) {
    if ((memory.getInternalOffset(off) & 0xfffffffe)
            == (memory.getInternalOffset(offset.evaluation()) & 0xfffffffe)) {
      hasBeenRead = true;
    }
  }

  public void loadWord(int off) {
    if ((memory.getInternalOffset(off) & 0xfffffffc)
            == (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc)) {
      hasBeenRead = true;
    }
  }
}
