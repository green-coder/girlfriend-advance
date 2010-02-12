package com.lemoulinstudio.gfa.nb.util;

import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Fabrizio Giudici
 */
public class EventBus {

  private static class ListenerAdapter<T> implements LookupListener {

    private final EventBusListener eventBusListener;

    public ListenerAdapter(final EventBusListener eventBusListener) {
      this.eventBusListener = eventBusListener;
    }

    public void resultChanged(final LookupEvent event) {
      final Lookup.Result result = (Lookup.Result) event.getSource();

      if (!result.allInstances().isEmpty())
        eventBusListener.onEvent((T) result.allInstances().iterator().next());
      else
        eventBusListener.onEvent(null);
    }

  }

  private static final EventBus instance = new EventBus();
  
  public static EventBus getDefault() {
    return instance;
  }

  private final CentralLookup centralLookup = CentralLookup.getDefault();
  private final Map<Class<?>, Result<?>> resultMapByClass = new HashMap<Class<?>, Result<?>>();
  private final Map<EventBusListener<?>, ListenerAdapter<?>> adapterMapByListener = new HashMap<EventBusListener<?>, ListenerAdapter<?>>();

  private EventBus() {
  }

  public void publish(final Object object) {
    if (object == null)
      throw new IllegalArgumentException("object is mandatory");

    for (final Object old : centralLookup.lookupAll(object.getClass()))
      centralLookup.remove(old);

    if (object != null)
      centralLookup.add(object);
  }

  public void unpublish(final Class<?> topic) {
    for (final Object old : centralLookup.lookupAll(topic))
      centralLookup.remove(old);
  }

  public synchronized <T> void subscribe(final Class<T> topic, final EventBusListener<T> listener) {
    Result<?> result = resultMapByClass.get(topic);

    if (result == null) {
      result = centralLookup.lookupResult(topic);
      resultMapByClass.put(topic, result);
      result.allInstances();
    }

    final ListenerAdapter<T> adapter = new ListenerAdapter<T>(listener);
    adapterMapByListener.put(listener, adapter);
    result.addLookupListener(adapter);
  }

  public synchronized <T> void unsubscribe(final Class<T> topic, final EventBusListener<T> listener) {
    final Result<?> result = resultMapByClass.get(topic);

    if (result == null)
      throw new IllegalArgumentException(String.format("Never subscribed to %s", topic));

    final ListenerAdapter<T> adapter = (ListenerAdapter<T>) adapterMapByListener.remove(listener);
    result.removeLookupListener(adapter);
  }
  
}
