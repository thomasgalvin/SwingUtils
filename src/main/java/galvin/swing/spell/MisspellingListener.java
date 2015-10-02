package galvin.swing.spell;

import java.util.List;
import javax.swing.text.Document;


public interface MisspellingListener
{
    public void misspellings( Document document, List<Misspelling> misspellings );
}
