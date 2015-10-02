package galvin.swing.spell;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class MisspellingPainter
    implements Highlighter.HighlightPainter
{

    public MisspellingPainter()
    {
    }

    public void paint( Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c )
    {
        try
        {
            TextUI mapper = c.getUI();
            Rectangle p0 = mapper.modelToView( c, offs0 );
            Rectangle p1 = mapper.modelToView( c, offs1 );

            Graphics2D g2 = (Graphics2D) g;
            g.setColor( Color.RED );

            Rectangle r = p0.union( p1 );

            int startX = r.x;
            int startY = r.y + r.height - 1;
            int endX = r.x + r.width;
            int endY = startY;

            Line2D.Double line = new Line2D.Double( startX, startY, endX, endY );
            g2.draw( line );
        }
        catch( BadLocationException e )
        {
            // can't render
        }
    }
}
