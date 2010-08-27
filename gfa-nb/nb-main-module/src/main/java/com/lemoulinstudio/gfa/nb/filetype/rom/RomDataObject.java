package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.analysis.ParseException;
import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.core.cpu.CpuStopListener;
import com.lemoulinstudio.gfa.nb.screen.ScreenTopComponent;
import com.lemoulinstudio.gfa.nb.screen.ScreenTopComponentFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

public class RomDataObject extends MultiDataObject {

  private class OpenSupport implements OpenCookie {
    public void open() {
      TopComponent tc = getTopComponent();
      tc.open();
      tc.requestActive();
    }
  }

  public class Resetable implements Node.Cookie {
    public void reset() {
      if (getGfaDeviceState() == GfaDeviceState.Running)
        stoppable.stop();

      setGfaDeviceState(GfaDeviceState.Undefined);
      getGfaDevice().reset();
      setGfaDeviceState(GfaDeviceState.Stopped);
    }
  }

  public class Runnable implements Node.Cookie {
    public void run() {
      cpuThread = new Thread(new java.lang.Runnable() {
        @Override
        public void run() {
          getGfaDevice().getCpu().run();
        }
      });
      setGfaDeviceState(GfaDeviceState.Running);
      cpuThread.start();
    }
  }

  public class Steppable implements Node.Cookie {
    public void step() {
      setGfaDeviceState(GfaDeviceState.Undefined);
      getGfaDevice().getCpu().step();
      setGfaDeviceState(GfaDeviceState.Stopped);
    }
  }

