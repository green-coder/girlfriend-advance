package com.lemoulinstudio.gfa.nb;

import com.lemoulinstudio.gfa.nb.util.SmartProxyLookup;
import org.openide.util.Lookup;

/**
 *
 * @author Vincent Cantin
 */
public class GfaContext {

  private static SmartProxyLookup lookup = new SmartProxyLookup();

  public static Lookup getLookup() {
    return lookup;
  }

  public static Lookup getDelegateLookup() {
    return lookup.getDelegateLookup();
  }

  public static void setDelegateLookup(Lookup lookupDelegate) {
    lookup.setDelegateLookup(lookupDelegate);
  }
  
}
