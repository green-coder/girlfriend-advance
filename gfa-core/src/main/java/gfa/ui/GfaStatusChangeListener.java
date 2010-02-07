package gfa.ui;

public interface GfaStatusChangeListener
{
    public static int STATUS_EXECUTION_STOPPED   = 0;
    public static int STATUS_EXECUTION_RUNNING   = 1;
    public static int STATUS_NO_GAMEPAK_PLUGGED  = 2;
    
    public void gfaStatusChanged(int status);
}
