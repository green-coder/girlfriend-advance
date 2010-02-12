package com.lemoulinstudio.gfa.core.input;

import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.memory.IORegisterSpace_8_16_32;
import java.util.EnumMap;
import java.util.EnumSet;

/**
 *
 * @author Vincent Cantin
 */
public class InputSystem {

  private IORegisterSpace_8_16_32 ioMem;
  private short keyConf = 0x03ff; // This is when all keys are released.

  public void connectToMemory(GfaMMU memory) {
    ioMem = (IORegisterSpace_8_16_32) memory.getMemoryBank(0x04);
  }

  public boolean getInputKeyState(InputKey inputKey) {
    return (keyConf & inputKey.getBitMask()) == 0;
  }

  public void setInputKeyState(InputKey inputKey, boolean isPushed) {
    if (isPushed) keyConf &= ~inputKey.getBitMask();
    else keyConf |= inputKey.getBitMask();

    ioMem.setReg16(0x0130, keyConf);
  }

}
