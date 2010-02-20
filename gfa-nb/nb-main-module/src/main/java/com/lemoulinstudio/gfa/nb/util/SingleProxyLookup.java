package com.lemoulinstudio.gfa.nb.util;

import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 * A lookup that proxy at most one lookup.
 *
 * @author vincent
 */
// Deprecated: the base class ProxyLookup doesn't seem to update the results, might be buggy.
@Deprecated
public class SingleProxyLookup extends ProxyLookup {

  private Lookup delegateLookup;

  public SingleProxyLookup() {
  }

  public SingleProxyLookup(Lookup delegateLookup) {
    setDelegateLookup(delegateLookup);
  }

  /*
   * Returns the delegate lookup.
   * A returned <code>null</code> value means no delegate lookup.
   */
  public Lookup getDelegateLookup() {
    return delegateLookup;
  }

  /**
   * Sets the delegate lookup.
   * 
   * @param delegateLookup The deletage lookup.
   * Use the <code>null</code> value to mean no delegate lookup.
   */
  public void setDelegateLookup(Lookup delegateLookup) {
    if (this.delegateLookup != delegateLookup) {
      if (delegateLookup == null)  setLookups();
      else setLookups(delegateLookup);
      this.delegateLookup = delegateLookup;
    }
  }
  
}
