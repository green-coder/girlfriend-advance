package com.lemoulinstudio.gfa.jse.ui;

import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import javax.swing.Action;
import javax.swing.JComponent;

abstract class AbstractActionPropertyChangeListener implements PropertyChangeListener {

  private static ReferenceQueue queue;
  private WeakReference target;
  private Action action;

  AbstractActionPropertyChangeListener(JComponent c, Action a) {
    super();
    setTarget(c);
    this.action = a;
  }

  public void setTarget(JComponent c) {
    if (queue == null) {
      queue = new ReferenceQueue();
    }
    // Check to see whether any old buttons have
    // been enqueued for GC.  If so, look up their
    // PCL instance and remove it from its Action.
    OwnedWeakReference r;
    while ((r = (OwnedWeakReference) queue.poll()) != null) {
      AbstractActionPropertyChangeListener oldPCL =
              (AbstractActionPropertyChangeListener) r.getOwner();
      Action oldAction = oldPCL.getAction();
      if (oldAction != null) {
        oldAction.removePropertyChangeListener(oldPCL);
      }
    }
    this.target = new OwnedWeakReference(c, queue, this);
  }

  public JComponent getTarget() {
    return (JComponent) this.target.get();
  }

  public Action getAction() {
    return action;
  }

  private static class OwnedWeakReference extends WeakReference {

    private Object owner;

    OwnedWeakReference(Object target, ReferenceQueue queue, Object owner) {
      super(target, queue);
      this.owner = owner;
    }

    public Object getOwner() {
      return owner;
    }
  }

}
