package galvin.swing;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ThrowableDialog
{
    private JTextArea textArea = new JTextArea();
    private ApplicationWindow window;
    private JScrollPane scrollPane;
    
    public ThrowableDialog( Throwable t )
    {
        StringBuilder content = new StringBuilder();
        fill( content, t );
        
        textArea.setText( content.toString().trim() );
        scrollPane = new JScrollPane( textArea );
        window = new ApplicationWindow( t.getMessage() );
        window.getContentPane().add(  scrollPane );
        window.setSize( 700, 350 );
        window.center();
    }
    
    public void setVisible( boolean visible )
    {
        window.setVisible( visible );
    }
    
    public void dispose()
    {
        window.dispose();
    }
    
    private static void fill( StringBuilder content, Throwable t )
    {
        content.append( t.getMessage() );
        content.append( "\n" );
        
        StackTraceElement[] elements  = t.getStackTrace();
        for( StackTraceElement element : elements )
        {
            content.append( element.getClassName() );
            content.append( " : " );
            content.append( element.getLineNumber() );
            content.append( " : " );
            content.append( element.getMethodName() );
            content.append( "\n" );
        }
        
        if( t.getCause() != null )
        {
            content.append( "Caused by:\n" );
            fill( content, t.getCause() );
        }
    }
}
