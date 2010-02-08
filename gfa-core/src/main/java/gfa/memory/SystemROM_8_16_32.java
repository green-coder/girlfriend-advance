package gfa.memory;

public class SystemROM_8_16_32 extends MemoryManagementUnit_8_16_32 {

  public SystemROM_8_16_32(String name, int size) {
    super(name, size);

    // BIOS emulation.
    // It comes from mappy. I don't really know what it should do,
    // but I know that it's better than having a memory all cleared
    // with zeros when a SWI occurs.
    // Copyright Joat (it's written in ASCII format in the words just below).
    setReg32(0x00000000, 0xea000015);
    setReg32(0x00000004, 0xea000016);
    setReg32(0x00000008, 0xea00000a);
    setReg32(0x0000000c, 0xea000016);
    setReg32(0x00000010, 0xea000015);
    setReg32(0x00000014, 0xea000016);
    setReg32(0x00000018, 0xea000000);
    setReg32(0x0000001c, 0xea000015);
    setReg32(0x00000020, 0xe92d500f);
    setReg32(0x00000024, 0xe28fe004);
    setReg32(0x00000028, 0xe59f005c);
    setReg32(0x0000002c, 0xe590f000);
    setReg32(0x00000030, 0xe8bd500f);
    setReg32(0x00000034, 0xe25ef004);
    setReg32(0x00000038, 0xe92d5800);
    setReg32(0x0000003c, 0xea000004);
    setReg32(0x00000040, 0xe55ec002);
    setReg32(0x00000044, 0xe35c001e);
    setReg32(0x00000048, 0xaa000001);
    setReg32(0x0000004c, 0xe59fb03c);
    setReg32(0x00000050, 0xe79bc10c);
    setReg32(0x00000054, 0xe8bd5800);
    setReg32(0x00000058, 0xe1b0f00e);
    setReg32(0x0000005c, 0xe3a0f302);
    setReg32(0x00000060, 0xea000006);
    setReg32(0x00000064, 0xe1b0f00e);
    setReg32(0x00000068, 0xea000004);
    setReg32(0x0000006c, 0xe25ef004);
    setReg32(0x00000070, 0xea000002);
    setReg32(0x00000074, 0xea000001);
    setReg32(0x00000078, 0xe25ef004);
    setReg32(0x0000007c, 0xeaffffff);
    setReg32(0x00000080, 0xeafffffe);
    setReg32(0x00000084, 0x74616f4a);
    setReg32(0x00000088, 0x00000a0d);
    setReg32(0x0000008c, 0x03007ffc);
    setReg32(0x00000090, 0x00000084);
  }

  protected void setReg32(int offset, int value) {
    offset = getInternalOffset(offset);
    offset &= 0xfffffffc; // offset is now word-aligned
    memory[offset + 0] = (byte) value;
    memory[offset + 1] = (byte) (value >>> 8);
    memory[offset + 2] = (byte) (value >>> 16);
    memory[offset + 3] = (byte) (value >>> 24);
  }

  protected byte read(int offset) {
    offset = getInternalOffset(offset);
    return memory[offset];
  }

  protected void write(int offset, byte value) {
    //System.out.println("Ecriture dans la rom system a l'offset " + gfa.util.Hex.toString(offset) + " !!!");
    //gfa.debug.StepFrame.BREAK();
    //memory[offset] = value;
  }

  public void reset() {
  }
  
}
