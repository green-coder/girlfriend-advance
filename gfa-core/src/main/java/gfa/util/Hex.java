package gfa.util;

public class Hex
{
  public static String toString(byte n)
  {
    String s = Integer.toHexString(0xff & n);
    while (s.length() < 2) s = "0" + s;
    return "0x" + s;
  }

  public static String toString(short n)
  {
    String s = Integer.toHexString(0xffff & n);
    while (s.length() < 4) s = "0" + s;
    return "0x" + s;
  }

  public static String toString(int n)
  {
    String s = Integer.toHexString(n);
    while (s.length() < 8) s = "0" + s;
    return "0x" + s;
  }

  public static String toString(long n)
  {
    String s = Long.toHexString(n);
    while (s.length() < 16) s = "0" + s;
    return "0x" + s;
  }
}
