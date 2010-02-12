package com.lemoulinstudio.gfa.jse.ui.resource;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import javax.swing.KeyStroke;

public class GfaResource_en extends ListResourceBundle {

  public Object[][] getContents() {
    return list;
  }
  static Object[][] list = {
    // The resources for menu titles in the menu bar.

    {"FileMenuAction.NAME", "File"},
    {"FileMenuAction.MNEMONIC_KEY", "f"},
    {"ExecutionMenuAction.NAME", "Execution"},
    {"ExecutionMenuAction.MNEMONIC_KEY", "e"},
    {"InternationalMenuAction.NAME", "International"},
    {"InternationalMenuAction.MNEMONIC_KEY", "i"},
    {"HelpMenuAction.NAME", "Help"},
    {"HelpMenuAction.MNEMONIC_KEY", "h"},

    // The resources for the various action of gfa.

    {"LoadRomAction.NAME",
      "Load Rom ..."},
    {"LoadRomAction.SHORT_DESCRIPTION",
      "Load a rom file into the memory and reset the cpu state."},
    {"LoadRomAction.LONG_DESCRIPTION",
      "Open a file chooser frame in order the user to load "
      + "a rom file into the memory and reset the cpu state."},
    {"LoadRomAction.MNEMONIC_KEY", "l"},
    {"LoadRomAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK)},

    {"ExitAction.NAME",
      "Exit"},
    {"ExitAction.SHORT_DESCRIPTION",
      "Exit Girlfriend Advance."},
    {"ExitAction.MNEMONIC_KEY",
      "x"},

    {"ResetAction.NAME",
      "Reset"},
    {"ResetAction.SHORT_DESCRIPTION",
      "Reset the emulator state."},
    {"ResetAction.MNEMONIC_KEY",
      "e"},
    {"ResetAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK)},

    {"RunAction.NAME",
      "Run"},
    {"RunAction.SHORT_DESCRIPTION",
      "Start the execution of the program."},
    {"RunAction.MNEMONIC_KEY",
      "r"},
    {"RunAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)},

    {"StopAction.NAME",
      "Stop"},
    {"StopAction.SHORT_DESCRIPTION",
      "Stop the execution of the program."},
    {"StopAction.MNEMONIC_KEY",
      "o"},
    {"StopAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0)},

    {"StepAction.NAME",
      "Step"},
    {"StepAction.SHORT_DESCRIPTION",
      "Execute the current instruction."},
    {"StepAction.MNEMONIC_KEY",
      "s"},
    {"StepAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)},

    {"UndoAction.NAME",
      "Undo"},
    {"UndoAction.SHORT_DESCRIPTION",
      "Undo the last executed instruction."},
    {"UndoAction.LONG_DESCRIPTION",
      "Undo the last executed instruction. "
      + "This option should be actived explicitly since it causes a lot of state backup "
      + "and consequently makes the execution to slow down."},
    {"UndoAction.MNEMONIC_KEY",
      "u"},
    {"UndoAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0)},

    {"NextAction.NAME",
      "Next"},
    {"NextAction.SHORT_DESCRIPTION",
      "Run until not arrived to the following instruction."},
    {"NextAction.MNEMONIC_KEY",
      "n"},
    {"NextAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0)},

    {"BreakNextAction.NAME",
      "Break Next"},
    {"BreakNextAction.SHORT_DESCRIPTION",
      "Run until the specified condition is true."},

    {"BreakPrevAction.NAME",
      "Break Prev"},
    {"BreakPrevAction.SHORT_DESCRIPTION",
      "Roll back when the specified condition was true."},

    {"FrenchLanguageAction.NAME",
      "French"},

    {"TwChineseLanguageAction.NAME",
      "Traditional chinese"},

    {"JapaneseLanguageAction.NAME",
      "Japanese"},

    {"VietnameseLanguageAction.NAME",
      "Vietnamese"},

    {"ThaiLanguageAction.NAME",
      "Thai"},

    {"ChineseLanguageAction.NAME",
      "Simplified chinese"},

    {"EnglishLanguageAction.NAME",
      "English"},

    {"DocumentationAction.NAME",
      "Gfa Documentation"},
    {"DocumentationAction.SHORT_DESCRIPTION",
      "The Girlfriend Advance documentation."},
    {"DocumentationAction.LONG_DESCRIPTION",
      "Display the Girlfriend Advance documentation."},
    {"DocumentationAction.MNEMONIC_KEY",
      "d"},
    {"DocumentationAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)},

    {"AboutAction.NAME",
      "About"},
    {"AboutAction.SHORT_DESCRIPTION",
      "Informations about Girlfriend Advance."},
    {"AboutAction.LONG_DESCRIPTION",
      "Display informations about Girlfriend Advance and his author "
      + "Vincent Cantin, also known as \"karma of revelation\"."},
    {"AboutAction.MNEMONIC_KEY",
      "a"},

    {"HomeDisasmAction.NAME",
      "Home"},
    {"HomeDisasmAction.SHORT_DESCRIPTION",
      "Back to current instruction."},
    {"HomeDisasmAction.LONG_DESCRIPTION",
      "Cause the viewport of the disassembler componant to show the current instruction."},
    {"HomeDisasmAction.ACCELERATOR_KEY",
      KeyStroke.getKeyStroke(KeyEvent.VK_HOME, Event.CTRL_MASK)},

    // The resources for tables.

    {"DisassemblerTableModel.columnName_0", "Offset"},
    {"DisassemblerTableModel.columnName_1", "Opcode"},
    {"DisassemblerTableModel.columnName_2", "Instruction"},

    {"RegisterTableModel.columnName_0", "Name"},
    {"RegisterTableModel.columnName_1", "Value"},

    // The resources for the disassembler menu.

    {"ViewMenuDisasmAction.NAME", "View"},
    {"BiosRomDisasmAction.NAME", "BiosRom"},
    {"ExternalRamDisasmAction.NAME", "ExternalRam"},
    {"WorkRamDisasmAction.NAME", "WorkRam"},
    {"IoRegDisasmAction.NAME", "I/O Registers"},
    {"PaletteRamDisasmAction.NAME", "PaletteRam"},
    {"VideoRamDisasmAction.NAME", "VideoRam"},
    {"OamRamDisasmAction.NAME", "OamRam"},
    {"GamepakRomDisasmAction.NAME", "GamepakRom"},
    {"CartRamDisasmAction.NAME", "CartRam"},

    {"TrackMenuDisasmAction.NAME", "Tracking"},
    {"NoTrackingDisasmAction.NAME", "No tracking"},
    {"CenterTrackingDisasmAction.NAME", "Centered tracking"},
    {"WindowTrackingDisasmAction.NAME", "Windowed tracking"},

    // The resources for InputPanel.

    {"InputPanel.up", "Up"},
    {"InputPanel.down", "Down"},
    {"InputPanel.left", "Left"},
    {"InputPanel.right", "Right"},
    {"InputPanel.select", "Select"},
    {"InputPanel.start", "Start"},
    {"InputPanel.a", "A"},
    {"InputPanel.b", "B"},
    {"InputPanel.l", "L"},
    {"InputPanel.r", "R"}
  };

}
