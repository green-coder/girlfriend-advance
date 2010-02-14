package com.lemoulinstudio.gfa.nb.screen;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataNode;
import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.util.ImageUtilities;
import org.openide.windows.TopComponent;

/**
 *
 * @author Vincent Cantin
 */
public abstract class ScreenTopComponent extends TopComponent {

  protected final RomDataObject dataObject;

  public ScreenTopComponent(final RomDataObject dataObject) {
    this.dataObject = dataObject;

    dataObject.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        setName(dataObject.getName());
      }
    });

    setName(dataObject.getName());
    setIcon(ImageUtilities.loadImage(RomDataNode.IMAGE_ICON_BASE, true));
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
