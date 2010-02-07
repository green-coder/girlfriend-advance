import gfa.ui.UserInterface;

public class Main
{
    public static void main(String[] args)
    {
	UserInterface ui;
	if (args.length >= 1) ui = new UserInterface(args[0]);
	else ui = new UserInterface();
	ui.show();
    }
    
    public static final String infoMsg =
	"GirlfriendAdvance Blonde Edition - by karma\n" +
	"Official website : http://gfa.emu-france.com\n" +
	"This software is an environment for the development on GBA.";
    
    public static final String usageMsg =
	"Usage : java GfaApplet [romFile]\n" +
	"  romFile : specify the file name of the rom pack.\n" +
	"            it should end with \".bin\", \".gba\", \".agb\" or \".zip\".\n" +
	"If none romFile is specified, gfa try to load the gfa demo.";
}
