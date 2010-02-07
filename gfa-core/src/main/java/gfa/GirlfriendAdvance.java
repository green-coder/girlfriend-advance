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
    cpu  = new Arm7Tdmi();
    mem  = new GfaMMU();
    dma0 = new Dma0();
    dma1 = new Dma1();
    dma2 = new Dma2();
    dma3 = new Dma3();
    time = new Time();
    lcd  = new Lcd();
    
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
    time.connectToMemory(mem);
    time.connectToLcd(lcd);
    lcd.connectToMemory(mem);
    
    cpu.reset();
    
    // Needed for handle the input of gfa from the keyboard.
    ioMem = (IORegisterSpace_8_16_32) mem.getMemoryBank(0x04);
  }

  public Arm7Tdmi getCpu() {return cpu;}
  public GfaMMU   getMem() {return mem;}
  public Lcd      getLcd() {return lcd;}


  protected IORegisterSpace_8_16_32 ioMem;
  protected short keyConf = 0x03ff;

  public void keyPressed(KeyEvent e)
  {
      System.out.println("keyPressed(KeyEvent e)");

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
    System.out.println("keyReleased(KeyEvent e)");

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
