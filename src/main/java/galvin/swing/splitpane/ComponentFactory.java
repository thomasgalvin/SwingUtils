/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
*/

package galvin.swing.splitpane;

import java.awt.Component;


public interface ComponentFactory
{
    public Component createComponent( Component currentSelection );
    public void removeComponent( Component currentSelection );
}
