package galvin.swing.text.caret;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class OverwriteCaret
    extends DefaultCaret
{
    protected static final int MIN_WIDTH = 8;

    /**
    Causes a text component to repaint itself.
     */
    @Override
    protected synchronized void damage( Rectangle r )
    {
        if( r != null )
        {
            Rectangle r2 = new Rectangle( 0, 0, 0, 0 );

            JTextComponent comp = getComponent();
            TextUI mapper = comp.getUI();
            try
            {
                r2 = mapper.modelToView( comp, getDot() + 1 );
            }
            catch( BadLocationException e )
            {
                r2 = r;
            }
            int width = r2.x - r.x;
            if( width == 0 )
            {
                width = MIN_WIDTH;
            }
            comp.repaint( r.x, r.y, width, r.height );

            this.x = r.x;
            this.y = r.y;
            this.width = width;
            this.height = r.height;
        }
    }

    /**
    Draws the cursor on a text component.
     */
    @Override
    public void paint( Graphics g )
    {
        if( isVisible() )
        {
            Rectangle r1 = new Rectangle( 0, 0, 0, 0 );
            Rectangle r2 = new Rectangle( 0, 0, 0, 0 );

            JTextComponent comp = getComponent();
            TextUI mapper = comp.getUI();
            try
            {
                r1 = mapper.modelToView( comp, getDot() );
            }
            catch( BadLocationException ble )
            {
            }
            try
            {
                r2 = mapper.modelToView( comp, getDot() + 1 );
            }
            catch( BadLocationException ble )
            {
                r2 = r1;
            }

            Graphics2D g2 = (Graphics2D) ( g.create() );
            g2.setColor( comp.getForeground() );
            g.setXORMode( comp.getBackground() );
            int width = r2.x - r1.x;
            if( width == 0 )
            {
                width = MIN_WIDTH;
            }
            g.fillRect( r1.x, r1.y, width, r1.height );
            //g.fillRect(r1.x, r1.height, width, 1);
            g.dispose();
        }
    }
}