package com.lemoulinstudio.gfa.jse.ui;

import com.lemoulinstudio.gfa.jse.ui.action.LocaleChangeListener;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

public abstract class InternationalTableModel extends AbstractTableModel
        implements LocaleChangeListener {

  protected String[] columnName;
  private String tableKey;

  public InternationalTableModel(UserInterface ui, String key) {
    super();
    tableKey = key;
    columnName = new String[0];
    ui.addLocaleChangeListener(this);
  }

  public void localeChanged(ResourceBundle resource) {
    for (int i = 0; i < columnName.length; i++)
      try {columnName[i] = resource.getString(tableKey + ".columnName_" + i);}
      catch(MissingResourceException e) {columnName[i] = "";}

    fireTableStructureChanged();
  }

  public int getColumnCount() {
    return columnName.length;
  }

  public String getColumnName(int columnNumber) {
    return columnName[columnNumber];
  }
  
}
