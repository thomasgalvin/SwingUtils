/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
*/

package galvin.swing.splitpane;

import java.awt.Component;
import javax.swing.JSplitPane;



public interface JSplitPaneFactory
{
    public JSplitPane create( int orientation, boolean continuousLayout, Component left, Component right );
}
