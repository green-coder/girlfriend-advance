import gfa.ui.UserInterface;

public class Main {

  public static void main(String[] args) {
    UserInterface ui = (args.length >= 1 ? new UserInterface(args[0]) : new UserInterface());
    ui.setVisible(true);
  }
  
  public static final String infoMsg =
          "GirlfriendAdvance Blonde Edition - by karma\n"
          + "Official website: http://gfa.emu-france.com\n"
          + "This software is an environment for the development on GBA.";
}
