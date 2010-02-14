package com.lemoulinstudio.gfa.nb.screen;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;

/**
 *
 * @author Vincent Cantin
 */
public interface ScreenTopComponentFactory {
  public ScreenTopComponent createScreenTopComponent(RomDataObject dataObject);
}
