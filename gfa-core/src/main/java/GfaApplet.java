import java.applet.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;

import gfa.*;
import gfa.debug.*;
import gfa.gfx.*;

public class GfaApplet
  extends Applet
  implements Runnable, LcdPainter
{

  protected GirlfriendAdvance gfa;
  protected Thread runner;
  protected String romName;

  public synchronized void init()
  {
    gfa = new GirlfriendAdvance();
    gfa.getLcd().setLcdPainter(this);
    disableEvents(-1L);
    enableEvents(AWTEvent.KEY_EVENT_MASK);
    addKeyListener(gfa);
    romName = getParameter("romName");
    gfa.getMem().loadRom(getDocumentBase(), romName);
  }

  public void start()
  {
    if (runner == null)
    {
      runner = new Thread(this);
      runner.start();
    }
  }

  public void run()
  {
    gfa.getCpu().run();
  }

  public void stop()
  {
    if ((runner != null) && runner.isAlive())
    {
      do
      {
	gfa.getCpu().stopPlease();
	try{runner.join(1000);}
	catch (InterruptedException e) {}
      } while (runner.isAlive());
    }
    runner = null;
  }

  public void paintLcd(Image lcdImage)
  {
    Graphics g = getGraphics();
    if (g != null)
      g.drawImage(lcdImage, 0, 0, null);
  }

  static public void main(String[] args)
  {
    GirlfriendAdvance gfa = new GirlfriendAdvance();
    String biosName = "";
    String romName = gfaDemoRomName; // gfx by Sonik (www.dream-emulation.fr.st)
    
    try // take parameters into account since there are some japanese webmaster who test this emu with many unknown roms ;-)
    {
      for (int i = 0; i < args.length; i++)
      {
	if (args[i].equals("-help"))
	{
	  System.out.println(infoMsg);
	  System.out.println(moreInfoMsg);
	  System.out.println(moreAndMoreInfoMsg);
	  System.out.println(usageMsg);
	  return;
	}
	else if (args[i].equals("-bios"))
	{
	  i++;
	  biosName = args[i];
	}
	else if (args[i].equals("-rom"))
	{
	  i++;
	  romName = args[i];
	}
	
      }
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      System.out.println("Bad parameter format.");
      System.out.println("Type \"java GirlfriendAdvance -help\" to have more information.");
      return;
    }
    
    URL contextUrl;
    try {contextUrl = new URL("file:");}
    catch (MalformedURLException e) {contextUrl = null;}
    
    if (!biosName.equals("")) gfa.getMem().loadBios(contextUrl, biosName);
    if (!romName.equals("")) gfa.getMem().loadRom(contextUrl, romName);
    
    GfaScreenFrame frame        = new GfaScreenFrame(gfa.getLcd());
    RegisterViewer regViewer    = new RegisterViewer(gfa.getCpu());
    CodeViewer     codeViewer   = new CodeViewer(gfa.getCpu(), gfa.getMem());
    StepFrame      stepFrame    = new StepFrame(gfa.getCpu(), gfa.getMem());
    InputFrame     inputFrame   = new InputFrame(gfa.getMem());
    stepFrame.addCpuStateChangeListener(regViewer);
    stepFrame.addCpuStateChangeListener(codeViewer);
  }

  public static final String gfaDemoRomName = "gfa-splash.zip";
  public static final String infoMsg = "GirlfriendAdvance Blonde Edition";
  public static final String moreInfoMsg = "Official website : http://gfa.emu-france.com";
  public static final String moreAndMoreInfoMsg = "Development site : http://www.sourceforge.net/projects/girlfriendadv";
  public static final String usageMsg = "Usage : java GfaApplet [-bios biosFile] [-rom romFile]\n" +
      "If none biosFile is specified, gfa won't emulate it.\n" +
      "If none romFile is specified, gfa load the gfa demo.\n";
}
