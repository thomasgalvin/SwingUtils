package galvin.swing.editor;

import java.awt.Graphics;
import javax.swing.text.DefaultHighlighter;

public class EditorHighlighter
    extends DefaultHighlighter
{

    @Override
    public void paint( Graphics g )
    {
        try
        {
            super.paint( g );
        }
        catch( Throwable t )
        {
            //System.out.println( "om nom nom" );
            //om nom nom
        }
    }
}
