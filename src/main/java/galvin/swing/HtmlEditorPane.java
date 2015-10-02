/**
Copyright &copy; 2007 Thomas Galvin.
 */
package galvin.swing;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.*;
import java.awt.Font;
import javax.swing.event.HyperlinkListener;

public class HtmlEditorPane
        extends JTextPane
        implements HyperlinkListener
{

    public HtmlEditorPane()
    {
        setEditable( false );
        setEditorKit( createEditorKit() );

        Font font = UIManager.getFont( "Label.font" );
        String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
        ( ( HTMLDocument ) getDocument() ).getStyleSheet().addRule( bodyRule );

        addHyperlinkListener( this );
    }

    public void hyperlinkUpdate( HyperlinkEvent evt )
    {
    }

    public static HTMLEditorKit createEditorKit()
    {
        return new HTMLEditorKit()
        {

            public javax.swing.text.Document createDefaultDocument()
            {
                HTMLDocument doc = ( HTMLDocument ) super.createDefaultDocument();
                doc.setAsynchronousLoadPriority( -1 );
                return doc;
            }
        };
    }
}