package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.analysis.BoolExpr;
import com.lemoulinstudio.gfa.analysis.BoolFalse;
import com.lemoulinstudio.gfa.analysis.ParseException;
import com.lemoulinstudio.gfa.analysis.Parser;
import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.CpuStepListener;

public class Breakpoint implements CpuStepListener {

  private GfaDevice gfaDevice;
  private BoolExpr conditionExpr;

  public Breakpoint(GfaDevice gfaDevice) {
    this.gfaDevice = gfaDevice;
    clearCondition();
  }

  public final void setCondition(String sourceCode) throws ParseException {
    Parser parser = new Parser(gfaDevice.getMemory(), gfaDevice.getCpu());
    conditionExpr = parser.parse(sourceCode);
  }

  public final void clearCondition() {
    conditionExpr = new BoolFalse();
  }

  public void notifyCpuStepped() {
    if (conditionExpr.evaluation())
      gfaDevice.getCpu().requestStop();
  }

}
