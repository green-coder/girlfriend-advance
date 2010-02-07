package gfa;

import gfa.cpu.*;
import gfa.memory.*;
import gfa.gfx.*;
import gfa.dma.*;
import gfa.time.*;

import java.net.*;
import java.awt.event.*;

public class GirlfriendAdvance
  implements KeyListener
{
  protected Arm7Tdmi cpu;
  protected GfaMMU mem;
  protected Dma dma0;
  protected Dma dma1;
  protected Dma dma2;
  protected Dma dma3;
  protected Time time;
  protected Lcd lcd;

  public GirlfriendAdvance()
  {
    this(new Arm7TdmiGen2());
  }

  public GirlfriendAdvance(Arm7Tdmi cpu)
  {
    this.cpu  = cpu;
    mem  = new GfaMMU();
    dma0 = new Dma0();
    dma1 = new Dma1();
    dma2 = new Dma2();
    dma3 = new Dma3();
    time = new Time();
    lcd  = new Lcd();
    
    setupConnections();
    reset();
    
    // Needed for handle the keyboard
    ioMem = (IORegisterSpace_8_16_32) mem.getMemoryBank(0x04);
  }

  public void reset()
  {
    getTime().reset();
    getCpu().reset();
    getDma0().reset();
    getDma1().reset();
    getDma2().reset();
    getDma3().reset();
    getMemory().reset();
  }
  
  protected void setupConnections()
  {
    cpu.connectToMemory(mem);
    cpu.connectToTime(time);
    mem.connectToTime(time);
    mem.connectToDma0(dma0);
    mem.connectToDma1(dma1);
    mem.connectToDma2(dma2);
    mem.connectToDma3(dma3);
    mem.connectToLcd(lcd);
    dma0.connectToMemory(mem);
    dma1.connectToMemory(mem);
    dma2.connectToMemory(mem);
    dma3.connectToMemory(mem);
    /* $$$ for debug purpose */ Arm7Tdmi.cpu = cpu;
    time.connectToMemory(mem);
    time.connectToLcd(lcd);
    time.connectToDma0(dma0);
    time.connectToDma1(dma1);
    time.connectToDma2(dma2);
    time.connectToDma3(dma3);
    lcd.connectToMemory(mem);
  }

  public Arm7Tdmi getCpu()    {return cpu;}
  public GfaMMU   getMemory() {return mem;}
  public Dma      getDma0()   {return dma0;}
  public Dma      getDma1()   {return dma1;}
  public Dma      getDma2()   {return dma2;}
  public Dma      getDma3()   {return dma3;}
  public Lcd      getLcd()    {return lcd;}
  public Time     getTime()   {return time;}


  protected IORegisterSpace_8_16_32 ioMem;
  protected short keyConf = 0x03ff;

  public void keyPressed(KeyEvent e)
  {
    //System.out.println("keyPressed(KeyEvent e)");

    int keyCode = e.getKeyCode();
    
    switch (keyCode)
    {
      case KeyEvent.VK_A:     keyConf &= ~0x0001; break; // The A button
      case KeyEvent.VK_B:     keyConf &= ~0x0002; break; // The B button
      case KeyEvent.VK_SHIFT: keyConf &= ~0x0004; break; // The SELECT button
      case KeyEvent.VK_ENTER: keyConf &= ~0x0008; break; // The START button
      case KeyEvent.VK_RIGHT: keyConf &= ~0x0010; break; // The RIGHT paddle
      case KeyEvent.VK_LEFT:  keyConf &= ~0x0020; break; // The LEFT paddle
      case KeyEvent.VK_UP:    keyConf &= ~0x0040; break; // The UP paddle
      case KeyEvent.VK_DOWN:  keyConf &= ~0x0080; break; // The DOWN paddle
      case KeyEvent.VK_R:     keyConf &= ~0x0100; break; // The R button
      case KeyEvent.VK_L:     keyConf &= ~0x0200; break; // The L button
      default: return;
    }
    
    ioMem.setReg16(0x0130, keyConf);
  }

  public void keyReleased(KeyEvent e)
  {
    //System.out.println("keyReleased(KeyEvent e)");

    int keyCode = e.getKeyCode();
    
    switch (keyCode)
    {
      case KeyEvent.VK_A:     keyConf |= 0x0001; break; // The A button
      case KeyEvent.VK_B:     keyConf |= 0x0002; break; // The B button
      case KeyEvent.VK_SHIFT: keyConf |= 0x0004; break; // The SELECT button
      case KeyEvent.VK_ENTER: keyConf |= 0x0008; break; // The START button
      case KeyEvent.VK_RIGHT: keyConf |= 0x0010; break; // The RIGHT paddle
      case KeyEvent.VK_LEFT:  keyConf |= 0x0020; break; // The LEFT paddle
      case KeyEvent.VK_UP:    keyConf |= 0x0040; break; // The UP paddle
      case KeyEvent.VK_DOWN:  keyConf |= 0x0080; break; // The DOWN paddle
      case KeyEvent.VK_R:     keyConf |= 0x0100; break; // The R button
      case KeyEvent.VK_L:     keyConf |= 0x0200; break; // The L button
      default: return;
    }
    
    ioMem.setReg16(0x0130, keyConf);
  }

  public void keyTyped(KeyEvent cookies)
  {
    // Miam !
    cookies.consume();
  }
}
