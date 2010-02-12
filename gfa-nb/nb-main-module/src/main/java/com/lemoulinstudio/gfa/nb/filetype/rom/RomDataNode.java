package com.lemoulinstudio.gfa.nb.filetype.rom;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

public class RomDataNode extends DataNode {

  private static final String IMAGE_ICON_BASE = "com/lemoulinstudio/gfa/nb/filetype/rom/green_mushroom16.png";

  RomDataNode(RomDataObject obj, Lookup lookup) {
    super(obj, Children.LEAF, lookup);
    setIconBaseWithExtension(IMAGE_ICON_BASE);
  }
  
//    /** Creates a property sheet. */
//    @Override
//    protected Sheet createSheet() {
//        Sheet s = super.createSheet();
//        Sheet.Set ss = s.get(Sheet.PROPERTIES);
//        if (ss == null) {
//            ss = Sheet.createPropertiesSet();
//            s.put(ss);
//        }
//        // TODO add some relevant properties: ss.put(...)
//        return s;
//    }
}
