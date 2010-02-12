package com.lemoulinstudio.gfa.nb.util;

/**
 *
 * @author Fabrizio Giudici
 */
public interface EventBusListener<T> {
  public void onEvent(T object);
}
