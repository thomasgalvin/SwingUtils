package galvin.swing.text.actions;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class GoogleWordAction
    extends AbstractAction
{

    private JTextComponent textComponent;
    private String word;

    public GoogleWordAction( JTextComponent textComponent, String word )
    {
        this.textComponent = textComponent;
        this.word = word;
        putValue( "Name", "Search for " + word + " in Google ..." );
    }

    public void actionPerformed( ActionEvent e )
    {
        try
        {
            Desktop.getDesktop().browse( new URI( buildLookupUrl( word ) ) );
        }
        catch( Throwable t )
        {
            Toolkit.getDefaultToolkit().beep();
            t.printStackTrace();
        }
    }

    public String buildLookupUrl( String word )
    {
        return "https://www.google.com/search?q=" + word.trim();
    }
}