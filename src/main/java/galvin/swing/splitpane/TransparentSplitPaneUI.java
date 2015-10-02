/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
*/

package galvin.swing.splitpane;


import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
* TransparentSplitPaneUI.
*/
public class TransparentSplitPaneUI
        extends BasicSplitPaneUI
{
    @Override
    public BasicSplitPaneDivider createDefaultDivider()
    {
        return new TransparentSplitPaneDivider( this );
    }

    @Override
    public BasicSplitPaneDivider createDefaultNonContinuousLayoutDivider()
    {
        return new TransparentSplitPaneDivider( this );
    }

    public static ComponentUI createUI( JComponent component)
    {
        return new TransparentSplitPaneUI();
    }

    @Override
    public BasicSplitPaneDivider getDivider()
    {
        return new TransparentSplitPaneDivider( this );
    }

    public static class TransparentSplitPaneDivider
            extends BasicSplitPaneDivider
    {
        public TransparentSplitPaneDivider(BasicSplitPaneUI ui)
        {
            super( ui );
        }

        @Override
        public void paint( Graphics g )
        {
            //no-op, this is transparent, remember?
        }
    }
}
