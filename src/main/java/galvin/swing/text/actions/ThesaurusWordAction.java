package galvin.swing.text.actions;

import javax.swing.text.JTextComponent;


public class ThesaurusWordAction
extends GoogleWordAction
{
    public ThesaurusWordAction( JTextComponent textComponent, String word )
    {
        super( textComponent, word );
        putValue( "Name", "Look up " + word + " in Thesaurus ..." );
    }
    
    @Override
    public String buildLookupUrl( String word )
    {
        return "http://thesaurus.com/browse/" + word;
    }
}
