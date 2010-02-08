package gfa.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

public class ExtendedBorderLayout implements LayoutManager2 {

  int hgap;
  int vgap;

  Component north;
  Component west;
  Component east;
  Component south;
  Component center;

  Component firstLine;
  Component lastLine;
  Component firstItem;
  Component lastItem;

  public static final String NORTH = "North";
  public static final String SOUTH = "South";
  public static final String EAST = "East";
  public static final String WEST = "West";
  public static final String CENTER = "Center";
  public static final String BEFORE_FIRST_LINE = "First";
  public static final String AFTER_LAST_LINE = "Last";
  public static final String BEFORE_LINE_BEGINS = "Before";
  public static final String AFTER_LINE_ENDS = "After";

  public ExtendedBorderLayout() {
    this(0, 0);
  }

  public ExtendedBorderLayout(int hgap, int vgap) {
    this.hgap = hgap;
    this.vgap = vgap;
  }

  public int getHgap() {
    return hgap;
  }

  public void setHgap(int hgap) {
    this.hgap = hgap;
  }

  public int getVgap() {
    return vgap;
  }

  public void setVgap(int vgap) {
    this.vgap = vgap;
  }

  public void addLayoutComponent(Component comp, Object constraints) {
    synchronized (comp.getTreeLock()) {
      if ((constraints == null) || (constraints instanceof String))
        addLayoutComponent((String) constraints, comp);
      else
        throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
    }
  }

  public void addLayoutComponent(String name, Component comp) {
    synchronized (comp.getTreeLock()) {
      /* Special case:  treat null the same as "Center". */
      if (name == null) {
        name = "Center";
      }

      /* Assign the component to one of the known regions of the layout.
       */
      if ("Center".equals(name)) {
        center = comp;
      } else if ("North".equals(name)) {
        north = comp;
      } else if ("South".equals(name)) {
        south = comp;
      } else if ("East".equals(name)) {
        east = comp;
      } else if ("West".equals(name)) {
        west = comp;
      } else if (BEFORE_FIRST_LINE.equals(name)) {
        firstLine = comp;
      } else if (AFTER_LAST_LINE.equals(name)) {
        lastLine = comp;
      } else if (BEFORE_LINE_BEGINS.equals(name)) {
        firstItem = comp;
      } else if (AFTER_LINE_ENDS.equals(name)) {
        lastItem = comp;
      } else {
        throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
      }
    }
  }

  public void removeLayoutComponent(Component comp) {
    synchronized (comp.getTreeLock()) {
      if (comp == center) {
        center = null;
      } else if (comp == north) {
        north = null;
      } else if (comp == south) {
        south = null;
      } else if (comp == east) {
        east = null;
      } else if (comp == west) {
        west = null;
      }
      if (comp == firstLine) {
        firstLine = null;
      } else if (comp == lastLine) {
        lastLine = null;
      } else if (comp == firstItem) {
        firstItem = null;
      } else if (comp == lastItem) {
        lastItem = null;
      }
    }
  }

