package gfa.cpu.instruction;

/**
 * This class represent an intruction's parameter which is a word of the memory.
 * The instances of this class are immutable.
 */
public class WordMemoryParameter
  extends MemoryParameter
{
  /**
   * The constructor of this class.
   */
  public WordMemoryParameter(int offset, boolean readStatus, boolean writtenStatus)
  {
    super(offset, readStatus, writtenStatus);
  }

}
