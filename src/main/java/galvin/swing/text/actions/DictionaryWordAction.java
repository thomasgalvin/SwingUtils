package galvin.swing.text.actions;

import javax.swing.text.JTextComponent;


public class DictionaryWordAction
extends GoogleWordAction
{
    public DictionaryWordAction( JTextComponent textComponent, String word )
    {
        super( textComponent, word );
        putValue( "Name", "Look up " + word + " in Dictionary ..." );
    }
    
    @Override
    public String buildLookupUrl( String word )
    {
        return "http://dictionary.reference.com/browse/" + word;
    }
}