  public Dimension minimumLayoutSize(Container target) {
    synchronized (target.getTreeLock()) {
      Dimension dim = new Dimension(0, 0);

      boolean ltr = target.getComponentOrientation().isLeftToRight();
      Component c = null;

      if ((c = getChild(EAST, ltr)) != null) {
        Dimension d = c.getMinimumSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(WEST, ltr)) != null) {
        Dimension d = c.getMinimumSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(CENTER, ltr)) != null) {
        Dimension d = c.getMinimumSize();
        dim.width += d.width;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(NORTH, ltr)) != null) {
        Dimension d = c.getMinimumSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }
      if ((c = getChild(SOUTH, ltr)) != null) {
        Dimension d = c.getMinimumSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }

      Insets insets = target.getInsets();
      dim.width += insets.left + insets.right;
      dim.height += insets.top + insets.bottom;

      return dim;
    }
  }

  public Dimension preferredLayoutSize(Container target) {
    synchronized (target.getTreeLock()) {
      Dimension dim = new Dimension(0, 0);

      boolean ltr = target.getComponentOrientation().isLeftToRight();
      Component c = null;

      if ((c = getChild(EAST, ltr)) != null) {
        Dimension d = c.getPreferredSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(WEST, ltr)) != null) {
        Dimension d = c.getPreferredSize();
        dim.width += d.width + hgap;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(CENTER, ltr)) != null) {
        Dimension d = c.getPreferredSize();
        dim.width += d.width;
        dim.height = Math.max(d.height, dim.height);
      }
      if ((c = getChild(NORTH, ltr)) != null) {
        Dimension d = c.getPreferredSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }
      if ((c = getChild(SOUTH, ltr)) != null) {
        Dimension d = c.getPreferredSize();
        dim.width = Math.max(d.width, dim.width);
        dim.height += d.height + vgap;
      }

      Insets insets = target.getInsets();
      dim.width += insets.left + insets.right;
      dim.height += insets.top + insets.bottom;

      return dim;
    }
  }

  public Dimension maximumLayoutSize(Container target) {
    return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
  }

  public float getLayoutAlignmentX(Container parent) {
    return 0.5f;
  }

  public float getLayoutAlignmentY(Container parent) {
    return 0.5f;
  }

  public void invalidateLayout(Container target) {
  }

  public void layoutContainer(Container target) {
    synchronized (target.getTreeLock()) {
      Insets insets = target.getInsets();
      //System.out.println("DebugLayoutContainer() :");
      //System.out.println("  target.getWidth() = " + target.getWidth());
      //System.out.println("  target.getHeight() = " + target.getHeight());
      int top = insets.top;
      int bottom = target.getHeight() - insets.bottom;
      int left = insets.left;
      int right = target.getWidth() - insets.right;

      boolean ltr = target.getComponentOrientation().isLeftToRight();
      Component northComp = getChild(NORTH, ltr);
      Component southComp = getChild(SOUTH, ltr);
      Component westComp = getChild(WEST, ltr);
      Component centerComp = getChild(CENTER, ltr);
      Component eastComp = getChild(EAST, ltr);

      // Resolve constraints.

      int[] minWidthArray = {
        ((westComp != null) ? westComp.getMinimumSize().width : 0),
        ((centerComp != null) ? centerComp.getMinimumSize().width : 0),
        ((eastComp != null) ? eastComp.getMinimumSize().width : 0)
      };

      int[] prefWidthArray = {
        ((westComp != null) ? westComp.getPreferredSize().width : 0),
        ((centerComp != null) ? centerComp.getPreferredSize().width : 0),
        ((eastComp != null) ? eastComp.getPreferredSize().width : 0)
      };

      boolean[] hCompArray = {
        (westComp != null),
        (centerComp != null),
        (eastComp != null)
      };

      int targetWidth = right - left;

      int[] widthArray = resolve(minWidthArray, prefWidthArray, hCompArray, targetWidth);

      int[] minHeightArray = {
        ((northComp != null) ? northComp.getMinimumSize().height : 0),
        Math.max(((westComp != null) ? westComp.getMinimumSize().height : 0),
        Math.max(((centerComp != null) ? centerComp.getMinimumSize().height : 0),
        ((eastComp != null) ? eastComp.getMinimumSize().height : 0))),
        ((southComp != null) ? southComp.getMinimumSize().height : 0)
      };

      int[] prefHeightArray = {
        ((northComp != null) ? northComp.getPreferredSize().height : 0),
        Math.max(((westComp != null) ? westComp.getPreferredSize().height : 0),
        Math.max(((centerComp != null) ? centerComp.getPreferredSize().height : 0),
        ((eastComp != null) ? eastComp.getPreferredSize().height : 0))),
        ((southComp != null) ? southComp.getPreferredSize().height : 0)
      };

      boolean[] vCompArray = {
        (northComp != null),
        (westComp != null) || (centerComp != null) || (eastComp != null),
        (southComp != null)
      };

      int targetHeight = bottom - top;

      int[] heightArray = resolve(minHeightArray, prefHeightArray, vCompArray, targetHeight);

      // Apply results to the componants.

      if (northComp != null) {
        northComp.setBounds(left, top, targetWidth, heightArray[0]);
      }

      top += heightArray[0];

      if (westComp != null) {
        westComp.setBounds(left, top, widthArray[0], heightArray[1]);
      }

      if (centerComp != null) {
        centerComp.setBounds(left + widthArray[0], top, widthArray[1], heightArray[1]);
      }

      if (eastComp != null) {
        eastComp.setBounds(right - widthArray[2], top, widthArray[2], heightArray[1]);
      }

      top += heightArray[1];

      if (southComp != null) {
        southComp.setBounds(left, top, targetWidth, heightArray[2]);
      }
    }
  }

  protected int[] resolve(int[] min, int[] pref, boolean[] isComp, int total) {
    int nbElm = min.length;
    int[] result = new int[nbElm];

    //for (int i = 0; i < nbElm; i++) System.out.println("min[i] = " + min[i]);
    //for (int i = 0; i < nbElm; i++) System.out.println("pref[i] = " + pref[i]);
    //System.out.println("total = " + total);

    if (total <= 0) {
      return result;
    }

    int nbComp = 0;
    for (int i = 0; i < nbElm; i++) {
      if (isComp[i]) {
        nbComp++;
      }
    }

    if (nbComp == 0) {
      return result;
    }

    int minSum = 0;
    int prefSum = 0;
    for (int i = 0; i < nbElm; i++) {
      if (pref[i] < min[i]) {
        min[i] = pref[i];
      }
      minSum += min[i];
      prefSum += pref[i];
    }

    if (total < minSum) {
      //System.out.println("cas 1");
      double ratio = ((double) total) / ((double) minSum);
      for (int i = 0; i < nbElm; i++) {
        result[i] = (int) (min[i] * ratio);
      }
    } else if (total < prefSum) {
      //System.out.println("cas 2");
      double ratio = ((double) (total - minSum)) / ((double) (prefSum - minSum));
      for (int i = 0; i < nbElm; i++) {
        result[i] = min[i] + (int) ((pref[i] - min[i]) * ratio);
      }
    } else // (total >= prefSum)
    {
      //System.out.println("cas 3");
      int part = (total - prefSum) / nbComp;
      for (int i = 0; i < nbElm; i++) {
        if (isComp[i]) {
          result[i] = pref[i] + part;
        }
      }
    }

    //for (int i = 0; i < nbElm; i++)
    //System.out.println("result[i] = " + result[i]);

    return result;
  }

  protected Component getChild(String key, boolean ltr) {
    Component result = null;

    if (key == NORTH) {
      result = (firstLine != null) ? firstLine : north;
    } else if (key == SOUTH) {
      result = (lastLine != null) ? lastLine : south;
    } else if (key == WEST) {
      result = ltr ? firstItem : lastItem;
      if (result == null) {
        result = west;
      }
    } else if (key == EAST) {
      result = ltr ? lastItem : firstItem;
      if (result == null) {
        result = east;
      }
    } else if (key == CENTER) {
      result = center;
    }

    if (result != null && !result.isVisible()) {
      result = null;
    }

    return result;
  }

  public String toString() {
    return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
  }
  
}
