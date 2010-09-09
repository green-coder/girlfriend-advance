package com.lemoulinstudio.gfa.core;

import com.lemoulinstudio.gfa.core.input.InputSystem;
import com.lemoulinstudio.gfa.core.cpu.Arm7Tdmi;
import com.lemoulinstudio.gfa.core.cpu.Arm7TdmiGen2;
import com.lemoulinstudio.gfa.core.dma.Dma;
import com.lemoulinstudio.gfa.core.dma.Dma0;
import com.lemoulinstudio.gfa.core.dma.Dma1;
import com.lemoulinstudio.gfa.core.dma.Dma2;
import com.lemoulinstudio.gfa.core.dma.Dma3;
import com.lemoulinstudio.gfa.core.gfx.Lcd;
import com.lemoulinstudio.gfa.core.gfx.LcdGen1;
import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.time.Time;

public class GfaDevice {

  private Arm7Tdmi cpu;
  private GfaMMU mem;
  private Dma dma0;
  private Dma dma1;
  private Dma dma2;
  private Dma dma3;
  private Time time;
  private Lcd lcd;
  private InputSystem inputSystem;

  public GfaDevice() {
    this(new Arm7TdmiGen2());
  }

  public GfaDevice(Arm7Tdmi cpu) {
    this.cpu  = cpu;
    mem  = new GfaMMU();
    dma0 = new Dma0();
    dma1 = new Dma1();
    dma2 = new Dma2();
    dma3 = new Dma3();
    time = new Time();
    lcd  = new LcdGen1();
    inputSystem = new InputSystem();
    
    setupConnections();
  }

  private void setupConnections() {
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
    time.connectToDma0(dma0);
    time.connectToDma1(dma1);
    time.connectToDma2(dma2);
    time.connectToDma3(dma3);
    lcd.connectToMemory(mem);
    inputSystem.connectToMemory(mem);

  }

  public void reset(boolean skipBios) {
    getCpu().reset(skipBios);
    getMemory().reset();
    getDma0().reset();
    getDma1().reset();
    getDma2().reset();
    getDma3().reset();
    getTime().reset();
    getLcd().reset();
  }

  public Arm7Tdmi    getCpu()         {return cpu;}
  public GfaMMU      getMemory()      {return mem;}
  public Dma         getDma0()        {return dma0;}
  public Dma         getDma1()        {return dma1;}
  public Dma         getDma2()        {return dma2;}
  public Dma         getDma3()        {return dma3;}
  public Time        getTime()        {return time;}
  public Lcd         getLcd()         {return lcd;}
  public InputSystem getInputSystem() {return inputSystem;}
  
}
