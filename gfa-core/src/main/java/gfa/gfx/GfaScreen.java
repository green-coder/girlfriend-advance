package gfa.gfx;

import gfa.*;

import javax.swing.*;
import java.awt.*;

public class GfaScreen
    extends JComponent
{
    protected Image lcdImage;
    
    public GfaScreen(GirlfriendAdvance gfa)
    {
	super();
	setDoubleBuffered(false);
	setPreferredSize(new Dimension(240, 160));
	setMinimumSize(new Dimension(240, 160));
	setMaximumSize(new Dimension(240, 160));
	lcdImage = gfa.getLcd().getImage();
    }
    
    public void resfresh()
    {
	repaint();
    }
    
    public void paint(Graphics g)
    {
	g.drawImage(lcdImage, 0, 0, this);
    }
}
