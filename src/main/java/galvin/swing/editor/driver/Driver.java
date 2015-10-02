package galvin.swing.editor.driver;

import galvin.swing.ApplicationWindow;
import galvin.swing.editor.Editor;
import galvin.swing.spell.DocumentSpellChecker;
import galvin.swing.spell.SpellingHighlighter;
import galvin.swing.spell.SpellingPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.text.Document;


public class Driver
{
    public static void main( String[] args )
    {
        try
        {
            Editor editorPane = new Editor();
            Document document = editorPane.getDocument();
            
            DocumentSpellChecker listener = new DocumentSpellChecker( document );
            SpellingHighlighter spellingHighlighter = new SpellingHighlighter( editorPane, document );
            SpellingPopupMenu spellingPopupMenu = new SpellingPopupMenu( editorPane );
            editorPane.setSpellingHighlighter( spellingHighlighter );
            
            editorPane.setStylesAllowed( false );
            
            ApplicationWindow applicationWindow = new ApplicationWindow( "Spell check test" );
            applicationWindow.getContentPane().add( new JScrollPane( editorPane) );
            applicationWindow.setSize( 900, 700 );
            applicationWindow.center();
            applicationWindow.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}
