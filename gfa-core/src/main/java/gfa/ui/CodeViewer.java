package gfa.ui;

import gfa.*;
import gfa.ui.action.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CodeViewer
    extends JScrollPane
    implements MouseListener,
	       GfaStateChangeListener
{
    protected DisassemblerTableModel tableModel;
    protected JTable table;
    protected JPopupMenu popupMenu;
    protected int trackingPolicy;
    
    public static final int NO_TRACKING     = 0;
    public static final int CENTER_TRACKING = 1;
    public static final int WINDOW_TRACKING = 2;
    
    public CodeViewer(UserInterface ui, GirlfriendAdvance gfa)
    {
	super();
	
	tableModel = new DisassemblerTableModel(ui, gfa);
	table = new JTable(tableModel);
	table.setSelectionBackground(Color.pink);
	ui.addGfaStateChangeListener(this);

	setViewportView(table);
	getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
	
	ui.homeDisasmAction        = new HomeDisasmAction(ui, this);
	
	ui.viewMenuDisasmAction    = new DoNothingAction(ui, "ViewMenuDisasmAction");
	ui.biosRomDisasmAction     = new ViewMemDisasmAction(ui, this, 0x00);
	ui.externalRamDisasmAction = new ViewMemDisasmAction(ui, this, 0x02);
	ui.workRamDisasmAction     = new ViewMemDisasmAction(ui, this, 0x03);
	ui.ioRegDisasmAction       = new ViewMemDisasmAction(ui, this, 0x04);
	ui.paletteRamDisasmAction  = new ViewMemDisasmAction(ui, this, 0x05);
	ui.videoRamDisasmAction    = new ViewMemDisasmAction(ui, this, 0x06);
	ui.oamRamDisasmAction      = new ViewMemDisasmAction(ui, this, 0x07);
	ui.gamepakRomDisasmAction  = new ViewMemDisasmAction(ui, this, 0x08);
	ui.cartRamDisasmAction     = new ViewMemDisasmAction(ui, this, 0x0e);
	
	ui.trackMenuDisasmAction       = new DoNothingAction(ui, "TrackMenuDisasmAction");
	ui.noTrackingDisasmAction      = new TrackingDisasmAction(ui, "No", this, NO_TRACKING);
	ui.centerTrackingDisasmAction  = new TrackingDisasmAction(ui, "Center", this, CENTER_TRACKING);
	ui.windowTrackingDisasmAction  = new TrackingDisasmAction(ui, "Window", this, WINDOW_TRACKING);
	
	JButton home = new ToolBarButton(ui.homeDisasmAction);
	setCorner(JScrollPane.UPPER_RIGHT_CORNER, home);
	
	popupMenu = new JPopupMenu();
	popupMenu.add(ui.homeDisasmAction);
	popupMenu.addSeparator();
	
	JMenu viewMenu = new JMenu(ui.viewMenuDisasmAction);
	
	viewMenu.add(new ExtendedMenuItem(ui.biosRomDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.externalRamDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.workRamDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.ioRegDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.paletteRamDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.videoRamDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.oamRamDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.gamepakRomDisasmAction));
	viewMenu.add(new ExtendedMenuItem(ui.cartRamDisasmAction));
	
	popupMenu.add(viewMenu);
	
	JRadioButtonMenuItem noTrackingItem     = new JRadioButtonMenuItem(ui.noTrackingDisasmAction);
	JRadioButtonMenuItem centerTrackingItem = new JRadioButtonMenuItem(ui.centerTrackingDisasmAction);
	JRadioButtonMenuItem windowTrackingItem = new JRadioButtonMenuItem(ui.windowTrackingDisasmAction);
	
	windowTrackingItem.setSelected(true);
	trackingPolicy = WINDOW_TRACKING;
	
	
	ButtonGroup trackingGroup = new ButtonGroup();
	trackingGroup.add(noTrackingItem);
	trackingGroup.add(centerTrackingItem);
	trackingGroup.add(windowTrackingItem);
	
	JMenu trackingMenu = new JMenu(ui.trackMenuDisasmAction);
	trackingMenu.add(noTrackingItem);
	trackingMenu.add(centerTrackingItem);
	trackingMenu.add(windowTrackingItem);
	
	popupMenu.add(trackingMenu);
	
	table.addMouseListener(this);
    }
    
    public void mouseClicked(MouseEvent e)
    {
	//System.out.println("click !");
    }
    
    public void mouseEntered(MouseEvent e)
    {
	//System.out.println("enter !");
    }
    
    public void mouseExited(MouseEvent e)
    {
	//System.out.println("exit !");
    }
    
    public void mousePressed(MouseEvent e)
    {
	//System.out.println("pressed !");
    }
    
    public void mouseReleased(MouseEvent e)
    {
	if (e.isPopupTrigger())
	    popupMenu.show(table, e.getX(), e.getY());
	//System.out.println("released !");
    }
    
    public void setMemorySetViewed(int memorySetToView)
    {
	tableModel.setMemorySetViewed(memorySetToView);
    }
    
    public void goHome()
    {
	goHome(false);
    }
    
    public void goHome(boolean force)
    {
	int tracking = trackingPolicy;
	if (force && (tracking == NO_TRACKING))
	    tracking = CENTER_TRACKING;
	
	int yRow = -1;
	Rectangle rect;
	JViewport viewport;
	int rowHeight;
	
	switch (tracking)
	    {
	    case NO_TRACKING:
		yRow = tableModel.getPcRow();
		break;
		
	    case CENTER_TRACKING:
		tableModel.goHome();
		yRow = tableModel.getPcRow();
		viewport = getViewport();
		rect = viewport.getViewRect();
		rowHeight = table.getRowHeight();
		int y = yRow * rowHeight - rect.height / 2;
		if (y < 0) y = 0;
		viewport.setViewPosition(new Point(0, y));
		break;
		
	    case WINDOW_TRACKING:
		tableModel.goHome();
		yRow = tableModel.getPcRow();
		viewport = getViewport();
		rect = viewport.getViewRect();
		rowHeight = table.getRowHeight();
		if (!rect.contains(0, yRow * rowHeight, 1, rowHeight))
		    viewport.setViewPosition(new Point(0, yRow * rowHeight));
		break;
		
	    default:
	    }
	
	// Select the current instruction row.
	if (yRow >= 0) table.setRowSelectionInterval(yRow, yRow);
    }
    
    public void setTrackingPolicy(int trackingPolicy)
    {
	this.trackingPolicy = trackingPolicy;
	goHome();
    }
    
    public void gfaStateChanged()
    {
	tableModel.gfaStateChanged();
	goHome();
    }
}
