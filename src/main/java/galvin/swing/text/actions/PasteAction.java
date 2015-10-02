package galvin.swing.text.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class PasteAction
    extends AbstractAction
{

    private JTextComponent textComponent;

    public PasteAction( JTextComponent textComponent )
    {
        this.textComponent = textComponent;
        putValue( "Name", "Paste" );
    }

    public void actionPerformed( ActionEvent e )
    {
        textComponent.paste();
    }
}