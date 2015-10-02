package galvin.swing.spell;

public class SpellCheckThread
    extends Thread
{

    private static final long SLEEP_TIME = 500;
    private DocumentSpellChecker documentSpellChecker;
    private boolean active = true;

    public SpellCheckThread( DocumentSpellChecker documentSpellChecker )
    {
        this.documentSpellChecker = documentSpellChecker;
    }

    @Override
    public void run()
    {
        while( true )
        {
            try
            {
                if( documentSpellChecker.doSpellCheck() )
                {
                    active = false;
                    return;
                }
            }
            catch( Throwable t )
            {
                //eat it
            }

            try
            {
                sleep( SLEEP_TIME );
            }
            catch( Throwable t )
            {
                //interrupt
            }
        }
    }

    public boolean isActive()
    {
        return active;
    }
}