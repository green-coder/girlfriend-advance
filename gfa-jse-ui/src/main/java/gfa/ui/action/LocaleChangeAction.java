package gfa.ui.action;

import gfa.ui.UserInterface;
import java.awt.event.ActionEvent;
import java.util.Locale;

public class LocaleChangeAction
        extends InternationalAction {

  protected UserInterface ui;
  protected Locale locale;

  public LocaleChangeAction(UserInterface ui, String key, Locale locale) {
    super(ui, key);
    this.ui = ui;
    this.locale = locale;
  }

  public void actionPerformed(ActionEvent event) {
    ui.fireLocaleChanged(locale);
  }
  
}
