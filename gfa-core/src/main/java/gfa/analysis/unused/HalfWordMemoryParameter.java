package gfa.cpu.instruction;

/**
 * This class represent an intruction's parameter which is a half-word of the memory.
 * The instances of this class are immutable.
 */
public class HalfWordMemoryParameter
  extends MemoryParameter
{
  /**
   * The constructor of this class.
   */
  public HalfWordMemoryParameter(int offset, boolean readStatus, boolean writtenStatus)
  {
    super(offset, readStatus, writtenStatus);
  }

}
