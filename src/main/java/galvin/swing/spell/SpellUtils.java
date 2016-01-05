package galvin.swing.spell;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import galvin.SystemUtils;
import galvin.swing.editor.Editor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellUtils
{
    private static final Logger logger = LoggerFactory.getLogger( SpellUtils.class );
    
    public static final String BASE_DICTIONARY = "spell/eng_com.dic";
    public static final String[] AMERICA_ENGLISH_DICTIONARIES = new String[]
    {
        BASE_DICTIONARY, "spell/color.dic", "spell/labeled.dic", "spell/center.dic", "spell/ize.dic", "spell/yze.dic", "spell/aspell.dic"
    };
    public static final String[] BRITTISH_ENGLISH_DICTIONARIES = new String[]
    {
        BASE_DICTIONARY, "spell/colour.dic", "spell/labelled.dic", "spell/centre.dic", "spell/ise.dic", "spell/yse.dic"
    };
    public static final File[] UNIX_SYSTEM_DICTIONARIES = new File[]{ 
        new File( "/usr/share/dict/words" ),
        new File( "/usr/dict/words" ),
    };
    
    public static final String CUSTOM_DICTIONARY_FOLDER = "Dictionaries";
    public static final String CUSTOM_DICTIONARY_FILE = "customJazzyDictionary.dic";
    private static SpellDictionary americanDictionary;
    private static SpellDictionary brittishDictionary;
    private static SpellDictionaryUser customDictionary;

    public static SpellDictionary getAmericanDictionary()
        throws IOException
    {
        if( americanDictionary == null )
        {
            americanDictionary = loadDictionary( AMERICA_ENGLISH_DICTIONARIES );
        }

        return americanDictionary;
    }

    public static SpellDictionary getBrittishDictionary()
        throws IOException
    {
        if( brittishDictionary == null )
        {
            brittishDictionary = loadDictionary( BRITTISH_ENGLISH_DICTIONARIES );
        }

        return brittishDictionary;
    }

    public static SpellDictionary loadDictionary( String[] dictionaryNames )
        throws IOException
    {
        SpellDictionaryHashMap result = new SpellDictionaryHashMap();

        for( String dictionaryName : dictionaryNames )
        {
            ClassLoader classloader = result.getClass().getClassLoader();
            InputStream stream = classloader.getResourceAsStream( dictionaryName );
            if( stream != null ){
                InputStreamReader reader = new InputStreamReader( stream );
                result.addDictionary( reader );
                reader.close();
            } else {
                System.out.println( "Spell Utils: could not read: " + dictionaryName );
            }
        }
        
        for( File dict : UNIX_SYSTEM_DICTIONARIES ){
            if( dict.exists() && dict.canRead() )
            {
                result.addDictionary( dict );
            }
        }

        return result;
    }

    public static SpellDictionaryUser loadDictionary( File dictionaryFile )
        throws IOException
    {
        SpellDictionaryUser result = new SpellDictionaryUser( dictionaryFile );
        return result;
    }

    public static File getDictionariesDirectory()
        throws IOException
    {
        String fileName = System.getProperty( "user.home" );
        fileName += File.separatorChar;

        if( SystemUtils.IS_MAC )
        {
            fileName += "Library" + File.separatorChar + CUSTOM_DICTIONARY_FOLDER + File.separatorChar;
        }
        else if( SystemUtils.IS_WINDOWS )
        {
            fileName += "Application Data" + File.separatorChar + CUSTOM_DICTIONARY_FOLDER + File.separatorChar;
        }
        else
        {
            //assuming a unix variant
            fileName += "." + CUSTOM_DICTIONARY_FOLDER + File.separatorChar;
        }

        File file = new File( fileName );
        file.mkdirs();
        return file;
    }

    public static SpellDictionaryUser getCustomDictionary()
        throws IOException
    {
        if( customDictionary == null )
        {
            customDictionary = loadDictionary( getCustomDictionaryFile() );
        }

        return customDictionary;
    }
    
    public static File getCustomDictionaryFile()
        throws IOException
    {

        File file = new File( getDictionariesDirectory(), CUSTOM_DICTIONARY_FILE );
        if( !file.exists() )
        {
            file.createNewFile();
        }
        return file;
    }
    
    public static void setUpSpelling( JTextComponent text,
                                      SpellDictionaryUser projectDict ){
        try {
            new SpellingPopupMenu( text, 
                                   SpellUtils.getCustomDictionary(), 
                                   projectDict );

            SpellingHighlighter highlighter = new SpellingHighlighter( text, projectDict );
            if( text instanceof Editor ){
                Editor editor = (Editor)text;
                editor.setSpellingHighlighter( highlighter );
            }
        }
        catch( IOException ioe ){
            logger.error( "Error loading spell utils", ioe );
        }
    }
}