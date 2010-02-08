package gfa.cpu.instruction;

import gfa.cpu.ArmReg;
import gfa.memory.MemoryInterface;

public class ArmStateLdmStm extends ArmStateInstruction {

  public ArmStateLdmStm(ArmReg[][] regs, MemoryInterface memory) {
    super(regs, memory);
  }

  static final protected int PreIndexingBit = 0x01000000;
  static final protected int UpDownBit      = 0x00800000;
  static final protected int PSRBit         = 0x00400000;
  static final protected int WriteBackBit   = 0x00200000;
  static final protected int LoadStoreBit   = 0x00100000;
  static final protected int RnMask         = 0x000f0000;
  static final protected int R15Bit         = 0x00008000;

  public void execute() {
    if (!isPreconditionSatisfied()) return;
    
    int baseRegisterNumber = (opcode & RnMask) >>> 16;
    ArmReg baseRegister = getRegister(baseRegisterNumber);
    int stackAdress = baseRegister.get() & 0xfffffffc;
    
    int nbRegistersToSaveOrToLoad = 0; // We count the number of registers to save/load.
    for (int i = 0; i <= 15; i++)
      if ((opcode & (1 << i)) != 0)
        nbRegistersToSaveOrToLoad++;
    
    int newBaseRegisterValue;
    if ((opcode & UpDownBit) != 0) { // Up
      newBaseRegisterValue = stackAdress + nbRegistersToSaveOrToLoad * 4;
      if ((opcode & PreIndexingBit) != 0) // Pre-increment
        stackAdress += 4;
    }
    else {                   // Down
      newBaseRegisterValue = stackAdress - nbRegistersToSaveOrToLoad * 4;
      stackAdress = newBaseRegisterValue;
      if ((opcode & PreIndexingBit) == 0) // Post-decrement
          stackAdress += 4;
    }
    
    if ((opcode & LoadStoreBit) == 0) { // Store
      int mode = getMode();
      if ((opcode & PSRBit) != 0)
        mode = usrModeBits;
      
      int i = 0;
      for (; i < 15; i++) // Empile le premier registre : debut du deuxieme cycle
        if ((opcode & (1 << i)) != 0) {
          memory.storeWord(stackAdress, getRegister(i, mode).get());
	  stackAdress += 4;
	  i++;
	  break;
	}
      
      if ((opcode & WriteBackBit) != 0) // fin du deuxieme cycle
        baseRegister.set((baseRegister.get() & 0x00000003) | newBaseRegisterValue); // update
      
      for (; i < 15; i++) // Empile les autres registres jusqu'a 14
        if ((opcode & (1 << i)) != 0) {
          memory.storeWord(stackAdress, getRegister(i, mode).get());
	  stackAdress += 4;
	}
      
      if ((opcode & R15Bit) != 0) { // Le cas de R15 + 12.
        memory.storeWord(stackAdress, PC.get() + 8);
	stackAdress += 4;
      }
    }
    else {                           // Load
      if ((opcode & WriteBackBit) != 0) // Write Back
        baseRegister.set((baseRegister.get() & 0x00000003) | newBaseRegisterValue);
      
      int mode = getMode();
      if (((opcode & PSRBit) != 0) &&
	  ((opcode & R15Bit) == 0))
        mode = usrModeBits;
      
      for (int i = 0; i < 15; i++) // Depile les registres de 0 a 14
        if ((opcode & (1 << i)) != 0) {
          getRegister(i, mode).set(memory.loadWord(stackAdress));
	  stackAdress += 4;
	}
      
      if ((opcode & R15Bit) != 0) {
        PC.set(memory.loadWord(stackAdress) & 0xfffffffe);
	if ((opcode & PSRBit) != 0)
          CPSR.set(getSPSR());
	stackAdress += 4;
      }
    }
  }

  public String disassemble(int offset) {
    int opcode = getOpcode(offset);
    String instru = ((opcode & LoadStoreBit) == 0) ? "stm" : "ldm";
    instru += preconditionToString(opcode);
    int rnNum = ((opcode & RnMask) >>> 16);
    if (rnNum == 13)
      instru += (((opcode & PreIndexingBit) != 0) ? "e" : "f") +
	        (((opcode & UpDownBit) != 0) ? "d" : "a");
    else
      instru += (((opcode & UpDownBit) != 0) ? "i" : "d") +
                (((opcode & PreIndexingBit) != 0) ? "b" : "a");
    String rn = getRegisterName(rnNum);
    if ((opcode & WriteBackBit) != 0) rn += "!";
    String registerList = "";
    for (int i = 0; i < 16; i++)
      if ((opcode & (1 << i)) != 0)
	registerList += ", r" + i;
    registerList = "{" + ((registerList.length() < 2) ? "" : registerList.substring(2)) + "}";
    if ((opcode & PSRBit) != 0) registerList += "^";
    return instru + " " + rn + ", " + registerList;
  }

}
