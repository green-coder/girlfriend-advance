package com.lemoulinstudio.gfa.core.gfx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import javax.swing.JComponent;

public class GfaScreen extends JComponent {

  private final static Dimension fixedDimension = new Dimension(240, 160);
  
  private final Image image;

  public GfaScreen(ImageProducer imageProducer) {
    setDoubleBuffered(false);
    image = Toolkit.getDefaultToolkit().createImage(imageProducer);
  }

  public Image getImage() {
    return image;
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
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, this);
  }
  
}
