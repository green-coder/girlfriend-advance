package gfa.ui.resource;

import java.util.ListResourceBundle;

public class GfaResource extends ListResourceBundle {

  public Object[][] getContents() {
    return list;
  }
  // Mettre ici tout ce qui est commun a toutes les langues.
  static Object[][] list = {
    {"ResetAction.SMALL_ICON", "gfa/ui/resource/reset32.jpg"},
    {"RunAction.SMALL_ICON", "gfa/ui/resource/run32.jpg"},
    {"StopAction.SMALL_ICON", "gfa/ui/resource/stop32.jpg"},
    {"StepAction.SMALL_ICON", "gfa/ui/resource/step32.jpg"},
    {"UndoAction.SMALL_ICON", "gfa/ui/resource/undo32.jpg"},
    {"NextAction.SMALL_ICON", "gfa/ui/resource/next32.jpg"},
    {"BreakPrevAction.SMALL_ICON", "gfa/ui/resource/break_prev32.jpg"},
    {"BreakNextAction.SMALL_ICON", "gfa/ui/resource/break_next32.jpg"},
    {"ScreenShotAction.SMALL_ICON", "gfa/ui/resource/screenshot32.jpg"},
    {"HomeDisasmAction.SMALL_ICON", "gfa/ui/resource/home10.jpg"}
  };
  
}
