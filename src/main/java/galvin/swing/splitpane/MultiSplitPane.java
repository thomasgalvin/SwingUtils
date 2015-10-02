/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.splitpane;

import galvin.swing.ApplicationWindow;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MultiSplitPane
    extends JPanel
    implements FocusListener
{

    private ComponentFactory componentFactory;
    private List<Component> components = new ArrayList();
    private Component currentComponent;
    private JSplitPaneFactory splitPaneFactory;

    public MultiSplitPane( ComponentFactory componentFactory )
    {
        this( componentFactory, null );
    }

    public MultiSplitPane( ComponentFactory componentFactory, boolean transparentPanes )
    {
        this( componentFactory, transparentPanes ? new TransparentSplitPaneFactory() : new DefaultSplitPaneFactory() );
        if( transparentPanes )
        {
            setOpaque( false );
        }
    }

    public MultiSplitPane( ComponentFactory componentFactory, JSplitPaneFactory splitPaneFactory )
    {
        this.componentFactory = componentFactory;
        currentComponent = componentFactory.createComponent( null );
        currentComponent.addFocusListener( this );
        components.add( currentComponent );

        if( splitPaneFactory == null )
        {
            this.splitPaneFactory = new DefaultSplitPaneFactory();
        }
        else
        {
            this.splitPaneFactory = splitPaneFactory;
        }

        setLayout( new BorderLayout() );
        add( currentComponent, BorderLayout.CENTER );
    }

    public synchronized void splitHorizontal()
    {
        split( JSplitPane.HORIZONTAL_SPLIT );
    }

    public synchronized void splitVertical()
    {
        split( JSplitPane.VERTICAL_SPLIT );
    }

    public synchronized void noSplit()
    {
        BorderLayout layout = (BorderLayout)getLayout();
        Component component = layout.getLayoutComponent( this, BorderLayout.CENTER );
        remove( component );
        add( currentComponent, BorderLayout.CENTER );
        components.clear();
        components.add( currentComponent );
        
        GuiUtils.forceRepaint( this );
    }

    private void split( int orientation )
    {
        Container container = getContainer();
        Component newComponent = componentFactory.createComponent( currentComponent );
        components.add( newComponent );
        newComponent.addFocusListener( this );
        JSplitPane newSplitPane = null;

        if( container == this )
        {
            newSplitPane = splitPaneFactory.create( orientation, true, currentComponent, newComponent );
            add( newSplitPane, BorderLayout.CENTER );
        }
        else
        {
            JSplitPane currentSplitPane = (JSplitPane)container;
            int dividerLocation = currentSplitPane.getDividerLocation();
            boolean leftComponent = currentSplitPane.getLeftComponent() == currentComponent;

            newSplitPane = splitPaneFactory.create( orientation, true, currentComponent, newComponent );

            if( leftComponent )
            {
                currentSplitPane.setLeftComponent( newSplitPane );
            }
            else
            {
                currentSplitPane.setRightComponent( newSplitPane );
            }

            currentSplitPane.setDividerLocation( dividerLocation );
        }

        newSplitPane.setBorder( null );
        GuiUtils.forceRepaint( this );
        newSplitPane.setDividerLocation( 0.5d );
        GuiUtils.forceRepaint( this );
        newComponent.requestFocus();
    }

    public Component getCurrentComponent()
    {
        return currentComponent;
    }
    
    public List<Component> getAllComponents()
    {
        List<Component> result = new ArrayList( components.size() );
        result.addAll( components );
        return result;
    }
    
    private Container getContainer()
    {
        Container container = currentComponent.getParent();

        if( container == this )
        {
            return this;
        }
        while( !( container instanceof JSplitPane ) )
        {
            container = container.getParent();
        }


        return container;
    }

    public void focusGained( FocusEvent evt )
    {
        currentComponent = evt.getComponent();
    }

    public void focusLost( FocusEvent evt )
    {
    }

    public static void main( String[] args )
    {
        try
        {
            MultiSplitPane splitPane = new MultiSplitPane( new TestComponentFactory() );
            splitPane.add(  splitPane.new ControlPanel(), BorderLayout.SOUTH );

            ApplicationWindow window = new ApplicationWindow( "MultiSplitPane" );
            window.getContentPane().add( splitPane );
            window.setSize( 1000, 750 );
            window.center();
            window.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public static class DefaultSplitPaneFactory
        implements JSplitPaneFactory
    {

        public JSplitPane create( int orientation, boolean continuousLayout,
                                  Component left, Component right )
        {
            return new JSplitPane( orientation, continuousLayout, left, right );
        }

    }

    public static class TransparentSplitPaneFactory
        implements JSplitPaneFactory
    {

        public JSplitPane create( int orientation, boolean continuousLayout,
                                  Component left, Component right )
        {
            return new TransparentSplitPane( orientation, continuousLayout, left, right );
        }

    }

    ///////
    // Test
    ///////

    private static class TestComponentFactory
        implements ComponentFactory
    {

        private int count = 0;

        public Component createComponent( Component currentSelection )
        {
            count++;
            final String text = "-- Content " + count + " --";
            JTextArea result = new JTextArea();
            result.setText( text );
            return result;
        }

        public void removeComponent( Component currentSelection )
        {
        }
    }

    private class ControlPanel
        extends JPanel
        implements ActionListener
    {
        private JButton horizontalButton = new JButton( "Split Horizontal" );
        private JButton verticalButton = new JButton( "Split Vertical" );
        private JButton resetButton = new JButton( "Reset" );

        public ControlPanel()
        {
           setLayout( new FlowLayout() );
           add( horizontalButton );
           add( verticalButton );
           add( resetButton );

           horizontalButton.addActionListener( this );
           verticalButton.addActionListener( this );
           resetButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Object source = evt.getSource();
            if( source == horizontalButton )
            {
                splitHorizontal();
            }
            else if( source == verticalButton )
            {
                splitVertical();
            }
            else if( source == resetButton )
            {
                noSplit();
            }
        }

    }

}
