package com.lemoulinstudio.gfa.nb.util;

import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Naive implementation of a proxy look up. At least, this one works fine
 * in all the situations that I tested it for.
 *
 * @author Vincent Cantin
 */
public class BarbarianProxyLookup extends AbstractLookup {

  private class BarbarianLookupListener implements LookupListener {
    public void resultChanged(LookupEvent ev) {
      content.set(delegateLookupResult.allInstances(), null);
    }
  }

  private final InstanceContent content;
  private final LookupListener delegateLookupListener;

  private Lookup delegateLookup;
  private Lookup.Result<Object> delegateLookupResult;

  public BarbarianProxyLookup() {
    this(new InstanceContent());
  }

  private BarbarianProxyLookup(InstanceContent content) {
    super(content);
    this.content = content;
    delegateLookupListener = new BarbarianLookupListener();
    
    delegateLookup = Lookup.EMPTY;
    delegateLookupResult = delegateLookup.lookupResult(Object.class);
    delegateLookupResult.addLookupListener(delegateLookupListener);
  }

  public Lookup getDelegateLookup() {
    return delegateLookup;
  }

  public void setDelegateLookup(Lookup delegateLookup) {
    delegateLookupResult.removeLookupListener(delegateLookupListener);
    delegateLookupResult = null;
    
    this.delegateLookup = delegateLookup;
    
    delegateLookupResult = delegateLookup.lookupResult(Object.class);
    content.set(delegateLookupResult.allInstances(), null);
    delegateLookupResult.addLookupListener(delegateLookupListener);
  }

}
