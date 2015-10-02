package galvin.swing.spell;

import com.swabunga.spell.event.SpellCheckEvent;
import javax.swing.text.Document;


public class Misspelling
{
    private Document document;
    private SpellCheckEvent spellCheckEvent;

    public Misspelling( Document document, SpellCheckEvent spellCheckEvent )
    {
        this.document = document;
        this.spellCheckEvent = spellCheckEvent;
    }

    public Document getDocument()
    {
        return document;
    }

    public void setDocument( Document document )
    {
        this.document = document;
    }

    public SpellCheckEvent getSpellCheckEvent()
    {
        return spellCheckEvent;
    }

    public void setSpellCheckEvent( SpellCheckEvent spellCheckEvent )
    {
        this.spellCheckEvent = spellCheckEvent;
    }
    
}
