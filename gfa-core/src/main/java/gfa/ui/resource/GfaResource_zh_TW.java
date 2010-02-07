package gfa.ui.resource;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class GfaResource_zh_TW
    extends ListResourceBundle
{
    public Object[][] getContents()
    {
	return list;
    }
    
    static Object[][] list =
    {
	// The resources for menu titles in the menu bar.
	
	{"FileMenuAction.NAME",
	 "\u6a94\u6848"},
	
	{"ExecutionMenuAction.NAME",
	 "\u57f7\u884c\u6307\u4ee4"},
	
	{"InternationalMenuAction.NAME",
	 "\u591a\u570b\u7248\u672c"},
	
	{"HelpMenuAction.NAME",
	 "\u8aaa\u660e"},
	
	// The resources for the various action of gfa.
	
	{"LoadRomAction.NAME",
	 "\u8f09\u5165Rom ..."},
	{"LoadRomAction.SHORT_DESCRIPTION",
	 "\u8f09\u5165rom\u5230\u8a18\u61b6\u9ad4\u4e2d\u4e26\u4e14\u91cd\u7f6ecpu\u7684\u72c0\u614b"},
	{"LoadRomAction.LONG_DESCRIPTION",
	 "\u958b\u555f\u6a94\u6848\u9078\u64c7\u4ecb\u9762\u8b93\u4f7f\u7528\u8005\u9078\u64c7\u6a94\u6848" +
	 "\u8f09\u5165rom\u5230\u8a18\u61b6\u9ad4\u4e2d\u4e26\u4e14\u91cd\u7f6ecpu\u7684\u72c0\u614b"},
	{"LoadRomAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK)},
	
	{"ExitAction.NAME",
	 "\u96e2\u958b"},
	{"ExitAction.SHORT_DESCRIPTION",
	 "\u96e2\u958b\u7a0b\u5f0f"},
	
	{"ResetAction.NAME",
	 "\u91cd\u7f6e"},
	{"ResetAction.SHORT_DESCRIPTION",
	 "\u91cd\u7f6ecpu."},
	{"ResetAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F2, Event.CTRL_MASK)},
	
	{"RunAction.NAME",
	 "\u57f7\u884c"},
	{"RunAction.SHORT_DESCRIPTION",
	 "\u958b\u59cb\u57f7\u884c\u7a0b\u5f0f"},
	{"RunAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)},
	
	{"StopAction.NAME",
	 "\u505c\u6b62"},
	{"StopAction.SHORT_DESCRIPTION",
	 "\u505c\u6b62\u57f7\u884c\u4e2d\u7684\u7a0b\u5f0f"},
	{"StopAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0)},
	
	{"StepAction.NAME",
	 "\u55ae\u6b65"},
	{"StepAction.SHORT_DESCRIPTION",
	 "\u57f7\u884c\u76ee\u524d\u7684\u6307\u4ee4"},
	{"StepAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0)},
	
	{"UndoAction.NAME",
	 "\u56de\u5fa9"},
	{"UndoAction.SHORT_DESCRIPTION",
	 "\u53d6\u6d88\u4e0a\u4e00\u6307\u4ee4\u7684\u57f7\u884c"},
	{"UndoAction.LONG_DESCRIPTION",
	 "\u53d6\u6d88\u4e0a\u4e00\u6307\u4ee4\u7684\u57f7\u884c" +
	 "\u9019\u500b\u529f\u80fd\u6703\u660e\u78ba\u5730\u5c07\u76f8\u95dc\u7684\u5099\u4efd\u8cc7\u6599\u9084\u539f" +
	 "\u5fc5\u7136\u6703\u4f7f\u57f7\u884c\u901f\u5ea6\u8b8a\u5f97\u5f88\u6162"},
	{"UndoAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0)},
	
	{"NextAction.NAME",
	 "\u4e0b\u4e00\u6b65"},
	{"NextAction.SHORT_DESCRIPTION",
	 "\u57f7\u884c\u4e0b\u4e00\u500b\u6307\u4ee4"},
	{"NextAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0)},
	
	{"FrenchLanguageAction.NAME",
	 "\u6cd5\u6587"},
	
	{"TwChineseLanguageAction.NAME",
	 "BIG5\u4e2d\u6587"},
	
	{"JapaneseLanguageAction.NAME",
	 "\u65e5\u672c\u8a71"},
	
	{"VietnameseLanguageAction.NAME",
	 "\u8d8a\u5357\u8a9e"},
	
	{"ThaiLanguageAction.NAME",
	 "\u6cf0\u570b\u6587"},
	
	{"ChineseLanguageAction.NAME",
	 "GB\u4e2d\u6587"},
	
	{"EnglishLanguageAction.NAME",
	 "\u82f1\u6587"},
	
	{"DocumentationAction.NAME",
	 "\u6587\u4ef6"},
	{"DocumentationAction.SHORT_DESCRIPTION",
	 "\u6a21\u64ec\u5668\u76f8\u95dc\u6587\u4ef6"},
	{"DocumentationAction.LONG_DESCRIPTION",
	 "\u986f\u793a\u6a21\u64ec\u5668\u76f8\u95dc\u6587\u4ef6"},
	{"DocumentationAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)},
	
	{"AboutAction.NAME",
	 "\u95dc\u65bc"},
	{"AboutAction.SHORT_DESCRIPTION",
	 "\u6a21\u64ec\u5668\u76f8\u95dc\u8cc7\u8a0a"},
	{"AboutAction.LONG_DESCRIPTION",
	 "\u986f\u793a\u6a21\u64ec\u5668\u548c\u4f5c\u8005\u7684\u76f8\u95dc\u8cc7\u8a0a" +
	 "Vincent Cantin, also known as \"karma of revelation\"."},
	
	{"HomeDisasmAction.NAME",
	 "\u56de\u5bb6"},
	{"HomeDisasmAction.SHORT_DESCRIPTION",
	 "\u56de\u5230\u76ee\u524d\u6307\u4ee4\u5217"},
	{"HomeDisasmAction.LONG_DESCRIPTION",
	 "\u53cd\u7d44\u8b6f\u5143\u4ef6\u986f\u793a\u5340\u6703\u6307\u5230\u76ee\u524d\u6307\u4ee4\u5217"},
	{"HomeDisasmAction.ACCELERATOR_KEY",
	 KeyStroke.getKeyStroke(KeyEvent.VK_HOME, Event.CTRL_MASK)},
	
	// The resources for tables.
	
	{"CodeViewerTable.column_0", "Offset"},
	{"CodeViewerTable.column_1", "Opcode"},
	{"CodeViewerTable.column_2", "Instruction"},
	
	{"RegisterViewerTable.column_0", "Name"},
	{"RegisterViewerTable.column_1", "Value"},
	
	// The resources for misc componants.
	
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
