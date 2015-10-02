package galvin.swing.spell;

import com.swabunga.spell.event.SpellCheckEvent;
import java.util.Iterator;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;

public class SpellingHighlighter
    implements MisspellingListener
{

    private JTextComponent textComponent;
    private Document document;
    private MisspellingPainter highlightPainter = new MisspellingPainter();

    public SpellingHighlighter()
    {
    }

    public SpellingHighlighter( JTextComponent textComponent )
    {
        this( textComponent, textComponent.getDocument() );
    }

    public SpellingHighlighter( JTextComponent textComponent, Document document )
    {
        setTextComponent( textComponent );
    }

    public JTextComponent getTextComponent()
    {
        return textComponent;
    }

    public void setTextComponent( JTextComponent textComponent )
    {
        this.textComponent = textComponent;
        setDocument( textComponent.getDocument() );
    }

    public Document getDocument()
    {
        return document;
    }

    public void setDocument( Document document )
    {
        if( this.document == document )
        {
            return;
        }

        if( this.document != null )
        {
            Object tmp = this.document.getProperty( DocumentSpellChecker.PROPERTY_DOCUMENT_SPELL_CHECKER );
            if( tmp != null && tmp instanceof DocumentSpellChecker )
            {
                DocumentSpellChecker documentSpellChecker = (DocumentSpellChecker) tmp;
                documentSpellChecker.removeListener( this );
            }
        }

        this.document = document;

        Object tmp = document.getProperty( DocumentSpellChecker.PROPERTY_DOCUMENT_SPELL_CHECKER );
        if( tmp != null && tmp instanceof DocumentSpellChecker )
        {
            DocumentSpellChecker documentSpellChecker = (DocumentSpellChecker) tmp;
            documentSpellChecker.addListener( this );
            documentSpellChecker.notifyListeners();
        }
        else
        {
            try
            {
                DocumentSpellChecker documentSpellChecker = new DocumentSpellChecker( document );
                document.putProperty( DocumentSpellChecker.PROPERTY_DOCUMENT_SPELL_CHECKER, documentSpellChecker );
                documentSpellChecker.addListener( this );
                documentSpellChecker.notifyListeners();
            }
            catch( Throwable t )
            {
                t.printStackTrace();
            }
        }
    }

    public void refreshDocument()
    {
        setDocument( textComponent.getDocument() );
    }

    public void misspellings( Document document, List<Misspelling> misspellings )
    {
        Highlight[] highlights = textComponent.getHighlighter().getHighlights();
        for( Highlight highlight : highlights )
        {
            if( highlight.getPainter() instanceof MisspellingPainter )
            {
                boolean found = false;

                Iterator<Misspelling> misspellingsIter = misspellings.iterator();
                while( misspellingsIter.hasNext() )
                {
                    Misspelling misspelling = misspellingsIter.next();
                    SpellCheckEvent spellCheckEvent = misspelling.getSpellCheckEvent();
                    int start = spellCheckEvent.getWordContextPosition();
                    int end = start + spellCheckEvent.getInvalidWord().length();

                    if( highlight.getStartOffset() == start && highlight.getEndOffset() == end )
                    {
                        found = true;
                        misspellingsIter.remove();
                    }
                }

                if( !found )
                {
                    textComponent.getHighlighter().removeHighlight( highlight );
                }
            }
        }

        for( Misspelling misspelling : misspellings )
        {
            SpellCheckEvent spellCheckEvent = misspelling.getSpellCheckEvent();

            try
            {
                int start = spellCheckEvent.getWordContextPosition();
                int end = start + spellCheckEvent.getInvalidWord().length();
                textComponent.getHighlighter().addHighlight( start, end, highlightPainter );
            }
            catch( BadLocationException ble )
            {
                ble.printStackTrace();
            }
//            System.out.println( "Spelling error: " + spellCheckEvent.getInvalidWord() + " at " + spellCheckEvent.getWordContextPosition() );
//            System.out.println( "    Suggestons:" );
//            List suggestions = spellCheckEvent.getSuggestions();
//
//            if( suggestions.isEmpty() )
//            {
//                System.out.println( "    No suggestons found" );
//            }
//            else
//            {
//                for( Object suggestion : suggestions )
//                {
//                    System.out.println( "    " + suggestion );
//                }
//            }
//            System.out.println( "" );
        }
    }
}
