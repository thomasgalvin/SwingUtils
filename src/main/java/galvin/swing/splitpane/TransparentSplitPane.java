/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
*/

package galvin.swing.splitpane;


import java.awt.Component;
import javax.swing.JSplitPane;

public class TransparentSplitPane
    extends JSplitPane
{

    public TransparentSplitPane( int newOrientation, boolean newContinuousLayout,
                                 Component newLeftComponent,
                                 Component newRightComponent )
    {
        super( newOrientation, newContinuousLayout, newLeftComponent, newRightComponent );
        initUi();
    }

    public TransparentSplitPane( int newOrientation, Component newLeftComponent,
                                 Component newRightComponent )
    {
        super( newOrientation, newLeftComponent, newRightComponent );
        initUi();
    }

    public TransparentSplitPane( int newOrientation, boolean newContinuousLayout )
    {
        super( newOrientation, newContinuousLayout );
        initUi();
    }

    public TransparentSplitPane( int newOrientation )
    {
        super( newOrientation );
        initUi();
    }

    public TransparentSplitPane()
    {
        initUi();
    }

    private void initUi()
    {
        setUI( new TransparentSplitPaneUI() );
        setOpaque( false );
        setBorder( null );
        setResizeWeight( 0.5 );
    }
}
