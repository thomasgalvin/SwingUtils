/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import galvin.dc.LanguageCode;
import java.awt.BorderLayout;
import javax.swing.JComboBox;

public class LanguageSelector
extends JComboBox
{
    private static LanguageCode[] codes;
    static
    {
        try
        {
            codes = LanguageCode.loadLanguageCodeArray();
        }
        catch( Throwable t )
        {
            codes = new LanguageCode[]
            {
                LanguageCode.ENGLISH,
                LanguageCode.ENGLISH_US,
                LanguageCode.ENGLISH_UK,
                LanguageCode.FRENCH,
                LanguageCode.SPANISH,
            };
            t.printStackTrace();
        }
    }
    
    public LanguageSelector()
    {
        super( codes );
        setSelectedItem( LanguageCode.ENGLISH_US );
    }
    
    @Override
    public LanguageCode getSelectedItem()
    {
        return (LanguageCode)super.getSelectedItem();
    }
    
    public void setSelectedItem( LanguageCode code )
    {
        if( code != null )
        {
            super.setSelectedItem( code );
        }
        else
        {
            super.setSelectedItem( LanguageCode.ENGLISH_US );
        }
    }
    
    public static void main( String[] args )
    {
        try
        {
            LanguageSelector selector = new LanguageSelector();
            ApplicationWindow window = new ApplicationWindow( "Languages" );
            window.getContentPane().setLayout( new BorderLayout() );
            window.getContentPane().add( selector, BorderLayout.CENTER );
            window.pack();
            window.center();
            window.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}
