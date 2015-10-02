package galvin.swing.spell.test;

import galvin.swing.ApplicationWindow;
import galvin.swing.spell.DocumentSpellChecker;
import galvin.swing.spell.SpellUtils;
import galvin.swing.spell.SpellingHighlighter;
import galvin.swing.spell.SpellingPopupMenu;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;

public class Driver
{

    public static void main( String[] args )
    {
        try
        {
            System.out.println( "1" );
            JEditorPane editorPane = new JEditorPane();
            System.out.println( "2" );
            Document document = editorPane.getDocument();
            System.out.println( "3" );
            DocumentSpellChecker listener = new DocumentSpellChecker( document );
            System.out.println( "4" );
            SpellingHighlighter spellingHighlighter = new SpellingHighlighter( editorPane, document );
            System.out.println( "5" );
            SpellingPopupMenu spellingPopupMenu = new SpellingPopupMenu( editorPane, SpellUtils.getCustomDictionary(), null );
            System.out.println( "6" );
            
//            Desktop.getDesktop().browse( SpellUtils.getCustomDictionaryFile().getParentFile().toURI() );
            System.out.println( "7" );
            
//            String text = 
//                "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n"
//                + "This iz a mispelled word.\n"
//                + "And thiz is alzo mizpelled.\n\n";
//            editorPane.setText( text );
            
            String text = 
                "I held the knife in both hands, trying to ward the vampire away, but Randal advanced without care or concern. I slashed the air, desperately attacking him, but the vampire slapped the blade out of my hand. He grabbed my hair, yanked my head back, and bared his fangs.\n" +
                "\n" +
                "The kitchen window exploded, showering us in glass. A dark shape cut through the air. I screamed and dove away, covering my face, but the thing wasn't coming after me; it was after *Randal.* It hit him hard enough to lift him off his feet and *through* the kitchen wall, leaving a gaping hole in the drywall with studs sticking out like splintered ribsz\n\n";
            text += text;
            text += text;
            text += text;
            text += text;
            text += text;
            editorPane.setText( text );
            
            ApplicationWindow applicationWindow = new ApplicationWindow( "Spell check test" );
            System.out.println( "8" );
            applicationWindow.getContentPane().add( new JScrollPane( editorPane) );
            System.out.println( "9" );
            applicationWindow.setSize( 900, 700 );
            System.out.println( "10" );
            applicationWindow.center();
            System.out.println( "11" );
            applicationWindow.setVisible( true );
            System.out.println( "12" );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}
