package gfa.ui;

import gfa.*;
import gfa.ui.action.*;
import gfa.gfx.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface
    extends JFrame
{
    public GirlfriendAdvance gfa;
    
    public GfaMenuBar menuBar;
    public GfaToolBar toolBar;
    public CodeViewer codeViewer;
    public RegisterViewerPanel regViewer;
    public InputPanel inputPanel;
    public GfaScreen gfaScreen;
    
    public Action fileMenuAction;
    public Action executionMenuAction;
    public Action internationalMenuAction;
    public Action helpMenuAction;
    
    public Action loadRomAction;
    public Action exitAction;
    
    public ResetAction resetAction;
    public Action runAction;
    public Action stopAction;
    public Action stepAction;
    public Action undoAction;
    public Action nextAction;
    
    public Action frenchLanguageAction;
    public Action twChineseLanguageAction;
    public Action japaneseLanguageAction;
    public Action vietnameseLanguageAction;
    public Action thaiLanguageAction;
    public Action chineseLanguageAction;
    public Action englishLanguageAction;
    
    public Action screenShotAction;
    
    public Action documentationAction;
    public Action aboutAction;
    
    public Action viewMenuDisasmAction;
    public Action homeDisasmAction;
    public Action biosRomDisasmAction;
    public Action externalRamDisasmAction;
    public Action workRamDisasmAction;
    public Action ioRegDisasmAction;
    public Action paletteRamDisasmAction;
    public Action videoRamDisasmAction;
    public Action oamRamDisasmAction;
    public Action gamepakRomDisasmAction;
    public Action cartRamDisasmAction;
    
    public Action trackMenuDisasmAction;
    public Action noTrackingDisasmAction;
    public Action centerTrackingDisasmAction;
    public Action windowTrackingDisasmAction;
    
    public UserInterface()
    {
	this(gfaDemoRomName);
    }
    
    public UserInterface(String romName)
    {
	super("Girlfriend Advance - Blond edition - by karma " +
	      "(I look for a job in Taiwan or in Japan)");
	
	// Create an instance of the emulator.
	gfa = new GirlfriendAdvance();
	
	// Create the actions of the program.
	
	fileMenuAction          = new DoNothingAction(this, "FileMenuAction");
	executionMenuAction     = new DoNothingAction(this, "ExecutionMenuAction");
	internationalMenuAction = new DoNothingAction(this, "InternationalMenuAction");
	helpMenuAction          = new DoNothingAction(this, "HelpMenuAction");
	
	loadRomAction = new LoadRomAction (this, gfa);
	exitAction    = new ExitAction    (this, gfa);
	
	resetAction = new ResetAction (this, gfa);
	runAction   = new RunAction   (this, gfa);
	stopAction  = new StopAction  (this, gfa);
	stepAction  = new StepAction  (this, gfa);
	undoAction  = new UndoAction  (this, gfa);
	nextAction  = new NextAction  (this, gfa);
	
	frenchLanguageAction     = new LocaleChangeAction(this, "FrenchLanguageAction", Locale.FRENCH);
	twChineseLanguageAction  = new LocaleChangeAction(this, "TwChineseLanguageAction", Locale.TRADITIONAL_CHINESE);
	japaneseLanguageAction   = new LocaleChangeAction(this, "JapaneseLanguageAction", Locale.JAPANESE);
	vietnameseLanguageAction = new LocaleChangeAction(this, "VietnameseLanguageAction", new Locale("vi", "", ""));
	thaiLanguageAction       = new LocaleChangeAction(this, "ThaiLanguageAction", new Locale("th", "", ""));
	chineseLanguageAction    = new LocaleChangeAction(this, "ChineseLanguageAction", Locale.SIMPLIFIED_CHINESE);
	englishLanguageAction    = new LocaleChangeAction(this, "EnglishLanguageAction", Locale.ENGLISH);
	
	screenShotAction = new ScreenShotAction(this, gfa);
	
	documentationAction = new DocumentationAction(this);
	aboutAction = new AboutAction(this);
	
	// Create the componants of the user interface.
	menuBar    = new GfaMenuBar(this);
	toolBar    = new GfaToolBar(this);
	codeViewer = new CodeViewer(this, gfa);
	regViewer  = new RegisterViewerPanel(this, gfa);
	inputPanel = new InputPanel(gfa);
	gfaScreen  = new GfaScreen(gfa);
	
	// Set the language to the default language of the plateform running gfa.
	fireLocaleChanged(Locale.getDefault());
	
	// Add componants to the main frame.
	setJMenuBar(menuBar);
	// inputPanel gfaScreen
	// codeViewer regViewer
	
	JPanel panel3 = new JPanel(new ExtendedBorderLayout());
	panel3.add(inputPanel, "West");
	panel3.add(gfaScreen, "Center");
	
	JPanel panel2 = new JPanel(new ExtendedBorderLayout());
	panel2.add(codeViewer, "West");
	//panel2.add(varWatcher, "Center");
	panel2.add(regViewer, "East");
	
	JPanel panel1 = new JPanel(new ExtendedBorderLayout());
	panel1.add(panel3, "North");
	panel1.add(panel2, "South");
	
	Container contentPane = getContentPane();
	contentPane.setLayout(new ExtendedBorderLayout());
	contentPane.add(toolBar, "North");
	contentPane.add(panel1, "Center");
	
	// Handle the windowClose event.
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter()
	    {
		public void windowClosing(WindowEvent e)
		{
		    exitAction.actionPerformed(new ActionEvent(this, 0, ""));
		}
	    });
	
	// Load the bios in the memory.
	gfa.getMemory().loadBios("roms/gba.rom");
	
	// Load a rom into the memory.
	if (romName != null)
	    {
		gfa.getMemory().loadRom(romName);
		fireGfaStatusChanged(GfaStatusChangeListener.STATUS_EXECUTION_STOPPED);
		fireGfaStateChanged();
	    }
	
	// Sizes the frame to the screen size.
	setSize(getToolkit().getScreenSize());
    }
    
    protected Vector stateListener = new Vector();
    
    public void addGfaStateChangeListener(GfaStateChangeListener obj)
    {
	stateListener.add(obj);
    }
    
    public void fireGfaStateChanged()
    {
	for (int i = 0; i < stateListener.size(); i++)
	    ((GfaStateChangeListener) stateListener.get(i)).gfaStateChanged();
    }
    
    protected Vector statusListener = new Vector();
    
    public void addGfaStatusChangeListener(GfaStatusChangeListener obj)
    {
	statusListener.add(obj);
    }

    public void fireGfaStatusChanged(int status)
    {
	for (int i = 0; i < statusListener.size(); i++)
	    ((GfaStatusChangeListener) statusListener.get(i)).gfaStatusChanged(status);
    }

    protected Vector localeListener = new Vector();
    
    public void addLocaleChangeListener(LocaleChangeListener obj)
    {
	localeListener.add(obj);
    }
    
    public void fireLocaleChanged(Locale locale)
    {
	ResourceBundle resource = ResourceBundle.getBundle("gfa.ui.resource.GfaResource", locale);
	for (int i = 0; i < localeListener.size(); i++)
	    ((LocaleChangeListener) localeListener.get(i)).localeChanged(resource);
    }
    
    
    public static final String gfaDemoRomName =
	"roms/gfa-splash.zip"; // gfx by Sonik (www.dream-emulation.fr.st)
}

