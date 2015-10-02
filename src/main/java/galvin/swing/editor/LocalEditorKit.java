package galvin.swing.editor;

import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class LocalEditorKit
        extends StyledEditorKit
{
    private static final ViewFactory parentViewFactory = new StyledEditorKit().getViewFactory();
//    private static final ViewFactory defaultFactory = new SyntaxViewFactory();
//    
//    public static final int PADDING = 10;
//    public static final int BEVEL_WIDTH = 2;
//    public static final String LINE_END_TOKEN = ".";
//    public static final String LINE_WRAP_TOKEN = ":";

    @Override
    public Object clone()
    {
        return new LocalEditorKit();
    }

    @Override
    public Document createDefaultDocument()
    {
        return new EditorDocument();
    }

//    @Override
//    public ViewFactory getViewFactory()
//    {
//        return defaultFactory;
//    }
//
//    static class SyntaxViewFactory
//            implements ViewFactory
//    {
//        public View create( Element element )
//        {
//            System.out.println( "create" );
//            String name = element.getName();
//            if( name != null )
//            {
//                if( name.equals( AbstractDocument.ParagraphElementName ) )
//                {
//                    return new SyntaxParagraphView( element );
//                }
//                else if( name.equals( AbstractDocument.ContentElementName ) )
//                {
//                    //return new SyntaxLabelView(element);
//                }
//            }
//
//            return parentViewFactory.create( element );
//        }
//    }
//    
//
//    public static int getGutterWidth( LineNumberAware lna, Graphics2D g2 )
//    {
//        int lineCount = lna.getLineCount();
//        lineCount = Math.max( lineCount, 10000 );
//
//        return LocalEditorKit.getGutterWidth( g2, lineCount + "" );
//    }
//
//    public static int getGutterWidth( Graphics2D g2, String text )
//    {
//        return getStringWidth( g2, text );
//    }
//
//    public static int getGutterHeight( Graphics2D g2, String text )
//    {
//        return getStringHeight( g2, text );
//    }
//
//    public static int getStringWidth( Graphics2D g2, String text )
//    {
//        FontRenderContext frc = g2.getFontRenderContext();
//        Rectangle2D bounds = g2.getFont().getStringBounds( text, frc );
//        return (int) bounds.getWidth();
//    }
//
//    public static int getStringHeight( Graphics2D g2, String text )
//    {
//        FontRenderContext frc = g2.getFontRenderContext();
//        Rectangle2D bounds = g2.getFont().getStringBounds( text, frc );
//        return (int) bounds.getHeight();
//    }
//
//    public static class SyntaxParagraphView
//            extends ParagraphView
//    {
//        protected Insets paragraphInsets;
//
//        public SyntaxParagraphView( Element element )
//        {
//            super( element );
//        }
//
//        @Override
//        public void paint( Graphics g, Shape shape )
//        {
//            super.paint( g, shape );
//
//            if( showLineNumbers() )
//            {
//                Graphics2D g2 = (Graphics2D) g;
//
//                Font font = g2.getFont();
//                Color color = g2.getColor();
//
//                Rectangle paragraphBounds = shape.getBounds();
//
//                int lineNumber = getLineNumber();
//                String lineNumberText = lineNumber + "";
//
//                int gutterLocation = LocalEditorKit.getGutterWidth( getTextControl(), g2 );
//                int x = gutterLocation - LocalEditorKit.getStringWidth( g2, lineNumberText ) - 5;
//                int y = paragraphBounds.y + getStringHeight( g2, lineNumberText ) / 2 + 5;
//
//                g2.setFont( StyleManager.getLineNumberFont() );
//                g2.setColor( StyleManager.getLineNumberColor() );
//                g2.drawString( lineNumberText, x, y );
//
//                g2.setFont( font );
//                g2.setColor( color );
//            }
//        }
//
//        public LineNumberAware getTextControl()
//        {
//            Container c = null;
//            do
//            {
//                c = getContainer();
//                if( c instanceof LineNumberAware )
//                {
//                    return (LineNumberAware) c;
//                }
//            }
//            while( c != null );
//
//            return null;
//        }
//
//        public float getGutterWidth( Graphics2D g2 )
//        {
//            LineNumberAware lna = getTextControl();
//            if( lna != null )
//            {
//                return LocalEditorKit.getGutterWidth( lna, g2 );
//            }
//
//            return 0;
//        }
//
//        @Override
//        public short getLeftInset()
//        {
//            if( showLineNumbers() )
//            {
//                Graphics2D g2 = (Graphics2D) getGraphics();
//                return (short) ( super.getLeftInset() + getGutterWidth( g2 ) );
//            }
//            else
//            {
//                return super.getLeftInset();
//            }
//        }
//
//        @Override
//        public short getRightInset()
//        {
//            return super.getRightInset();
//        }
//
//        public boolean showLineNumbers()
//        {
//            LineNumberAware lna = getTextControl();
//            if( lna != null )
//            {
//                return lna.showLineNumbers();
//            }
//
//            return false;
//        }
//
//        public int getLineNumber()
//        {
//            return getDocument().getRootElements()[0].getElementIndex( getElement().getStartOffset() ) + 1;
//        }
//
//        public int getLineCount()
//        {
//            LineNumberAware lna = getTextControl();
//            if( lna != null )
//            {
//                return lna.getLineCount();
//            }
//
//            return 0;
//        }
//    }

    public interface LineNumberAware
    {
        public int getLineCount();

        public int getGutterPadding();

        public boolean showLineNumbers();
    }
}
