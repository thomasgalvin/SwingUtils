package galvin.swing.spell;


public class SpellCheckThreadFactory
{
    public static SpellCheckThread getSpellCheckThread( DocumentSpellChecker documentSpellChecker )
    {
        return new SpellCheckThread( documentSpellChecker );
    }
}
