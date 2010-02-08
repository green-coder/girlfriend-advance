package gfa.ui.resource;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import javax.swing.KeyStroke;

public class GfaResource_fr extends ListResourceBundle {

  public Object[][] getContents() {
    return list;
  }

  static Object[][] list = {
    // The resources for menu titles in the menu bar.

    {"FileMenuAction.NAME",
      "Fichier"},
    {"FileMenuAction.MNEMONIC_KEY",
      "f"},

    {"ExecutionMenuAction.NAME",
      "Exécution"},
    {"ExecutionMenuAction.MNEMONIC_KEY",
      "e"},

    {"InternationalMenuAction.NAME",
      "International"},
    {"InternationalMenuAction.MNEMONIC_KEY",
      "i"},

    {"HelpMenuAction.NAME",
      "Aide"},
    {"HelpMenuAction.MNEMONIC_KEY",
      "a"},
      
    // The resources for the various action of gfa.

    {"LoadRomAction.NAME",
      "Charger Rom ..."},
    {"LoadRomAction.SHORT_DESCRIPTION",
      "Charge un fichier de rom en mémoire et réinitialise l'état du processeur."},
    {"LoadRomAction.LONG_DESCRIPTION",
      "Ouvre une fenêtre dans laquelle l'utilisateur pourra choisir la rom à "
      + "charger en mémoire. Cette opération réinitialise l'état du processeur."},
    {"LoadRomAction.MNEMONIC_KEY",
      "c"},
    {"LoadRomAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK)},

    {"ExitAction.NAME",
      "Quitter"},
    {"ExitAction.SHORT_DESCRIPTION",
      "Quitte le logiciel Girlfriend Advance."},
    {"ExitAction.MNEMONIC_KEY",
      "q"},

    {"ResetAction.NAME",
      "Réinitialise"},
    {"ResetAction.SHORT_DESCRIPTION",
      "Réinitialise le processeur."},
    {"ResetAction.MNEMONIC_KEY",
      "i"},
    {"ResetAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK)},

    {"RunAction.NAME",
      "Exécute"},
    {"RunAction.SHORT_DESCRIPTION",
      "Lance l'exécution du programme."},
    {"RunAction.MNEMONIC_KEY",
      "e"},
    {"RunAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)},

    {"StopAction.NAME",
      "Stop"},
    {"StopAction.SHORT_DESCRIPTION",
      "Stoppe l'exécution du programme."},
    {"StopAction.MNEMONIC_KEY",
      "t"},
    {"StopAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0)},

    {"StepAction.NAME",
      "Marche"},
    {"StepAction.SHORT_DESCRIPTION",
      "Exécute l'instruction courante."},
    {"StepAction.MNEMONIC_KEY",
      "m"},
    {"StepAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)},

    {"UndoAction.NAME",
      "Revient"},
    {"UndoAction.SHORT_DESCRIPTION",
      "Annule la dernière instruction."},
    {"UndoAction.LONG_DESCRIPTION",
      "Annule l'effet de l'exécution de la dernière instruction. "
      + "Cette option doit être activée explicitement parcequ'elle cause beaucoup "
      + "de sauvegardes de l'état de l'émulateur, et par conséquent elle ralentit "
      + "considérablement l'exécution du programme émulé."},
    {"UndoAction.MNEMONIC_KEY",
      "r"},
    {"UndoAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0)},

    {"NextAction.NAME",
      "Suivante"},
    {"NextAction.SHORT_DESCRIPTION",
      "Exécute le programme jusqu'à l'instruction suivante."},
    {"NextAction.MNEMONIC_KEY",
      "s"},
    {"NextAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0)},

    {"BreakNextAction.NAME",
      "Break Next"},
    {"BreakNextAction.SHORT_DESCRIPTION",
      "Exécute jusqu'à ce que la condition spécifiée soit vraie."},

    {"BreakPrevAction.NAME",
      "Break Prev"},
    {"BreakPrevAction.SHORT_DESCRIPTION",
      "Revient au dernier moment où la condition spécifiée était vraie."},

    {"FrenchLanguageAction.NAME",
      "Français"},

    {"TwChineseLanguageAction.NAME",
      "Chinois traditionel"},

    {"JapaneseLanguageAction.NAME",
      "Japonais"},
    {"VietnameseLanguageAction.NAME",
      "Vietnamien"},

    {"ThaiLanguageAction.NAME",
      "Thaïlandais"},

    {"ChineseLanguageAction.NAME",
      "Chinois simplifié"},

    {"EnglishLanguageAction.NAME",
      "Anglais"},

    {"ScreenShotAction.NAME",
      "Photo d'écran"},
    {"ScreenShotAction.SHORT_DESCRIPTION",
      "Prend une photo de l'écran au prochain rafraichissement."},
    {"ScreenShotAction.LONG_DESCRIPTION",
      "Option pour se la jouer à la \"Loft Story\" sur les jeux"},

    {"DocumentationAction.NAME",
      "Documentation de Gfa"},
    {"DocumentationAction.SHORT_DESCRIPTION",
      "La documentation de Girlfriend Advance."},
    {"DocumentationAction.LONG_DESCRIPTION",
      "Affiche la documentation de Girlfriend Advance."},
    {"DocumentationAction.MNEMONIC_KEY",
      "d"},
    {"DocumentationAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)},

    {"AboutAction.NAME",
      "A propos"},
    {"AboutAction.SHORT_DESCRIPTION",
      "Informations à propos de Girlfriend Advance."},
    {"AboutAction.LONG_DESCRIPTION",
      "Affiche les informations concernant Girlfriend Advance et son auteur "
      + "Vincent Cantin, aussi connu sous le pseudonyme de \"karma of revelation\"."},
    {"AboutAction.MNEMONIC_KEY",
      "a"},

    {"HomeDisasmAction.NAME",
      "Maison"},
    {"HomeDisasmAction.SHORT_DESCRIPTION",
      "Revient à l'instruction courante."},
    {"HomeDisasmAction.LONG_DESCRIPTION",
      "Rend l'instruction courante visible sur le composant du désassembleur."},
    {"HomeDisasmAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_HOME, Event.CTRL_MASK)},

    // The resources for tables.

    {"DisassemblerTableModel.columnName_0", "Position"},
    {"DisassemblerTableModel.columnName_1", "Opcode"},
    {"DisassemblerTableModel.columnName_2", "Instruction"},

    {"RegisterTableModel.columnName_0", "Nom"},
    {"RegisterTableModel.columnName_1", "Valeur"},

    // The resources for the disassembler menu.

    {"ViewMenuDisasmAction.NAME", "Voir"},
    {"BiosRomDisasmAction.NAME", "Rom du bios"},
    {"ExternalRamDisasmAction.NAME", "Ram externe"},
    {"WorkRamDisasmAction.NAME", "Ram de travail"},
    {"IoRegDisasmAction.NAME", "Registres e/s matérielles"},
    {"PaletteRamDisasmAction.NAME", "Ram de la palette"},
    {"VideoRamDisasmAction.NAME", "Ram vidéo"},
    {"OamRamDisasmAction.NAME", "Ram d'attribut des sprites"},
    {"GamepakRomDisasmAction.NAME", "Rom de la cartouche"},
    {"CartRamDisasmAction.NAME", "Rom de sauvegarde"},

    {"TrackMenuDisasmAction.NAME", "Suivi"},
    {"NoTrackingDisasmAction.NAME", "Pas de suivi"},
    {"CenterTrackingDisasmAction.NAME", "Suivi centré"},
    {"WindowTrackingDisasmAction.NAME", "Suivi fenêtré"},

    // The resources for InputPanel.

    {"InputPanel.up", "Haut"},
    {"InputPanel.down", "Bas"},
    {"InputPanel.left", "Gauche"},
    {"InputPanel.rigth", "Droite"},
    {"InputPanel.select", "Select"},
    {"InputPanel.start", "Start"},
    {"InputPanel.a", "A"},
    {"InputPanel.b", "B"},
    {"InputPanel.l", "L"},
    {"InputPanel.r", "R"}
  };

}
