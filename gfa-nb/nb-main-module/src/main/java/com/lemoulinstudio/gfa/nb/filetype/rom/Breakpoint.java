package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.analysis.BoolExpr;
import com.lemoulinstudio.gfa.analysis.BoolFalse;
import com.lemoulinstudio.gfa.analysis.ParseException;
import com.lemoulinstudio.gfa.analysis.Parser;
import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.CpuStepListener;

public class Breakpoint implements CpuStepListener {

  private GfaDevice device;
  private String sourceCode;
  private BoolExpr conditionExpr;

  public Breakpoint(GfaDevice gfaDevice) {
    this.device = gfaDevice;
    clearCondition();
  }

  public final void setCondition(String sourceCode) throws ParseException {
    device.getMemory().clearListeners();
    Parser parser = new Parser(device.getMemory(), device.getCpu());
    conditionExpr = parser.parse(sourceCode);
    this.sourceCode = sourceCode;
  }

  public final void clearCondition() {
    conditionExpr = new BoolFalse();
    sourceCode = "#f";
  }

  public String getSourceCode() {
    return sourceCode;
  }

  public void notifyCpuStepped() {
    if (conditionExpr.evaluation())
      device.getCpu().requestStop();

    conditionExpr.clearStatus();
  }

}
