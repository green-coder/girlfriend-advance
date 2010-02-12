package com.lemoulinstudio.gfa.core.input;

/**
 *
 * @author Vincent Cantin
 */
// Note: The order of the elements matters.
public enum InputKey {
  A,
  B,
  Select,
  Start,
  Right,
  Left,
  Up,
  Down,
  R,
  L;

  public short getBitMask() {
    return (short) (0x0001 << ordinal());
  }
}
