package com.lemoulinstudio.gfa.core.util;

public class Hex {

  protected final static char[] digits = {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  public static String toString(byte n) {
    StringBuffer sb = new StringBuffer(2);
    sb.append(digits[(n >>> 4) & 0x0000000f]);
    sb.append(digits[n & 0x0000000f]);
    return sb.toString();
  }

  public static String toString(short n) {
    StringBuffer sb = new StringBuffer(4);
    for (int i = 3; i >= 0; i--)
      sb.append(digits[(n >>> (i * 4)) & 0x0000000f]);
    return sb.toString();
  }

  public static String toString(int n) {
    StringBuffer sb = new StringBuffer(8);
    for (int i = 7; i >= 0; i--)
      sb.append(digits[(n >>> (i * 4)) & 0x0000000f]);
    return sb.toString();
  }

  public static String toString(long n) {
    StringBuffer sb = new StringBuffer(16);
    for (int i = 15; i >= 0; i--)
      sb.append(digits[(int) (n >>> (i * 4)) & 0x0000000f]);
    return sb.toString();
  }
  
}
