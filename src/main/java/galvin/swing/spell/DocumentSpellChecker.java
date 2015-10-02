package galvin.swing.spell;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import galvin.StringUtils;
import galvin.swing.text.DocumentUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class DocumentSpellChecker
    implements DocumentListener, SpellDictionaryListener
{

    public static final String PROPERTY_DOCUMENT_SPELL_CHECKER = "com.galvin.util.swing.spell.DocumentSpellChecker";
    private static final long UPDATE_SLEEP_TIME = 250;
    private final Object LOCK = new Object();
    private Document document;
    private long lastUpdate;
    private boolean updated;
    private boolean spellCheckRunning = true;
    private List<Misspelling> misspellings = new ArrayList();
    private List<MisspellingListener> listeners = new ArrayList();
    private SpellCheckThread updateThread;
    private SpellChecker spellChecker;
    private LocalSpellCheckListener spellCheckListener;
    private SpellDictionaryUser userDictionary;
    private SpellDictionaryUser projectDictionary;

    public DocumentSpellChecker( Document document ) throws IOException
    {
        this( document, SpellUtils.getAmericanDictionary(), SpellUtils.getCustomDictionary(), null );
    }

    public DocumentSpellChecker( Document document, SpellDictionary dictionary, SpellDictionaryUser userDictionary, SpellDictionaryUser projectDictionary )
    {
        this.document = document;
        this.userDictionary = userDictionary;
        this.projectDictionary = projectDictionary;
        this.spellChecker = new SpellChecker( dictionary );

        document.addDocumentListener( this );
        document.putProperty( PROPERTY_DOCUMENT_SPELL_CHECKER, this );

        if( userDictionary != null )
        {
            spellChecker.addDictionary( userDictionary );
            userDictionary.addListener( this );
        }

        if( projectDictionary != null )
        {
            spellChecker.addDictionary( projectDictionary );
            projectDictionary.addListener( this );
        }

        spellCheckListener = new LocalSpellCheckListener();
        spellChecker.addSpellCheckListener( spellCheckListener );
    }

    public void startSpellCheck()
    {
        spellCheckRunning = true;

        if( userDictionary != null )
        {
            userDictionary.addListener( this );
        }

        if( projectDictionary != null )
        {
            projectDictionary.addListener( this );
        }

        updated = true;
        lastUpdate = 0;
        updateThread = SpellCheckThreadFactory.getSpellCheckThread( this );
        updateThread.start();
    }

    public void stopSpellCheck()
    {
        spellCheckRunning = false;

        if( userDictionary != null )
        {
            userDictionary.removeListener( this );
        }

        if( projectDictionary != null )
        {
            projectDictionary.removeListener( this );
        }

        misspellings.clear();
        notifyListeners();
    }

    public boolean doSpellCheck()
    {
        synchronized( LOCK )
        {
            if( updated && System.currentTimeMillis() > lastUpdate + UPDATE_SLEEP_TIME )
            {
                misspellings.clear();
                String text = StringUtils.neverNull( DocumentUtils.getText( document ) );
                StringWordTokenizer tokenizer = new StringWordTokenizer( text );
                spellChecker.checkSpelling( tokenizer );
                
                updated = false;
                lastUpdate = System.currentTimeMillis();
                notifyListeners();
                
                return true;
            }

            return false;
        }
    }
    
    public List<Misspelling> getMisspellings()
    {
        List<Misspelling> result = new ArrayList();
        result.addAll( misspellings );
        return result;
    }

    public void addListener( MisspellingListener listener )
    {
        listeners.add( listener );
    }

    public void removeListener( MisspellingListener listener )
    {
        listeners.remove( listener );
    }

    public void notifyListeners()
    {
        for( int i = listeners.size() - 1; i >= 0; i-- )
        {
            listeners.get( i ).misspellings( document, getMisspellings() );
        }
    }

    public void dictionaryUpdated( SpellDictionary source )
    {
        recordChange( null );
    }

    private class LocalSpellCheckListener
        implements SpellCheckListener
    {

        public void spellingError( SpellCheckEvent spellCheckEvent )
        {
            misspellings.add( new Misspelling( document, spellCheckEvent ) );
        }
    }

    public void changedUpdate( DocumentEvent evt )
    {
        //for some reason, gaining focus is considered a change event.
        //since there really is no change, this little hack will
        //get around it.
        if( evt.toString().compareTo( "[]" ) != 0 )
        {
            recordChange( evt );
        }
    }

    public void insertUpdate( DocumentEvent evt )
    {
        recordChange( evt );
    }

    public void removeUpdate( DocumentEvent evt )
    {
        recordChange( evt );
    }

    public void recordChange( DocumentEvent evt )
    {
        synchronized( LOCK )
        {
            updated = true;
            lastUpdate = System.currentTimeMillis();

            if( spellCheckRunning )
            {
                if( updateThread == null || !updateThread.isActive() )
                {
                    updateThread = SpellCheckThreadFactory.getSpellCheckThread( this );
                    updateThread.start();
                }
            }
        }
    }
}
