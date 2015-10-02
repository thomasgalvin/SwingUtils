package galvin.swing.text.caret;

import java.awt.Graphics;
import javax.swing.text.DefaultCaret;

public class InvisibleCaret
    extends DefaultCaret
{
    @Override
    public void paint( Graphics g )
    {}
}