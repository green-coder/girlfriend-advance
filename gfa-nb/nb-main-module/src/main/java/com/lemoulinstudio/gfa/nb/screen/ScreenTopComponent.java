package com.lemoulinstudio.gfa.nb.screen;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataNode;
import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 *
 * @author Vincent Cantin
 */
public abstract class ScreenTopComponent extends TopComponent {

  private RomDataObject dataObject;

  public ScreenTopComponent() {
    setName(NbBundle.getMessage(DefaultScreenTopComponent.class, "CTL_ScreenTopComponent"));
    setIcon(ImageUtilities.loadImage(RomDataNode.IMAGE_ICON_BASE, true));
//    setToolTipText(NbBundle.getMessage(DefaultScreenTopComponent.class, "HINT_DefaultScreenTopComponent"));
  }

  public RomDataObject getDataObject() {
    return dataObject;
  }

  // This should only be called once, and by the RomDataObject class.
  public void setDataObject(final RomDataObject dataObject) {
    this.dataObject = dataObject;

    dataObject.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        setName(dataObject.getName());
      }
    });

    setName(dataObject.getName());
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
