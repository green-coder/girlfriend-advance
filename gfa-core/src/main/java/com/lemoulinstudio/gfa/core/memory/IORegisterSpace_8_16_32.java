package com.lemoulinstudio.gfa.core.memory;

import com.lemoulinstudio.gfa.core.dma.Dma;
import com.lemoulinstudio.gfa.core.gfx.Lcd;
import com.lemoulinstudio.gfa.core.time.Time;
import com.lemoulinstudio.gfa.core.time.Timer;

public class IORegisterSpace_8_16_32 extends MemoryManagementUnit {

  protected byte[] memory;
  protected int mirrorMask;
  protected Timer timer0;
  protected Timer timer1;
  protected Timer timer2;
  protected Timer timer3;
  protected Dma dma0;
  protected Dma dma1;
  protected Dma dma2;
  protected Dma dma3;
  protected Lcd lcd;

  public IORegisterSpace_8_16_32(String name, int size) {
    super(name);
    memory = new byte[size];
    mirrorMask = size - 1;

    // For now, all keys are released.
    setReg16(KeyAddress, (short) 0xffff);
  }

  public void connectToTime(Time time) {
    timer0 = time.timer0;
    timer1 = time.timer1;
    timer2 = time.timer2;
    timer3 = time.timer3;
  }

  public void connectToDma0(Dma dma0) {
    this.dma0 = dma0;
  }

  public void connectToDma1(Dma dma1) {
    this.dma1 = dma1;
  }

  public void connectToDma2(Dma dma2) {
    this.dma2 = dma2;
  }

  public void connectToDma3(Dma dma3) {
    this.dma3 = dma3;
  }

  public void connectToLcd(Lcd lcd) {
    this.lcd = lcd;
  }

  public void reset() {
    for (int i = 0; i < memory.length; i++)
      memory[i] = 0;

    // For now, all keys are released.
    setReg16(KeyAddress, (short) 0xffff);
  }

  public byte loadByte(int offset) {
    return read(offset);
  }

  public short loadHalfWord(int offset) {
    offset &= 0xfffffffe;
    return (short) ((read(offset + 1) << 8) |
                    (0xff & read(offset)));
  }

  public int loadWord(int offset) {
    offset &= 0xfffffffc;
    return ((read(offset + 3) << 24) |
            ((0xff & read(offset + 2)) << 16) |
            ((0xff & read(offset + 1)) <<  8) |
             (0xff & read(offset)));
  }

  public void storeByte(int offset, byte value) {
    write(offset, value);
  }

  public void storeHalfWord(int offset, short value) {
    offset &= 0xfffffffe;
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
  }

