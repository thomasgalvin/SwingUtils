package galvin.swing.text.macros;

import java.util.Comparator;

public class MacroComparator
        implements Comparator
{

    public int compare( Object o1, Object o2 )
    {
        if( o1 instanceof Macro && o2 instanceof Macro )
        {
            Macro one = ( Macro ) o1;
            Macro two = ( Macro ) o2;

            try
            {
                return one.getAddbreviation().compareToIgnoreCase( two.getAddbreviation() );
            }
            catch( Throwable t )
            {
                t.printStackTrace();
            }
        }

        return 0;
    }
}
