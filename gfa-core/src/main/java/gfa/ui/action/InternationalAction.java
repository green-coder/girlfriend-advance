package gfa.ui.action;

import gfa.*;
import gfa.ui.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class InternationalAction
    extends AbstractAction
    implements LocaleChangeListener
{
    private String actionKey;
    
    public InternationalAction(UserInterface ui, String key)
    {
	super();
	actionKey = key;
	ui.addLocaleChangeListener(this);
    }
    
    public void localeChanged(ResourceBundle resource)
    {
	try {putValue(NAME, resource.getString(actionKey + ".NAME"));}
	catch(MissingResourceException e) {putValue(NAME, "");}
	
	try {putValue(SHORT_DESCRIPTION, resource.getString(actionKey + ".SHORT_DESCRIPTION"));}
	catch(MissingResourceException e) {putValue(SHORT_DESCRIPTION, "");}
	
	try {putValue(LONG_DESCRIPTION, resource.getString(actionKey + ".LONG_DESCRIPTION"));}
	catch(MissingResourceException e) {putValue(LONG_DESCRIPTION, "");}
	
	try {putValue(SMALL_ICON, new ImageIcon(resource.getString(actionKey + ".SMALL_ICON")));}
	catch(MissingResourceException e) {putValue(SMALL_ICON, null);}
	
	try {putValue(MNEMONIC_KEY, new Integer((int) resource.getString(actionKey + ".MNEMONIC_KEY").charAt(0)));}
	catch(MissingResourceException e) {putValue(MNEMONIC_KEY, new Integer(0));}
	
	try {putValue(ACCELERATOR_KEY, resource.getObject(actionKey + ".ACCELERATOR_KEY"));}
	catch(MissingResourceException e) {putValue(ACCELERATOR_KEY, null);}
    }
}
