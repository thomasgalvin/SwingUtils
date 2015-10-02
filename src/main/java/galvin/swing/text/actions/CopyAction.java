package galvin.swing.text.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class CopyAction
    extends AbstractAction
{

    private JTextComponent textComponent;

    public CopyAction( JTextComponent textComponent )
    {
        this.textComponent = textComponent;
        putValue( "Name", "Copy" );
    }

    public void actionPerformed( ActionEvent e )
    {
        textComponent.copy();
    }
}
