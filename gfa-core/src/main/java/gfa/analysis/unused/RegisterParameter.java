package gfa.cpu.instruction;

import gfa.cpu.ArmReg;

/**
 * This class represent an intruction's parameter which is a register.
 * The instances of this class are immutable.
 */
public class RegisterParameter
  extends Parameter
{
  protected ArmReg register;

  /**
   * The constructor of this class.
   */
  public RegisterParameter(ArmReg register, boolean readStatus, boolean writtenStatus)
  {
    super(readStatus, writtenStatus);
    this.register = register;
  }

  /**
   * Return the reference of the register of this parameter.
   */
  public ArmReg getRegister()
  {
    return register;
  }

}
