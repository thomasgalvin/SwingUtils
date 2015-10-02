package galvin.swing.text.actions;

import javax.swing.text.JTextComponent;


public class WikipediaWordAction
extends GoogleWordAction
{
    public WikipediaWordAction( JTextComponent textComponent, String word )
    {
        super( textComponent, word );
        putValue( "Name", "Look up " + word + " in Wikipedia ..." );
    }
    
    @Override
    public String buildLookupUrl( String word )
    {
        return "http://en.wikipedia.org/wiki/" + word;
    }
}
