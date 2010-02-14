package com.lemoulinstudio.gfa.nb.screen;

import com.lemoulinstudio.gfa.nb.filetype.rom.RomDataObject;

/**
 *
 * @author Vincent Cantin
 */
public class DefaultScreenTopComponentFactory implements ScreenTopComponentFactory {

  public ScreenTopComponent createScreenTopComponent(RomDataObject dataObject) {
    return new DefaultScreenTopComponent(dataObject);
  }
  
}
