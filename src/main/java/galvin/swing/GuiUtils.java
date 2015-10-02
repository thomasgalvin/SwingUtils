/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import galvin.SystemUtils;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public final class GuiUtils
{
    private static final KeyStroke ESCAPE_KEYSTROKE = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
    public static final String CLOSE_DIALOG_ACTION_MAP_KEY = "galvin.swing.dispatch:WINDOW_CLOSING";
    
    private GuiUtils(){}
    
    public static final int PADDING = 5;
    
    public static void center( Window window )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = window.getSize();
        int x = ( screenSize.width - frameSize.width ) / 2;
        int y = ( screenSize.height - frameSize.height ) / 2;
        window.setLocation( x, y );
    }

    public static ImageIcon createImageIcon( String path )
    {
        URL url = ClassLoader.getSystemResource( path );
        if( url == null )
        {
            url = GuiUtils.class.getClassLoader().getSystemResource( path );
        }
        return new ImageIcon( url );
    }

    public static Dimension getMaxSize( JComponent[] components )
    {
        int width = 0;
        int height = 0;

        for( int i = 0; i < components.length; i++ )
        {
            Dimension size = components[i].getPreferredSize();
            width = Math.max( width, size.width );
            height = Math.max( height, size.height );
        }

        return new Dimension( width, height );
    }
    
    public static void setSize( Dimension size, JComponent[] components )
    {
        for( JComponent component : components )
        {
            component.setSize( size );
        }
    }
    
    public static void setSize( Dimension size, List<JComponent> components )
    {
        for( JComponent component : components )
        {
            component.setSize( size );
        }
    }

    public static KeyStroke getQuitKeyStroke()
    {
        if( SystemUtils.IS_MAC )
        {
            return KeyStroke.getKeyStroke( KeyEvent.VK_Q, SystemUtils.PREFERED_MODIFIER_KEY );
        }
        else
        {
            return KeyStroke.getKeyStroke( KeyEvent.VK_F4, SystemUtils.SECONDARY_MODIFIER_KEY );
        }
    }
    
    public static void forceRepaint( JComponent component )
    {
        component.invalidate();
        component.repaint();
        component.validate();
    }
    
    public static void forceRepaint( Container component )
    {
        component.invalidate();
        component.repaint();
        component.validate();
    }

    public static Dimension preferredSize( JComponent component )
    {
        Dimension size = component.getPreferredSize();
        component.setSize( size );
        return size;
    }

    public static Dimension preferredSize( JComponent component, int overrideWidth, int overrideHeight )
    {
        Dimension size = component.getPreferredSize();

        if( overrideWidth != -1 )
        {
            size.width = overrideWidth;
        }

        if( overrideHeight != -1 )
        {
            size.height = overrideHeight;
        }

        component.setSize( size );
        return size;
    }

    public static void preferredSize( List<JComponent> components )
    {
        for( JComponent component : components)
        {
            Dimension size = component.getPreferredSize();
            component.setSize( size );
        }
    }

    public static Dimension sameSize( List<JComponent> components )
    {
        JComponent[] array = new JComponent[ components.size() ];
        array = components.toArray( array );
        return sameSize( array );
    }

    public static Dimension sameSize( JComponent[] components )
    {
        int width = 0;
        int height = 0;

        for( int i = 0; i < components.length; i++ )
        {
            Dimension size = components[i].getPreferredSize();
            width = Math.max( width, size.width );
            height = Math.max( height, size.height );
        }

        Dimension result = new Dimension( width, height );

        for( int i = 0; i < components.length; i++ )
        {
            components[i].setSize( result );
        }

        return result;
    }

    public static void sameWidth( JComponent[] components, int width )
    {
        for( int i = 0; i < components.length; i++ )
        {
            Dimension size = components[i].getSize();
            size.width = width;
            components[i].setSize( size );
        }
    }

    public static void sameHeight( JComponent[] components, int height )
    {
        for( int i = 0; i < components.length; i++ )
        {
            Dimension size = components[i].getSize();
            size.height = height;
            components[i].setSize( size );
        }
    }
    
    public static void setWidthPercentageOfScreen( Window window, int widthPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = ( screenSize.width * widthPercentage ) / 100;
        int height = window.getHeight();
        window.setSize( width, height );
        center( window );
    }
    
    public static void setHeightPercentageOfScreen( Window window, int heightPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = window.getWidth();
        int height = ( screenSize.height * heightPercentage ) / 100;
        window.setSize( width, height );
        center( window );
    }
    
    public static void setPercentageOfScreen( Window window, int widthPercentage, int heightPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = ( screenSize.width * widthPercentage ) / 100;
        int height = ( screenSize.height * heightPercentage ) / 100;
        window.setSize( width, height );
        center( window );
    }

    public static void setPercentageOfScreen( Window window, double widthPercentage, double heightPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)( screenSize.width * widthPercentage );
        int height = (int)( screenSize.height * heightPercentage );
        window.setSize( width, height );
        center( window );
    }
    
    public static void setWidthPercentageOfScreen( Window window, double widthPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)( screenSize.width * widthPercentage );
        int height = window.getHeight();
        window.setSize( width, height );
        center( window );
    }
    
    public static void setHeightPercentageOfScreen( Window window, double heightPercentage )
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = window.getWidth();
        int height = (int)( screenSize.height * heightPercentage );
        window.setSize( width, height );
        center( window );
    }
    
    public static Action closeOnEscape( JDialog dialog )
    {
        return new CloseDialogAction( dialog );
    }
    
    public static Action closeOnEscape( JFrame frame )
    {
        return new CloseFrameAction( frame );
    }
    
    private static class CloseDialogAction
        extends AbstractAction
    {
        private JDialog dialog;
        
        public CloseDialogAction( JDialog dialog )
        {
            this.dialog = dialog;
            dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ESCAPE_KEYSTROKE, CLOSE_DIALOG_ACTION_MAP_KEY );
            dialog.getRootPane().getActionMap().put( CLOSE_DIALOG_ACTION_MAP_KEY, this  );
        }
        
        @Override
        public void actionPerformed( ActionEvent event )
        {
            dialog.dispatchEvent(new WindowEvent(  dialog, WindowEvent.WINDOW_CLOSING )); 
        }
    }
    
    private static class CloseFrameAction
        extends AbstractAction
    {
        private JFrame frame;
        
        public CloseFrameAction( JFrame frame )
        {
            this.frame = frame;
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( ESCAPE_KEYSTROKE, CLOSE_DIALOG_ACTION_MAP_KEY );
            frame.getRootPane().getActionMap().put( CLOSE_DIALOG_ACTION_MAP_KEY, this  );
        }
        
        @Override
        public void actionPerformed( ActionEvent event )
        {
            frame.dispatchEvent(new WindowEvent(  frame, WindowEvent.WINDOW_CLOSING )); 
        }
    }
}
