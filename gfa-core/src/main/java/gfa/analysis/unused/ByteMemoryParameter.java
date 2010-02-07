package gfa.cpu.instruction;

/**
 * This class represent an intruction's parameter which is a byte of the memory.
 * The instances of this class are immutable.
 */
public class ByteMemoryParameter
  extends MemoryParameter
{
  /**
   * The constructor of this class.
   */
  public ByteMemoryParameter(int offset, boolean readStatus, boolean writtenStatus)
  {
    super(offset, readStatus, writtenStatus);
  }

}
