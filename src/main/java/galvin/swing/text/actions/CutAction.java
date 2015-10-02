package galvin.swing.text.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;

public class CutAction
    extends AbstractAction
{

    private JTextComponent textComponent;

    public CutAction( JTextComponent textComponent )
    {
        this.textComponent = textComponent;
        putValue( "Name", "Cut" );
    }

    public void actionPerformed( ActionEvent e )
    {
        textComponent.cut();
    }
}