  public void storeWord(int offset, int value) {
    offset &= 0xfffffffc;
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 3, (byte) (value >> 24));
  }

  public byte swapByte(int offset, byte value) {
    byte result = read(offset);
    write(offset, value);
    return result;
  }

  public short swapHalfWord(int offset, short value) {
    offset &= 0xfffffffe;
    short result = (short) ((read(offset + 1) << 8) |
                             read(offset));
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
    return result;
  }

  public int swapWord(int offset, int value) {
    offset &= 0xfffffffc;
    int result = ((read(offset + 3) << 24) |
                  ((0xff & read(offset + 2)) << 16) |
                  ((0xff & read(offset + 1)) << 8)  |
                   (0xff & read(offset)));
    write(offset, (byte) value);
    write(offset + 1, (byte) (value >> 8));
    write(offset + 2, (byte) (value >> 16));
    write(offset + 3, (byte) (value >> 24));
    return result;
  }

  public final static int DMA0SrcLAddress = 0x000000b0;
  public final static int DMA0SrcHAddress = 0x000000b2;
  public final static int DMA0DstLAddress = 0x000000b4;
  public final static int DMA0DstHAddress = 0x000000b6;
  public final static int DMA0SizeAddress = 0x000000b8;
  public final static int DMA0CrAddress   = 0x000000ba;

  public final static int DMA1SrcLAddress = 0x000000bc;
  public final static int DMA1SrcHAddress = 0x000000be;
  public final static int DMA1DstLAddress = 0x000000c0;
  public final static int DMA1DstHAddress = 0x000000c2;
  public final static int DMA1SizeAddress = 0x000000c4;
  public final static int DMA1CrAddress   = 0x000000c6;

  public final static int DMA2SrcLAddress = 0x000000c8;
  public final static int DMA2SrcHAddress = 0x000000ca;
  public final static int DMA2DstLAddress = 0x000000cc;
  public final static int DMA2DstHAddress = 0x000000ce;
  public final static int DMA2SizeAddress = 0x000000d0;
  public final static int DMA2CrAddress   = 0x000000d2;

  public final static int DMA3SrcLAddress = 0x000000d4;
  public final static int DMA3SrcHAddress = 0x000000d6;
  public final static int DMA3DstLAddress = 0x000000d8;
  public final static int DMA3DstHAddress = 0x000000da;
  public final static int DMA3SizeAddress = 0x000000dc;
  public final static int DMA3CrAddress   = 0x000000de;

  protected byte read(int offset) {
    offset = getInternalOffset(offset);
    int off16 = offset & 0xfffffffe; // Offset aligned on halfWords.
    switch (off16) {
      case Timer0DataAdress: setReg16(off16, timer0.getTime()); break;
      case Timer1DataAdress: setReg16(off16, timer1.getTime()); break;
      case Timer2DataAdress: setReg16(off16, timer2.getTime()); break;
      case Timer3DataAdress: setReg16(off16, timer3.getTime()); break;

      case DMA0SrcLAddress: setReg16(off16, dma0.getSrcLRegister()); break;
      case DMA0SrcHAddress: setReg16(off16, dma0.getSrcHRegister()); break;
      case DMA0DstLAddress: setReg16(off16, dma0.getDstLRegister()); break;
      case DMA0DstHAddress: setReg16(off16, dma0.getDstHRegister()); break;
      case DMA0SizeAddress: setReg16(off16, dma0.getCountRegister()); break;
      case DMA0CrAddress:   setReg16(off16, dma0.getCrRegister()); break;

      case DMA1SrcLAddress: setReg16(off16, dma1.getSrcLRegister()); break;
      case DMA1SrcHAddress: setReg16(off16, dma1.getSrcHRegister()); break;
      case DMA1DstLAddress: setReg16(off16, dma1.getDstLRegister()); break;
      case DMA1DstHAddress: setReg16(off16, dma1.getDstHRegister()); break;
      case DMA1SizeAddress: setReg16(off16, dma1.getCountRegister()); break;
      case DMA1CrAddress:   setReg16(off16, dma1.getCrRegister()); break;

      case DMA2SrcLAddress: setReg16(off16, dma2.getSrcLRegister()); break;
      case DMA2SrcHAddress: setReg16(off16, dma2.getSrcHRegister()); break;
      case DMA2DstLAddress: setReg16(off16, dma2.getDstLRegister()); break;
      case DMA2DstHAddress: setReg16(off16, dma2.getDstHRegister()); break;
      case DMA2SizeAddress: setReg16(off16, dma2.getCountRegister()); break;
      case DMA2CrAddress:   setReg16(off16, dma2.getCrRegister()); break;

      case DMA3SrcLAddress: setReg16(off16, dma3.getSrcLRegister()); break;
      case DMA3SrcHAddress: setReg16(off16, dma3.getSrcHRegister()); break;
      case DMA3DstLAddress: setReg16(off16, dma3.getDstLRegister()); break;
      case DMA3DstHAddress: setReg16(off16, dma3.getDstHRegister()); break;
      case DMA3SizeAddress: setReg16(off16, dma3.getCountRegister()); break;
      case DMA3CrAddress:   setReg16(off16, dma3.getCrRegister()); break;
    }

    return memory[offset];
  }

  protected void write(int offset, byte value) {
    offset = getInternalOffset(offset);

    int off16 = offset & 0xfffffffe; // Offset aligned on halfWords.
    int off8  = offset & 1;          // The rest of alignment.
    
    short val16 = getReg16(off16);
    
    if (off8 == 0) val16 = (short) ((val16 & 0x0000ff00) | (value & 0x000000ff));
    else val16 = (short) ((val16 & 0x000000ff) | ((value & 0x000000ff) << 8));
    
    switch (off16) {
      case Timer0DataAdress: timer0.setTime(val16); break;
      case Timer1DataAdress: timer1.setTime(val16); break;
      case Timer2DataAdress: timer2.setTime(val16); break;
      case Timer3DataAdress: timer3.setTime(val16); break;

      case Timer0CrAdress: updateTimerState(timer0, val16); break;
      case Timer1CrAdress: updateTimerState(timer1, val16); break;
      case Timer2CrAdress: updateTimerState(timer2, val16); break;
      case Timer3CrAdress: updateTimerState(timer3, val16); break;

      case DMA0SrcLAddress: dma0.setSrcLRegister(val16); break;
      case DMA0SrcHAddress: dma0.setSrcHRegister(val16); break;
      case DMA0DstLAddress: dma0.setDstLRegister(val16); break;
      case DMA0DstHAddress: dma0.setDstHRegister(val16); break;
      case DMA0SizeAddress: dma0.setCountRegister(val16); break;
      case DMA0CrAddress:   dma0.setCrRegister(val16); break;

      case DMA1SrcLAddress: dma1.setSrcLRegister(val16); break;
      case DMA1SrcHAddress: dma1.setSrcHRegister(val16); break;
      case DMA1DstLAddress: dma1.setDstLRegister(val16); break;
      case DMA1DstHAddress: dma1.setDstHRegister(val16); break;
      case DMA1SizeAddress: dma1.setCountRegister(val16); break;
      case DMA1CrAddress:   dma1.setCrRegister(val16); break;

      case DMA2SrcLAddress: dma2.setSrcLRegister(val16); break;
      case DMA2SrcHAddress: dma2.setSrcHRegister(val16); break;
      case DMA2DstLAddress: dma2.setDstLRegister(val16); break;
      case DMA2DstHAddress: dma2.setDstHRegister(val16); break;
      case DMA2SizeAddress: dma2.setCountRegister(val16); break;
      case DMA2CrAddress:   dma2.setCrRegister(val16); break;

      case DMA3SrcLAddress:
        val16 = dma3.getSrcLRegister();
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setSrcLRegister(val16);
        break;

      case DMA3SrcHAddress:
        val16 = dma3.getSrcHRegister();
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setSrcHRegister(val16);
        break;

      case DMA3DstLAddress:
        val16 = dma3.getDstLRegister();
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setDstLRegister(val16);
        break;

      case DMA3DstHAddress:
        val16 = dma3.getDstHRegister();
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setDstHRegister(val16);
        break;

      case DMA3SizeAddress:
        val16 = dma3.getCountRegister();
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setCountRegister(val16);
        break;

      case DMA3CrAddress:
        val16 = dma3.getCrRegister();
        //System.out.println("val16 = " + Hex.toString(val16));
        if (off8 == 0) val16 = (short) ((val16 & 0xff00) | (0xff & value));
        else val16 = (short) ((val16 & 0xff) | (value << 8));
        dma3.setCrRegister(val16);
        break;
	
      case BG2XOriginLAddress: lcd.updateBG2XOriginL(val16); break;
      case BG2XOriginHAddress: lcd.updateBG2XOriginH(val16); break;
      case BG2YOriginLAddress: lcd.updateBG2YOriginL(val16); break;
      case BG2YOriginHAddress: lcd.updateBG2YOriginH(val16); break;
      
      case BG3XOriginLAddress: lcd.updateBG3XOriginL(val16); break;
      case BG3XOriginHAddress: lcd.updateBG3XOriginH(val16); break;
      case BG3YOriginLAddress: lcd.updateBG3YOriginL(val16); break;
      case BG3YOriginHAddress: lcd.updateBG3YOriginH(val16); break;
      
      case KeyAddress: return;
      case KeyAddress+1: return;

      case IFRegisterAddress:
	//System.out.println("Write dans IF " + Hex.toString(offset) + " : " + Hex.toString(value));
	memory[offset] &= ~value;
	return;

      case IERegisterAddress:
	//System.out.println("Write dans IE " + Hex.toString(offset) + " : " + Hex.toString(value));
	break;
      
      default:
    }
    
    memory[offset] = value;
  }

  public short getReg16(int offset) {
    return (short) ((memory[offset + 1] << 8) | (0xff & memory[offset]));
  }

  public final void setReg16(int offset, short value) {
    memory[offset] = (byte) value;
    memory[offset + 1] = (byte) (value >> 8);
  }

  public int getReg32(int offset) {
    return ((memory[offset + 3] << 24) |
	    ((0xff & memory[offset + 2]) << 16) |
	    ((0xff & memory[offset + 1]) << 8) |
	    (0xff & memory[offset]));
  }

  public void setReg32(int offset, int value) {
    memory[offset] = (byte) value;
    memory[offset + 1] = (byte) (value >> 8);
    memory[offset + 2] = (byte) (value >> 16);
    memory[offset + 3] = (byte) (value >> 24);
  }

  public final static int LCDRegisterAddress = 0x00000000; // The screen mode
  public final static int videoModeMask      = 0x00000007;
  public final static int videoCGBModeBit    = 0x00000008;
  public final static int videoFramBufSelBit = 0x00000010;
  public final static int videoOAMHBlankBit  = 0x00000020;
  public final static int video1DMappingBit  = 0x00000040;
  public final static int videoOAMBit        = 0x00001000;

  public final static int[] videoBGBit = {
    0x00000100, 0x00000200, 0x00000400, 0x00000800
  };

  public final static int DispSrAddress     = 0x00000004;
  public final static int DispSrInVBlankBit = 0x00000001;
  public final static int DispSrInHBlankBit = 0x00000002;

  public final static int LCYRegisterAddress = 0x00000006; // Current Y of the raytrace

  public final static int[] BGFormatRegisterAddress = {
    0x00000008, 0x0000000a, 0x0000000c, 0x0000000e
  };

  public final static int BGXPriorityMask           = 0x0003; // the priority of the background
  public final static int BGXTileDataAdressMask     = 0x000c; // the tile data address (0x6000000 + S * 0x4000)
  public final static int BGXMosaicEnabledBit       = 0x0040; // mosaic effect enabled (like on SNES)
  public final static int BGXFormatPalette256Color  = 0x0080; // if set : {256 colors * 1} else {16 colors * 16}
  public final static int BGXTileMapAdressMask      = 0x1f00; // the tile map address (0x6000000 + M * 0x800)
  public final static int BGXWrapAroundBit          = 0x2000; // if set : background repeats out of its limit else nothing displayed
  public final static int BGXFormatXNumberOfTileBit = 0x4000; // if set : horizontally {64 tiles} else {only 32}
  public final static int BGXFormatYNumberOfTileBit = 0x8000; // if set : vertically {64 tiles} else {only 32}

  // xScroll coordinate for BG
  public final static int[] BGSCX = {
    0x00000010, 0x00000014, 0x00000018, 0x0000001c
  };

  // yScroll coordinate for BG
  public final static int[] BGSCY = {
    0x00000012, 0x00000016, 0x0000001a, 0x0000001e
  };

  public final static int BGXScrollValueMask = 0x000003ff;

  public int getGfxMode() {
    return (getReg16(LCDRegisterAddress) & videoModeMask);
  }

  public boolean isCGBMode() {
    return ((getReg16(LCDRegisterAddress) & videoCGBModeBit) != 0);
  }

  public boolean is1DMappingMode() {
    return ((getReg16(LCDRegisterAddress) & video1DMappingBit) != 0);
  }

  public boolean isFrame1Mode() {
    return ((getReg16(LCDRegisterAddress) & videoFramBufSelBit) != 0);
  }

  public boolean isBGEnabled(int bgNumber) {
    return ((getReg16(LCDRegisterAddress) & videoBGBit[bgNumber]) != 0);
  }

  public int getBGPriority(int bgNumber) {
    return (getReg16(BGFormatRegisterAddress[bgNumber]) & BGXPriorityMask);
  }

  public boolean isBG_256Color(int bgNumber) {
    return ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXFormatPalette256Color) != 0);
  }

  public boolean isBGMosaicEnabled(int bgNumber) {
    return ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXMosaicEnabledBit) != 0);
  }

  public int getBGTileMapAddress(int bgNumber) {
    // Direct Hardware Access : no need an absolute address
    return /* 0x6000000 + */ ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXTileMapAdressMask) >>> 8) * 0x800;
  }

  public int getBGTileDataAddress(int bgNumber) {
    // Direct Hardware Access : no need an absolute address
    return /* 0x6000000 + */ ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXTileDataAdressMask) >>> 2) * 0x4000;
  }

  public int getBGXNumberOfTile(int bgNumber) {
    if ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXFormatXNumberOfTileBit) != 0)
      return 64;
    else
      return 32;
  }

  public int getBGYNumberOfTile(int bgNumber) {
    if ((getReg16(BGFormatRegisterAddress[bgNumber]) & BGXFormatYNumberOfTileBit) != 0)
      return 64;
    else
      return 32;
  }

  public int getBGSCX(int bgNumber) {
    return (getReg16(BGSCX[bgNumber]) & BGXScrollValueMask);
  }

  public int getBGSCY(int bgNumber) {
    return (getReg16(BGSCY[bgNumber]) & BGXScrollValueMask);
  }

  public boolean isSpriteEnabled() {
    return ((getReg16(LCDRegisterAddress) & videoOAMBit) != 0);
  }

  public final static int BG2XOriginLAddress = 0x0028;
  public final static int BG2XOriginHAddress = 0x002a;
  public final static int BG2YOriginLAddress = 0x002c;
  public final static int BG2YOriginHAddress = 0x002e;
  
  public int getBG2RotScalXOrigin() {
    return getReg32(BG2XOriginLAddress) & 0x0fffffff;
  }
  
  public int getBG2RotScalYOrigin() {
    return getReg32(BG2YOriginLAddress) & 0x0fffffff;
  }
  
  public final static int BG2PaAddress = 0x0020;
  public final static int BG2PbAddress = 0x0022;
  public final static int BG2PcAddress = 0x0024;
  public final static int BG2PdAddress = 0x0026;
  
  public short getBG2RotScalPA() {
    return getReg16(BG2PaAddress);
  }
  
  public short getBG2RotScalPB() {
    return getReg16(BG2PbAddress);
  }
  
  public short getBG2RotScalPC() {
    return getReg16(BG2PcAddress);
  }
  
  public short getBG2RotScalPD() {
    return getReg16(BG2PdAddress);
  }
  
  public int getBG2RotScalNumberOfTile() {
    switch ((getReg16(BGFormatRegisterAddress[2]) &
	     (BGXFormatXNumberOfTileBit |
	      BGXFormatYNumberOfTileBit)) >>> 14) {
      case 0: return 16;
      case 1: return 32;
      case 2: return 64;
      default: return 128;
    }
  }

  public final static int BG3XOriginLAddress = 0x0038;
  public final static int BG3XOriginHAddress = 0x003a;
  public final static int BG3YOriginLAddress = 0x003c;
  public final static int BG3YOriginHAddress = 0x003e;
  
  public int getBG3RotScalXOrigin() {
    return getReg32(BG3XOriginLAddress) & 0x0fffffff;
  }

  public int getBG3RotScalYOrigin() {
    return getReg32(BG3YOriginLAddress) & 0x0fffffff;
  }
  
  public final static int BG3PaAddress = 0x0030;
  public final static int BG3PbAddress = 0x0032;
  public final static int BG3PcAddress = 0x0034;
  public final static int BG3PdAddress = 0x0036;
  
  public short getBG3RotScalPA() {
    return getReg16(BG3PaAddress);
  }

  public short getBG3RotScalPB() {
    return getReg16(BG3PbAddress);
  }

  public short getBG3RotScalPC() {
    return getReg16(BG3PcAddress);
  }

  public short getBG3RotScalPD() {
    return getReg16(BG3PdAddress);
  }

  public int getBG3RotScalNumberOfTile() {
    switch ((getReg16(BGFormatRegisterAddress[3]) &
	     (BGXFormatXNumberOfTileBit |
	      BGXFormatYNumberOfTileBit)) >>> 14) {
      case 0: return 16;
      case 1: return 32;
      case 2: return 64;
      default: return 128;
    }
  }
  
  public boolean isBG2RotScalWrapAround() {
    return ((getReg16(BGFormatRegisterAddress[2]) & BGXWrapAroundBit) != 0);
  }

  public boolean isBG3RotScalWrapAround() {
    return ((getReg16(BGFormatRegisterAddress[3]) & BGXWrapAroundBit) != 0);
  }

  public final static int MosaicSizeRegisterAddress = 0x0000004c;
  public final static int MosaicRegisterBGXMask     = 0x000f;
  public final static int MosaicRegisterBGYMask     = 0x00f0;
  public final static int MosaicRegisterOBJXMask    = 0x0f00;
  public final static int MosaicRegisterOBJYMask    = 0xf000;

  public int getMosaicBGXSize() {
    return (getReg16(MosaicSizeRegisterAddress) & MosaicRegisterBGXMask) + 1;
  }

  public int getMosaicBGYSize() {
    return ((getReg16(MosaicSizeRegisterAddress) & MosaicRegisterBGYMask) >>> 4) + 1;
  }

  public int getMosaicOBJXSize() {
    return ((getReg16(MosaicSizeRegisterAddress) & MosaicRegisterOBJXMask) >>> 8) + 1;
  }

  public int getMosaicOBJYSize() {
    return ((getReg16(MosaicSizeRegisterAddress) & MosaicRegisterOBJYMask) >>> 12) + 1;
  }

  public final static int KeyAddress              = 0x00000130;
  public final static int KeyRegisterAddress      = 0x00000098; // The input register
  public final static int nAButtonBit             = 0x00000001;
  public final static int nBButtonBit             = 0x00000002;
  public final static int nSelectButtonBit        = 0x00000004;
  public final static int nStartButtonBit         = 0x00000008;
  public final static int nDPadRightButtonBit     = 0x00000010;
  public final static int nDPadLeftButtonBit      = 0x00000020;
  public final static int nDPadUpButtonBit        = 0x00000040;
  public final static int nDPadDownButtonBit      = 0x00000080;
  public final static int nRightShoulderButtonBit = 0x00000100;
  public final static int nLeftShoulderButtonBit  = 0x00000200;

  public final static int Timer0DataAdress = 0x00000100;
  public final static int Timer1DataAdress = 0x00000104;
  public final static int Timer2DataAdress = 0x00000108;
  public final static int Timer3DataAdress = 0x0000010c;

  public final static int Timer0CrAdress     = 0x00000102;
  public final static int Timer1CrAdress     = 0x00000106;
  public final static int Timer2CrAdress     = 0x0000010a;
  public final static int Timer3CrAdress     = 0x0000010e;
  public final static int TimerXCrFreqMask   = 0x00000003;
  public final static int TimerXCrCascadeBit = 0x00000004;
  public final static int TimerXCrIRQBit     = 0x00000040;
  public final static int TimerXCrEnabledBit = 0x00000080;

  protected void updateTimerState(Timer timer, short val16) {
    switch (val16 & TimerXCrFreqMask) {
      case 0: timer.setPeriod(1); break;
      case 1: timer.setPeriod(64); break;
      case 2: timer.setPeriod(256); break;
      case 3: timer.setPeriod(1024); break;
    }

    timer.setCascadeEnabled((val16 & TimerXCrCascadeBit) != 0);
    timer.setIRQEnabled((val16 & TimerXCrIRQBit) != 0);
    timer.setTimerEnabled((val16 & TimerXCrEnabledBit) != 0);
  }

  public void setVBlank(boolean b) {
    short val = getReg16(DispSrAddress);
    if (b) val |= DispSrInVBlankBit;
    else val &= ~DispSrInVBlankBit;
    setReg16(DispSrAddress, val);
  }

  public void setHBlank(boolean b) {
    short val = getReg16(DispSrAddress);
    if (b) val |= DispSrInHBlankBit;
    else val &= ~DispSrInHBlankBit;
    setReg16(DispSrAddress, val);
  }

  public int getVCountSetting() {
    return getReg16(DispSrAddress) >>> 8;
  }

  public boolean isVCountMatchInterruptEnabled() {
    return ((getReg16(DispSrAddress) & 0x0020) != 0);
  }

  public void setYScanline(int vValue) {
    //System.out.println("YScanline.set(" + vValue + ");");
    memory[LCYRegisterAddress] = (byte) vValue;
  }
  
  public final static int IERegisterAddress  = 0x00000200;
  public final static int IFRegisterAddress  = 0x00000202;
  public final static int IMERegisterAddress = 0x00000208;
  
  public final static short vBlankInterruptBit = 0x0001;
  public final static short hBlankInterruptBit = 0x0002;
  public final static short vCountInterruptBit = 0x0004;
  public final static short timer0InterruptBit = 0x0008;
  public final static short timer1InterruptBit = 0x0010;
  public final static short timer2InterruptBit = 0x0020;
  public final static short timer3InterruptBit = 0x0040;
  public final static short commInterruptBit   = 0x0080;
  public final static short dma0InterruptBit   = 0x0100;
  public final static short dma1InterruptBit   = 0x0200;
  public final static short dma2InterruptBit   = 0x0400;
  public final static short dma3InterruptBit   = 0x0800;
  public final static short keyInterruptBit    = 0x1000;
  //public final static short unknownButUsedBit  = 0x2000;
  
  protected boolean isInterruptMasterEnabled() {
    return ((memory[IMERegisterAddress] & 0x01) != 0);
  }

  protected boolean isInterruptEnabled(short interruptBit) {
    return ((getReg16(IERegisterAddress) & interruptBit) != 0);
  }

  public void genInterrupt(short interruptBit) {
    /*
    if (isInterruptMasterEnabled() &&
	isInterruptEnabled(interruptBit))
    {
      System.out.print("genInterrupt(" + Hex.toString(interruptBit) + ");");
      System.out.println(" SET !");
      setReg16(IFRegisterAddress, (short) (getReg16(IFRegisterAddress) | interruptBit));
    }
    else
      System.out.println(" failed .... IE=" + Hex.toString(getReg16(IERegisterAddress)) + "... IME=" + isInterruptMasterEnabled());
    */

    setReg16(IFRegisterAddress, (short) (getReg16(IFRegisterAddress) |
                                        (getReg16(IERegisterAddress) & interruptBit)));
  }

  public int getInternalOffset(int offset) {
    return offset & mirrorMask;
  }

}
