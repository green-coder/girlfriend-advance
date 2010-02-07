package gfa.cpu.instruction;

/**
 * This class represent an intruction's parameter which is an element of the memory.
 * The instances of this class are immutable.
 */
public abstract class MemoryParameter
  extends Parameter
{
  protected int offset;

  /**
   * The constructor of this class.
   */
  public MemoryParameter(int offset, boolean readStatus, boolean writtenStatus)
  {
    super(readStatus, writtenStatus);
    this.offset = offset;
  }

  /**
   * Return the offset of the memory element concerned.
   */
  public int getOffset()
  {
    return offset;
  }
}
