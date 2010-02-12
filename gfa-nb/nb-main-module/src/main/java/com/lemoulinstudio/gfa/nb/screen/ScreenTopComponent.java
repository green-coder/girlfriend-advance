package com.lemoulinstudio.gfa.nb.screen;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;
import org.openide.windows.TopComponent;

/**
 *
 * @author Vincent Cantin
 */
public abstract class ScreenTopComponent extends TopComponent {

  private RomDataObject dataObject;

  public RomDataObject getDataObject() {
    return dataObject;
  }

  public void setDataObject(RomDataObject dataObject) {
    this.dataObject = dataObject;
  }

  @Override
  public int getPersistenceType() {
    return TopComponent.PERSISTENCE_NEVER;
  }

  private static final String PREFERRED_ID = "ScreenTopComponent";

  @Override
  protected String preferredID() {
    return PREFERRED_ID;
  }

}
