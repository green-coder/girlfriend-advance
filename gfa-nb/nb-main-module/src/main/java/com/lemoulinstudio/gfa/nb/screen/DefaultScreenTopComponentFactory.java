package com.lemoulinstudio.gfa.nb.screen;

/**
 *
 * @author Vincent Cantin
 */
public class DefaultScreenTopComponentFactory implements ScreenTopComponentFactory {

  public ScreenTopComponent createScreenTopComponent() {
    return new DefaultScreenTopComponent();
  }
  
}
