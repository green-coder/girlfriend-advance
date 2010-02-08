package gfa.ui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class GfaMenuBar extends JMenuBar {

  public GfaMenuBar(UserInterface ui) {
    JMenu fileMenu = new JMenu(ui.fileMenuAction);
    JMenuItem loadRomItem = new ExtendedMenuItem(ui.loadRomAction);
    JMenuItem exitItem = new ExtendedMenuItem(ui.exitAction);

    JMenu executionMenu = new JMenu(ui.executionMenuAction);
    JMenuItem resetItem = new ExtendedMenuItem(ui.resetAction);
    JMenuItem runItem = new ExtendedMenuItem(ui.runAction);
    JMenuItem stopItem = new ExtendedMenuItem(ui.stopAction);
    JMenuItem stepItem = new ExtendedMenuItem(ui.stepAction);
    JMenuItem undoItem = new ExtendedMenuItem(ui.undoAction);
    JMenuItem nextItem = new ExtendedMenuItem(ui.nextAction);

    JMenu internationalMenu = new JMenu(ui.internationalMenuAction);
    JMenuItem frenchItem = new ExtendedMenuItem(ui.frenchLanguageAction);
    JMenuItem twChineseItem = new ExtendedMenuItem(ui.twChineseLanguageAction);
    JMenuItem japaneseItem = new ExtendedMenuItem(ui.japaneseLanguageAction);
    JMenuItem vietnameseItem = new ExtendedMenuItem(ui.vietnameseLanguageAction);
    JMenuItem thaiItem = new ExtendedMenuItem(ui.thaiLanguageAction);
    JMenuItem chineseItem = new ExtendedMenuItem(ui.chineseLanguageAction);
    JMenuItem englishItem = new ExtendedMenuItem(ui.englishLanguageAction);

    JMenu helpMenu = new JMenu(ui.helpMenuAction);
    JMenuItem aboutItem = new ExtendedMenuItem(ui.aboutAction);
    JMenuItem documentationItem = new ExtendedMenuItem(ui.documentationAction);

    fileMenu.add(loadRomItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);

    executionMenu.add(resetItem);
    executionMenu.add(runItem);
    executionMenu.add(stopItem);
    executionMenu.add(stepItem);
    executionMenu.add(undoItem);
    executionMenu.add(nextItem);

    internationalMenu.add(frenchItem);
    internationalMenu.add(twChineseItem);
    internationalMenu.add(japaneseItem);
    internationalMenu.add(vietnameseItem);
    internationalMenu.add(thaiItem);
    internationalMenu.add(chineseItem);
    internationalMenu.add(englishItem);

    helpMenu.add(documentationItem);
    helpMenu.add(aboutItem);

    add(fileMenu);
    add(executionMenu);
    add(internationalMenu);
    add(helpMenu);
  }
  
}
