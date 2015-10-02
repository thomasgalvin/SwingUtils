package galvin.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

public class CloseableDialog
extends JDialog
{
    public CloseableDialog()
    {
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        addWindowListener( new WindowAdapter()
        {

            @Override
            public void windowClosing( WindowEvent e )
            {
                closeDialog();
            }
        } );
    }
    
    public void closeDialog()
    {
        setVisible( false );
        dispose();
    }
}
