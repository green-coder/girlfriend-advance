package com.lemoulinstudio.gfa.nb.filetype.rom;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;

public class RomDataObject extends MultiDataObject {

  public RomDataObject(FileObject pf, RomDataLoader loader) throws DataObjectExistsException, IOException {
    super(pf, loader);

  }

  @Override
  protected Node createNodeDelegate() {
    return new RomDataNode(this);
  }
}
