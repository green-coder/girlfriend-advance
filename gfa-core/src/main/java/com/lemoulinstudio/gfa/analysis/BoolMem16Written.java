package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;

public class BoolMem16Written extends BoolMemWritten {

  public BoolMem16Written(IntExpr off, GfaMMU mem) {
    super(off, mem);
  }

  public void storeByte(int off, byte val) {
    if ((memory.getInternalOffset(off) & 0xfffffffe)
            == (memory.getInternalOffset(offset.evaluation()) & 0xfffffffe)) {
      hasBeenWritten = true;
    }
  }

  public void storeHalfWord(int off, short val) {
    if ((memory.getInternalOffset(off) & 0xfffffffe)
            == (memory.getInternalOffset(offset.evaluation()) & 0xfffffffe)) {
      hasBeenWritten = true;
    }
  }

  public void storeWord(int off, int val) {
    if ((memory.getInternalOffset(off) & 0xfffffffc)
            == (memory.getInternalOffset(offset.evaluation()) & 0xfffffffc)) {
      hasBeenWritten = true;
    }
  }
}
