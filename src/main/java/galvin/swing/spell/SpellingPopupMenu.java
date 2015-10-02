package galvin.swing.spell;


import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellCheckEvent;
import galvin.StringUtils;
import galvin.swing.text.TextControlUtils;
import galvin.swing.text.actions.CopyAction;
import galvin.swing.text.actions.PasteAction;
import galvin.swing.text.actions.CutAction;
import galvin.swing.text.actions.DictionaryWordAction;
import galvin.swing.text.actions.GoogleDefineWordAction;
import galvin.swing.text.actions.GoogleWordAction;
import galvin.swing.text.actions.ThesaurusWordAction;
import galvin.swing.text.actions.WikipediaWordAction;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class SpellingPopupMenu
    extends JPopupMenu
    implements PopupMenuListener
{

    public static final int MAX_MENU_ITEMS = 10;
    private JTextComponent textComponent;
    private JMenu moreMenu;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private SpellDictionary userDictionary;
    private SpellDictionary projectDictionary;

    public SpellingPopupMenu( JTextComponent textComponent )
    {
        this( textComponent, null, null );
    }

    public SpellingPopupMenu( JTextComponent textComponent, SpellDictionary userDictionary, SpellDictionary projectDictionary )
    {
        this.textComponent = textComponent;
        this.userDictionary = userDictionary;
        this.projectDictionary = projectDictionary;

        cutAction = new CutAction( textComponent );
        copyAction = new CopyAction( textComponent );
        pasteAction = new PasteAction( textComponent );

        textComponent.add( this );
        textComponent.setComponentPopupMenu( this );
        addPopupMenuListener( this );

        configure();
    }

    public void configure()
    {
        removeAll();

        addSpellingItems();
        addDefaultItems();
    }

    public void addDefaultItems()
    {
        if( getComponentCount() != 0 )
        {
            addSeparator();
        }

        int clickPosition = getClickPosition();
        if( clickPosition >= 0 )
        {
            String word = TextControlUtils.getCurrentWord( textComponent, clickPosition );
            if( !StringUtils.empty( word ) )
            {

                Action googleWordAction = new GoogleWordAction( textComponent, word );
                Action googleDefineWordAction = new GoogleDefineWordAction( textComponent, word );
                Action wikipediaWordAction = new WikipediaWordAction( textComponent, word );
                Action dictionaryWordAction = new DictionaryWordAction( textComponent, word );
                Action thesaurusWordAction = new ThesaurusWordAction( textComponent, word );

                add( googleWordAction );
                add( googleDefineWordAction );
                add( wikipediaWordAction );
                add( dictionaryWordAction );
                add( thesaurusWordAction );
                addSeparator();
            }
        }

        add( cutAction );
        add( copyAction );
        add( pasteAction );
    }

    public int getClickPosition()
    {
        try
        {
            Point textComponentLocation = textComponent.getLocationOnScreen();
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

            int caretX = mouseLocation.x - textComponentLocation.x;
            int caretY = mouseLocation.y - textComponentLocation.y;
            Point caretLocationOnScreen = new Point( caretX, caretY );
            int caretPosition = textComponent.viewToModel( caretLocationOnScreen );
            return caretPosition;
        }
        catch( IllegalComponentStateException ex )
        {
            //eat it; this only happens if the text editor is not yet on screen
        }

        return -1;
    }

    public void addSpellingItems()
    {
        moreMenu = null;

        if( textComponent != null )
        {
            Document document = textComponent.getDocument();
            if( document != null )
            {
                DocumentSpellChecker documentSpellChecker = (DocumentSpellChecker) document.getProperty( DocumentSpellChecker.PROPERTY_DOCUMENT_SPELL_CHECKER );
                if( documentSpellChecker != null )
                {
                    int caretPosition = getClickPosition();
                    if( caretPosition >= 0 )
                    {
//                        System.out.println( "caret position: " + caretPosition );

                        Misspelling misspelling = null;
                        SpellCheckEvent spellCheckEvent = null;

                        List<Misspelling> misspellings = documentSpellChecker.getMisspellings();
                        for( Misspelling testMisspelling : misspellings )
                        {
                            spellCheckEvent = testMisspelling.getSpellCheckEvent();
                            int start = spellCheckEvent.getWordContextPosition();
                            int end = start + spellCheckEvent.getInvalidWord().length();
//                            System.out.println( "    start: " + start );
//                            System.out.println( "    end: " + end );

                            if( caretPosition >= start && caretPosition <= end )
                            {
                                misspelling = testMisspelling;
//                                System.out.println( "    match" );
                                break;
                            }
//                            else
//                            {
//                                System.out.println( "    no match" );
//                            }
//                            System.out.println( "\n" );
                        }

                        if( misspelling != null )
                        {
                            List suggestions = spellCheckEvent.getSuggestions();
                            if( !suggestions.isEmpty() )
                            {
                                buildSugestionsMenu( suggestions, caretPosition, spellCheckEvent );
                            }
                            else
                            {
                                add( getNoSuggestionsItem() );
                            }

                            if( userDictionary != null || projectDictionary != null )
                            {
                                addSeparator();

                                if( userDictionary != null )
                                {
                                    add( new AddWordToUserDictionaryAction( spellCheckEvent.getInvalidWord() ) );
                                }

                                if( projectDictionary != null )
                                {
                                    add( new AddWordToProjectDictionaryAction( spellCheckEvent.getInvalidWord() ) );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void buildSugestionsMenu( List suggestions, int caretPosition, SpellCheckEvent spellCheckEvent )
    {
        for( Object suggestion : suggestions )
        {
            ReplaceWordAction action = new ReplaceWordAction( caretPosition, spellCheckEvent.getInvalidWord(), suggestion.toString() );

            if( moreMenu == null )
            {
                if( getComponentCount() > MAX_MENU_ITEMS )
                {
                    moreMenu = getMoreSuggestionsMenu();
                    add( moreMenu );
                    moreMenu.add( action );
                }
                else
                {
                    add( action );
                }
            }
            else
            {
                if( moreMenu.getComponentCount() > MAX_MENU_ITEMS )
                {
                    JMenu newMoreMenu = getMoreSuggestionsMenu();
                    moreMenu.add( newMoreMenu );
                    moreMenu = newMoreMenu;
                    moreMenu.add( action );
                }
                else
                {
                    moreMenu.add( action );
                }
            }
        }
    }

    public JMenuItem getNoSuggestionsItem()
    {
        JMenuItem result = new JMenuItem( "No suggestions found" );
        result.setEnabled( false );
        return result;
    }

    public JMenu getMoreSuggestionsMenu()
    {
        JMenu result = new JMenu( "More" );
        return result;
    }

    public void popupMenuWillBecomeVisible( PopupMenuEvent e )
    {
        configure();
    }

    public void popupMenuWillBecomeInvisible( PopupMenuEvent e )
    {
    }

    public void popupMenuCanceled( PopupMenuEvent e )
    {
    }

    public SpellDictionary getUserDictionary()
    {
        return userDictionary;
    }

    public void setUserDictionary( SpellDictionary userDictionary )
    {
        this.userDictionary = userDictionary;
    }

    public SpellDictionary getProjectDictionary()
    {
        return projectDictionary;
    }

    public void setProjectDictionary( SpellDictionary projectDictionary )
    {
        this.projectDictionary = projectDictionary;
    }

    private class AddWordToDictionaryThread
    extends Thread
    {
        private SpellDictionary dictionary;
        private String word;

        public AddWordToDictionaryThread( SpellDictionary dictionary, String word )
        {
            this.dictionary = dictionary;
            this.word = word;
        }
        
        @Override
        public void run()
        {
            if( !dictionary.isCorrect( word ) )
            {
                dictionary.addWord( word );
            }
        }
    }

    private class ReplaceWordAction
        extends AbstractAction
    {

        int caretPosition;
        String badWord;
        String goodWord;

        public ReplaceWordAction( int caretPosition, String badWord, String goodWord )
        {
            this.caretPosition = caretPosition;
            this.badWord = badWord;
            this.goodWord = goodWord;

            putValue( "Name", goodWord );
        }

        public void actionPerformed( ActionEvent e )
        {
            TextControlUtils.spellCheckReplace( textComponent, badWord, goodWord, caretPosition );
        }
    }

    private class AddWordToUserDictionaryAction
        extends AbstractAction
    {

        String word;

        public AddWordToUserDictionaryAction( String word )
        {
            this.word = word;
            putValue( "Name", "Add " + word + " to dictionary" );
        }

        public void actionPerformed( ActionEvent e )
        {
            System.out.println( "Adding " + word + " to user dictionary" );
            new AddWordToDictionaryThread( userDictionary, word ).start();
        }
    }
    
    private class AddWordToProjectDictionaryAction
        extends AbstractAction
    {

        String word;

        public AddWordToProjectDictionaryAction( String word )
        {
            this.word = word;
            putValue( "Name", "Add " + word + " to project dictionary" );
        }

        public void actionPerformed( ActionEvent e )
        {
            System.out.println( "Adding " + word + " to project dictionary" );
            new AddWordToDictionaryThread( projectDictionary, word ).start();
        }
    }
}
