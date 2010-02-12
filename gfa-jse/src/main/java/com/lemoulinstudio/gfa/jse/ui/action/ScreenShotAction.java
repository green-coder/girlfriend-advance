package com.lemoulinstudio.gfa.jse.ui.action;

import com.lemoulinstudio.gfa.jse.GirlfriendAdvance;
import com.lemoulinstudio.gfa.jse.ui.UserInterface;
import com.lemoulinstudio.gfa.core.util.PngEncoder;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShotAction extends InternationalAction
        implements Runnable {

  protected GirlfriendAdvance gfa;
  protected UserInterface ui;
  protected SimpleDateFormat dateFormat;
  protected boolean screenShotRequested;
  protected Image lcdImage;

  public ScreenShotAction(UserInterface ui, GirlfriendAdvance gfa) {
    super(ui, "ScreenShotAction");
    this.gfa = gfa;
    this.ui = ui;
    screenShotRequested = false;
    dateFormat = new SimpleDateFormat("dd_MM_yyyy HH_mm_ss_SSS");
    lcdImage = gfa.getLcd().getImage();
  }

  public void actionPerformed(ActionEvent event) {
    if (isEnabled()) {
      setEnabled(false);
      Thread t = new Thread(this);
      t.start();
    }
  }

  protected String getPngFileName() {
    String fileName = gfa.getMemory().getRomFileName();
    fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
    String filename = fileName.toLowerCase();
    if (filename.endsWith(".bin")
            || filename.endsWith(".agb")
            || filename.endsWith(".gba")
            || filename.endsWith(".zip")
            || filename.endsWith(".rom"))
      fileName = fileName.substring(0, fileName.length() - ".xxx".length());
    return fileName + " " + dateFormat.format(new Date()) + ".png";
  }

  /**
   * Save the image currently displayed to the LCD into a PNG file.
   */
  public void run() {
    try {
      PngEncoder encoder = new PngEncoder(lcdImage, false, PngEncoder.FILTER_NONE, 9);
      FileOutputStream pngFile = new FileOutputStream(getPngFileName());
      pngFile.write(encoder.pngEncode());
      pngFile.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    setEnabled(true);
  }

}
