package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.nb.screen.ScreenTopComponent;
import com.lemoulinstudio.gfa.nb.screen.ScreenTopComponentFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

public class RomDataObject extends MultiDataObject {

  private class OpenSupport implements OpenCookie {

    private ScreenTopComponent screenTopComponent;

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

    public void open() {
      TopComponent tc = getTopComponent();
      tc.open();
      tc.requestActive();
    }

  }

  public RomDataObject(FileObject pf, RomDataLoader loader) throws DataObjectExistsException, IOException {
    super(pf, loader);

    CookieSet cookies = getCookieSet();
    cookies.add(new OpenSupport());
  }

  @Override
  protected Node createNodeDelegate() {
    return new RomDataNode(this, getLookup());
  }

  @Override
  public Lookup getLookup() {
    return getCookieSet().getLookup();
  }

  private GfaDevice gfaDevice;

  public synchronized GfaDevice getGfaDevice() {
    if (gfaDevice == null) {
      // Create the devide.
      gfaDevice = new GfaDevice();

      // Load the bios.
      gfaDevice.getMemory().loadBios("roms/bios.gba");

      // Load the rom.
      try {gfaDevice.getMemory().loadRom(getPrimaryFile().getInputStream());}
      catch (FileNotFoundException e) {}
    }
    
    return gfaDevice;
  }

}
