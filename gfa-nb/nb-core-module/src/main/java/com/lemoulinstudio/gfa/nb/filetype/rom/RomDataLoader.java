package com.lemoulinstudio.gfa.nb.filetype.rom;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

public class RomDataLoader extends UniFileLoader {

  public static final String REQUIRED_MIME = "bin/x-gba";
  private static final long serialVersionUID = 1L;

  public RomDataLoader() {
    super("com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject");
  }

  @Override
  protected String defaultDisplayName() {
    return NbBundle.getMessage(RomDataLoader.class, "LBL_Rom_loader_name");
  }

  @Override
  protected void initialize() {
    super.initialize();
    getExtensions().addMimeType(REQUIRED_MIME);
  }

  protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
    return new RomDataObject(primaryFile, this);
  }

  @Override
  protected String actionsContext() {
    return "Loaders/" + REQUIRED_MIME + "/Actions";
  }
}