  public class StepBackable implements Node.Cookie {
    public void stepBack() {
      try {
        // Set a breakpoint.
        getGfaDevice().getCpu().addCpuStepListener(breakpoint);

        // Set its condition.
        breakpoint.setCondition(String.format("(> (time) %d)",
                getGfaDevice().getTime().getTime() - 8));

        // Reset
        setGfaDeviceState(GfaDeviceState.Undefined);
        getGfaDevice().reset();

        // Run
        cpuThread = new Thread(new java.lang.Runnable() {
          @Override
          public void run() {
            getGfaDevice().getCpu().run();
          }
        });
        setGfaDeviceState(GfaDeviceState.Running);
        cpuThread.start();
      }
      catch (ParseException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }

  public class Debuggable implements Node.Cookie {
    public void debug() {
      // Set a breakpoint.
      getGfaDevice().getCpu().addCpuStepListener(breakpoint);

      // Run
      cpuThread = new Thread(new java.lang.Runnable() {
        @Override
        public void run() {
          getGfaDevice().getCpu().run();
        }
      });
      setGfaDeviceState(GfaDeviceState.Running);
      cpuThread.start();
    }
  }

  public class DebugBackable implements Node.Cookie {
    public void debugBack() {
    }
  }

  public class Stoppable implements Node.Cookie {
    public void stop() {
      getGfaDevice().getCpu().requestStop();
    }
  }

  private class MyCpuStoppedListener implements CpuStopListener {
    public void notifyCpuStopped() {
      cpuThread = null;
      
      // Make sure that we remove the breakpoint.
      GfaDevice device = getGfaDevice();
      device.getCpu().removeCpuStepListener(breakpoint);
      device.getMemory().clearListeners();

      setGfaDeviceState(GfaDeviceState.Stopped);
    }
  }

  public class StoppedState implements Node.Cookie {
    public RomDataObject getRomDataObject() {
      return RomDataObject.this;
    }
  }
  
  public enum GfaDeviceState {
    Undefined,
    Stopped,
    Running
  }

  private Resetable resetable = new Resetable();
  private DebugBackable debugBackable = new DebugBackable();
  private StepBackable stepBackable = new StepBackable();
  private Stoppable stoppable = new Stoppable();
  private Steppable steppable = new Steppable();
  private Debuggable debuggable = new Debuggable();
  private Runnable runnable = new Runnable();
  private StoppedState stoppedState = new StoppedState();

  private GfaDeviceState gfaDeviceState = GfaDeviceState.Undefined;
  private Thread cpuThread = null;
  private Map<GfaDeviceState, List<Node.Cookie>> stateToCookies =
          new HashMap<GfaDeviceState, List<Node.Cookie>>();

  private ScreenTopComponent screenTopComponent;

  public RomDataObject(FileObject pf, RomDataLoader loader) throws DataObjectExistsException, IOException {
    super(pf, loader);

    CookieSet cookies = getCookieSet();
    cookies.add(new OpenSupport());

    stateToCookies.put(GfaDeviceState.Undefined, Collections.<Node.Cookie>emptyList());
    stateToCookies.put(GfaDeviceState.Stopped, Arrays.<Node.Cookie>asList(stoppedState, resetable, debugBackable, stepBackable, steppable, debuggable, runnable));
    stateToCookies.put(GfaDeviceState.Running, Arrays.<Node.Cookie>asList(resetable, stoppable));
  }

  @Override
  protected Node createNodeDelegate() {
    return new RomDataNode(this, getLookup());
  }

  @Override
  public Lookup getLookup() {
    return getCookieSet().getLookup();
  }

  private TopComponent getTopComponent() {
    if (screenTopComponent == null) {
      // Todo: Document this hook.
      ScreenTopComponentFactory factory =
              Lookups.forPath("Gfa/ScreenTopComponentFactory")
              .lookup(ScreenTopComponentFactory.class);
      screenTopComponent = factory.createScreenTopComponent(RomDataObject.this);
    }

    return screenTopComponent;
  }

  public GfaDeviceState getGfaDeviceState() {
    return gfaDeviceState;
  }

  public synchronized void setGfaDeviceState(GfaDeviceState gfaDeviceState) {
    List<Node.Cookie> cookiesBefore = stateToCookies.get(this.gfaDeviceState);
    List<Node.Cookie> cookiesAfter = stateToCookies.get(gfaDeviceState);

    CookieSet cookieSet = getCookieSet();

    for (Node.Cookie cookie : cookiesBefore)
      if (!cookiesAfter.contains(cookie))
        cookieSet.remove(cookie);
    
    this.gfaDeviceState = gfaDeviceState;

    for (Node.Cookie cookie : cookiesAfter)
      if (!cookiesBefore.contains(cookie))
        cookieSet.add(cookie);
  }

  private GfaDevice gfaDevice;
  private CpuStopListener cpuStopListener;
  private Breakpoint breakpoint;

  private synchronized void ensureDeviceCreated() {
    if (gfaDevice == null) {
      // Create the device.
      gfaDevice = new GfaDevice();

      // Listen when it stops.
      cpuStopListener = new MyCpuStoppedListener();
      gfaDevice.getCpu().addCpuStopListener(cpuStopListener);

      // Create a breakpoint.
      breakpoint = new Breakpoint(gfaDevice);

      // Set its initial value.
      try {breakpoint.setCondition("(> (time) 8062230)");}
      catch (ParseException ex) {Exceptions.printStackTrace(ex);}

      // Create a cpu logguer.
      gfaDevice.getCpu().addCpuStepListener(new CpuLogger(gfaDevice));

      // Load the bios.
      gfaDevice.getMemory().loadBios("roms/bios.gba");

      // Load the rom.
      try {gfaDevice.getMemory().loadRom(getPrimaryFile().getInputStream());}
      catch (FileNotFoundException e) {}
      catch (OutOfMemoryError oome) {} // tmp fix

      // Set the state of the device.
      setGfaDeviceState(GfaDeviceState.Stopped);
    }
  }

  public GfaDevice getGfaDevice() {
    ensureDeviceCreated();
    return gfaDevice;
  }

  public Breakpoint getBreakpoint() {
    ensureDeviceCreated();
    return breakpoint;
  }

  public synchronized void releaseResources() {
    // Stop the device if needed.
    if (getGfaDeviceState() == GfaDeviceState.Running)
      stoppable.stop();

    // Set state to undefined, so that no unwanted cookies are left in the lookup.
    setGfaDeviceState(GfaDeviceState.Undefined);

    // Release the reference to the device.
    gfaDevice = null;
    cpuStopListener = null;
    breakpoint = null;

    // Release the reference to the top component.
    screenTopComponent = null;
  }

}
