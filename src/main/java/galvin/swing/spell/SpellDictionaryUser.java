package galvin.swing.spell;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SpellDictionaryUser
extends SpellDictionaryHashMap
{
    private List<SpellDictionaryListener> listeners = new ArrayList();
    
    public SpellDictionaryUser( File file ) throws FileNotFoundException, IOException
    {
        super( file );
    }
    
    public void addListener( SpellDictionaryListener listener )
    {
        //new Exception( "Adding listener" ).printStackTrace();
        //System.out.println( "adding listener" );
        listeners.add( listener );
    }
    
    public void removeListener( SpellDictionaryListener listener )
    {
        //System.out.println( "removing listener" );
        listeners.remove( listener );
    }

    @Override
    public void addWord( String string )
    {
        super.addWord( string );
        notifyListeners();
    }
    
    public void notifyListeners()
    {
        System.out.println( "notifying " + listeners.size() );
        for( int i = listeners.size()-1; i >= 0; i-- )
        {
            listeners.get( i ).dictionaryUpdated( this );
        }
    }
}
