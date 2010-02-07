package gfa.ui.action;

import gfa.ui.*;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class LocaleChangeAction
    extends InternationalAction
{
    protected UserInterface ui;
    protected Locale locale;
    
    public LocaleChangeAction(UserInterface ui, String key, Locale locale)
    {
	super(ui, key);
	this.ui = ui;
	this.locale = locale;
    }
    
    public void actionPerformed(ActionEvent event)
    {
	ui.fireLocaleChanged(locale);
    }
}
