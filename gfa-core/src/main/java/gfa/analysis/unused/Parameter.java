package gfa.analysis;

/**
 * This class represent an intruction's parameter. 
 * It's the super-class of all non-constant parameters.
 */
public abstract class Parameter
{
  protected boolean readStatus;
  protected boolean writtenStatus;

  /**
   * The constructor of this class.
   * "readStatus" and "writtenStatus" defines the way this parameter is used for.
   */
  public Parameter(boolean readStatus, boolean writtenStatus)
  {
    this.readStatus = readStatus;
    this.writtenStatus = writtenStatus;
  }

  /**
   * Return true if this parameter is read by its instruction, else return false.
   */
  public boolean isRead()
  {
    return readStatus;
  }

  /**
   * Return true if this parameter is written by its instruction, else return false.
   */
  public boolean isWritten()
  {
    return writtenStatus;
  }

}
