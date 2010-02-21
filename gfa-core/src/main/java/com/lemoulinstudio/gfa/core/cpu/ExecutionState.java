package com.lemoulinstudio.gfa.core.cpu;

/**
 *
 * @author Vincent Cantin
 */
public enum ExecutionState {
  Thumb(2, 1),
  Arm(4, 2);

  private int instructionSize;
  private int instructionBitWidth;

  private ExecutionState(int instructionSize, int instructionBitWidth) {
    this.instructionSize = instructionSize;
    this.instructionBitWidth = instructionBitWidth;
  }

  public int getInstructionSize() {
    return instructionSize;
  }

  public int getInstructionBitWidth() {
    return instructionBitWidth;
  }

}
