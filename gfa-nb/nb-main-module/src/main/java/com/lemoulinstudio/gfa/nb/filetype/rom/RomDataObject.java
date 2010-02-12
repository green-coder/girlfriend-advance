package com.lemoulinstudio.gfa.nb.filetype.rom;

import com.lemoulinstudio.gfa.core.GfaDevice;
import com.lemoulinstudio.gfa.nb.screen.ScreenTopComponent;
import java.io.IOException;
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

    private TopComponent topComponent;

    private TopComponent getTopComponent() {
      // Todo: Document this hook.
      if (topComponent == null) {
        ScreenTopComponent screenTopComponent = Lookups.forPath("Gfa/TopComponent").lookupAll(ScreenTopComponent.class).iterator().next();
        screenTopComponent.setDataObject(RomDataObject.this);
      }

      return topComponent;
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

}
