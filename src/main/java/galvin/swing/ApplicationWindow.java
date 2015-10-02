/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import galvin.swing.mac.MacUtils;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class ApplicationWindow
extends JFrame
{
    protected boolean exitOnClose = true;
    static
    {
        MacUtils.useMacScreenMenuBar();
    }

    public ApplicationWindow()
    {
        this( "Application Window" );
    }

    public ApplicationWindow( String title )
    {
        super( title );
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        createUI();
    }

    public void maximize()
    {
        setExtendedState( getExtendedState() | MAXIMIZED_BOTH );
    }

    public void fullScreen()
    {
        setExtendedState( Frame.MAXIMIZED_BOTH );
        center();
    }

    public void center()
    {
        GuiUtils.center( this );
    }

    public void setPercentageOfScreen( int widthPercentage, int heightPercentage )
    {
        GuiUtils.setPercentageOfScreen( this, widthPercentage, heightPercentage );
    }
    
    public void setWidthPercentageOfScreen( int percentage )
    {
        GuiUtils.setWidthPercentageOfScreen( this, percentage );
    }
    
    public void setHeightPercentageOfScreen( int percentage )
    {
        GuiUtils.setHeightPercentageOfScreen( this, percentage );
    }

    public void setPercentageOfScreen( double widthPercentage, double heightPercentage )
    {
        GuiUtils.setPercentageOfScreen( this, widthPercentage, heightPercentage );
    }
    
    public void setWidthPercentageOfScreen( double percentage )
    {
        GuiUtils.setWidthPercentageOfScreen( this, percentage );
    }
    
    public void setHeightPercentageOfScreen( double percentage )
    {
        GuiUtils.setHeightPercentageOfScreen( this, percentage );
    }

    public boolean placeOnSecondaryScreen()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        GraphicsDevice secondaryScreen = null;

        GraphicsDevice[] graphicsDevices = ge.getScreenDevices();
        for( int i = 0; i < graphicsDevices.length; i++ )
        {
            if( graphicsDevices[i] != defaultScreen
                && graphicsDevices[i].getType()
                   == GraphicsDevice.TYPE_RASTER_SCREEN )
            {
                secondaryScreen = graphicsDevices[i];
                break;
            }
        }

        if( secondaryScreen != null )
        {
            GraphicsConfiguration configuration = secondaryScreen.getDefaultConfiguration();
            Rectangle bounds = configuration.getBounds();
            setLocation( bounds.x, bounds.y );
            return true;
        }

        return false;
    }

    public boolean placeOnPrimaryScreen()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();

        if( defaultScreen != null )
        {
            GraphicsConfiguration configuration = defaultScreen.getDefaultConfiguration();
            Rectangle bounds = configuration.getBounds();
            setLocation( bounds.x, bounds.y );
            return true;
        }

        return false;
    }

    public void reset()
    {
        setPercentageOfScreen( 75, 75);
        placeOnPrimaryScreen();
        center();
    }

    public void closeApplicationWindow()
    {
        setVisible( false );
        if( exitOnClose )
        {
            try
            {
                dispose();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
            finally
            {
                System.exit( 0 );
            }
        }
    }

    public void handleQuit()
            throws IllegalStateException
    {
        System.out.println( "handleQuit" );
        closeApplicationWindow();
        throw new IllegalStateException();
    }

    protected void createUI()
    {
        getContentPane().setLayout( new BorderLayout() );
        setPercentageOfScreen( 95, 75 );
        center();

        addWindowListener( new WindowAdapter()
        {

            @Override
            public void windowClosing( WindowEvent e )
            {
                closeApplicationWindow();
            }
        } );
    }

    public void setExitOnClose( boolean exitOnClose )
    {
        this.exitOnClose = exitOnClose;
    }

    public boolean getExitOnClose()
    {
        return exitOnClose;
    }
}
