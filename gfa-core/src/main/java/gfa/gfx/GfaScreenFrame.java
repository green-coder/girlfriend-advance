package gfa.gfx;

import java.awt.*;
import java.awt.event.*;

public class GfaScreenFrame
  extends Frame
  implements LcdPainter
{
  public GfaScreenFrame(Lcd lcd)
  {
    setTitle("Girlfriend Advance - Blond edition");
    setSize(280, 230);
    lcd.setLcdPainter(this);
    addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          System.exit(0);
        }
      });
    setLocation(200, 100);
    show();
  }

  public void paintLcd(Image lcdImage)
  {
    Graphics g = getGraphics();
    if (g != null)
      g.drawImage(lcdImage, 16, 24, null);
  }
}
