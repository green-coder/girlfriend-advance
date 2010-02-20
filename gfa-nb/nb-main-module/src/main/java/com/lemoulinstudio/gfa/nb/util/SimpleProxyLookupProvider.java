package com.lemoulinstudio.gfa.nb.util;

import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author vincent
 */
// Deprecated: Lookups.proxy(...) doesn't work well with results and listeners
// when the delegate lookup is changing. It is buggy.
@Deprecated
public class SimpleProxyLookupProvider implements Lookup.Provider {

  private class DelegateLookupProvider implements Lookup.Provider {
    public Lookup getLookup() {
      return delegateLookup;
    }
  }

  private Lookup delegateLookup;
  private final Lookup proxyLookup;

  public SimpleProxyLookupProvider() {
    delegateLookup = Lookup.EMPTY;
    proxyLookup = Lookups.proxy(new DelegateLookupProvider());
  }

  public Lookup getLookup() {
    // Make the proxyLookup check its delegate.
    proxyLookup.lookup((Class) null);

    return proxyLookup;
  }

  public void setDelegateLookup(Lookup delegateLookup) {
    this.delegateLookup = delegateLookup;

    // Make the proxyLookup check its delegate.
    proxyLookup.lookup((Class) null);
  }

  public Lookup getDelegateLookup() {
    return delegateLookup;
  }

}
