package galvin.swing.text.actions;

import javax.swing.text.JTextComponent;


public class GoogleDefineWordAction
extends GoogleWordAction
{
    public GoogleDefineWordAction( JTextComponent textComponent, String word )
    {
        super( textComponent, word );
        putValue( "Name", "Define " + word + " in Google ..." );
    }
    
    @Override
    public String buildLookupUrl( String word )
    {
        return "https://www.google.com/search?q=define:" + word;
    }
}