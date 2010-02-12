package com.lemoulinstudio.gfa.core.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JComponent;

public class GfaScreen extends JComponent {

  private Dimension fixedDimension = new Dimension(240, 160);
  protected Image lcdImage;

  public GfaScreen() {
    lcdImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("gfa-splash.png"));
  }

  public GfaScreen(Image lcdImage) {
    setDoubleBuffered(false);
    this.lcdImage = lcdImage;
  }

  @Override
  public Dimension getMaximumSize() {
    return fixedDimension;
  }

  @Override
  public Dimension getMinimumSize() {
    return fixedDimension;
  }

  @Override
  public Dimension getPreferredSize() {
    return fixedDimension;
  }

  @Override
  public boolean isPreferredSizeSet() {
    return true;
  }

  @Override
  public boolean isMinimumSizeSet() {
    return true;
  }

  @Override
  public boolean isMaximumSizeSet() {
    return true;
  }

  public void resfresh() {
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(lcdImage, 0, 0, this);
  }
  
}
