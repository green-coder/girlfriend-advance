package gfa.cpu;

import gfa.memory.MemoryInterface;

public class Arm7TdmiGen2 extends Arm7Tdmi {

  public InstructionDecoder decoder;

  public Arm7TdmiGen2() {
    super();
  }

  public void connectToMemory(MemoryInterface memory) {
    super.connectToMemory(memory);
    decoder = new InstructionDecoder(allRegisters, memory);
  }
  
  public String disassembleArmInstruction(int offset) {
    return decoder.decodeArmInstruction(memory.directLoadWord(offset)).disassemble(offset);
  }

  public String disassembleThumbInstruction(int offset) {
    return decoder.decodeThumbInstruction(memory.directLoadHalfWord(offset)).disassemble(offset);
  }

  /*
  protected ArmReg newArmReg(int v) {
    return new ArmRegObserver(new ArmReg(v));
  }
  */
  
  public void step() {
    int instructionTime;

    // Handle IRQ
    if (!CPSR.isBitSet(iFlagBit) &&
	(memory.loadByte(REG_IME_Address) != 0) &&
	((memory.loadHalfWord(REG_IE_Address) & memory.loadHalfWord(REG_IF_Address)) != 0))
	//(memory.loadHalfWord(REG_IF_Address) != 0))
    {
      //System.out.println("IRQ in (PC = " + PC + ")");
      getRegister(14, irqModeBits).set(PC.get() + 4); // LR <- PC + 4
      SPSR_irq.set(CPSR);     // SPSR_irq <- CPSR
      setMode(irqModeBits);   // CPSR changed to mode irq
      PC.set(irqVectorAddress);
      setArmState();          // interrupt handler is written in armState asm.
      CPSR.setOn(iFlagBit);   // inhibe interrupts
      instructionTime = 4;    // inaccurate
      //System.out.println("IRQ out");
      //stopPlease();
    }
    
    else if (isInThumbState()) {
      short opcode = memory.directLoadHalfWord(PC.get());
      PC.add(2); // add the size of a halfword.
      decoder.decodeThumbInstruction(opcode).execute();
      instructionTime = 2; // inaccurate
    }
    else {
      int opcode = memory.directLoadWord(PC.get());
      PC.add(4); // add the size of a word.
      decoder.decodeArmInstruction(opcode).execute();
      instructionTime = 4; // inaccurate
    }

    time.addTime(instructionTime);
  }

}
