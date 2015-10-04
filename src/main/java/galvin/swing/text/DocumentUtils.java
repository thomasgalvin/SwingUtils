/**
Copyright &copy 2011 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.text;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;

public class DocumentUtils
{

    protected static StyleContext styleContext = new StyleContext();
    protected static Style jvmDefaultStyle = styleContext.getStyle( "default" );
    protected static String textEditorStyleName = "Text Editor Default";
    protected static Style textEditorStyle = null;
    protected static String codeEditorStyleName = "Code Editor Default";
    protected static Style codeEditorStyle = null;

    /////////////////////
    // Search and Replace
    /////////////////////

    public static String getText( Document document )
    {
        try
        {
            return document.getText( 0, document.getLength() );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
            return "";
        }
    }
    
    public static void setText( Document document, StringBuilder text )
    {
        setText( document, text.toString() );
    }
    
    public static void setText( Document document, String text )
    {
        try
        {
            document.remove( 0, document.getLength() );
            document.insertString( 0, text, null );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
    
    public static void appendText( Document document, String text )
    {
        try
        {
            document.insertString( document.getLength(), text, null );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
    
    public static void copyText( Document sourceDocument, Document targetDocument )
    {
        String text = getText( sourceDocument );
        setText( targetDocument, text );
    }

    //////////////////////////
    // Line count, length, etc
    //////////////////////////
    public static int getLineCount( Document document )
    {
        return getLineNumber( document, document.getLength() );
    }

    public static int getLineNumber( Document document, int pos )
    {
        Element[] roots = document.getRootElements();
        Element root = roots[0];
        return root.getElementIndex( pos ) + 1;
    }

    public static int getCaretLine( Document document, int caretPosition )
    {
        return getLineNumber( document, caretPosition );
    }

    /**
    Returns the beginning offset for the given line, or -1 if there is an error.
     */
    public static int getLineStartOffset( Document document, int line )
    {
        Element lineElement = getElementForLine( document, line );
        if( lineElement == null )
        {
            return -1;
        }
        else
        {
            return lineElement.getStartOffset();
        }
    }

    /**
    Returns the offset of first non-whitespace character on the specified line.
     */
    public static int getLineStartOffsetNoWhitespace( Document document, int line )
    {
        Element lineElement = getElementForLine( document, line );
        if( lineElement == null )
        {
            return -1;
        }
        else
        {
            int start = lineElement.getStartOffset();
            String lineText = getLineText( document, line );

            int length = lineText.length();
            int i = 0;
            while( i < length && Character.isWhitespace( lineText.charAt( i ) ) )
            {
                i++;
                start++;
            }

            return start;
        }
    }

    public static int getLineEndOffset( Document document, int line )
    {
        Element lineElement = getElementForLine( document, line );
        if( lineElement == null )
        {
            return -1;
        }
        else
        {
            int result = lineElement.getEndOffset();
            if( result > document.getLength() ){
                result = document.getLength();
            }
            return result;
        }
    }

    /**
    Returns the offset of last non-whitespace character on the specified line.
     */
    public static int getLineEndOffsetNoWhitespace( Document document, int line )
    {
        Element lineElement = getElementForLine( document, line );
        if( lineElement == null )
        {
            return -1;
        }
        else
        {
            int realEnd = lineElement.getEndOffset() - 1;
            int end = realEnd;
            String lineText = getLineText( document, line );

            int length = lineText.length();
            int i = length - 1;
            while( i >= 0 && Character.isWhitespace( lineText.charAt( i ) ) )
            {
                i--;
                end--;
            }

            if( end != realEnd )
            {
                end++;
            }

            return end;
        }
    }

    public static int getLineLength( Document document, int line )
    {
        Element lineElement = getElementForLine( document, line );
        if( lineElement == null )
        {
            return -1;
        }
        else
        {
            return lineElement.getEndOffset() - lineElement.getStartOffset() - 1;
        }
    }

    public static String getLineText( Document document, int line )
    {
        int lineStart = getLineStartOffset( document, line );
        int lineEnd = getLineEndOffset( document, line );

        try
        {
            if( lineStart != -1 && lineEnd != -1 )
            {
                return document.getText( lineStart, lineEnd - lineStart );
            }
        }
        catch( BadLocationException ble )
        {
            System.out.println( ble.offsetRequested() );
            ble.printStackTrace();
        }

        return "";
    }

    public static String getLineTextNoWhitespace( Document document, int line )
    {
        return getLineText( document, line ).trim();
    }

    /////////////////////////
    // Current Word Functions
    /////////////////////////
    public static char getCharAt( Document document, int position )
            throws BadLocationException
    {
        return document.getText( position, 1 ).charAt( 0 );
    }
    private static char[] wordSeparatingCharacters = new char[]
    {
        '!', '@', '#',
        '$', '%', '^',
        '&', '*', '(',
        ')', '-', '_',
        '=', '+', '\\',
        '|', '~', '[',
        ']', '{', '}',
        ';', ':', '\'',
        '"', ',', '.',
        '<', '>', '/',
        '?'
    };

    public static boolean isWordSeparatingCharacter( char c )
    {
        for( int i = 0; i < wordSeparatingCharacters.length; i++ )
        {
            if( c == wordSeparatingCharacters[i] )
            {
                return true;
            }
        }
        return false;
    }

    public static int getWordStart( Document document, int startingPosition )
    {
        int lineNumber = getLineNumber( document, startingPosition );
        int lineStart = getLineStartOffset( document, lineNumber );

        int length = document.getLength();
        int result = startingPosition;

        try
        {
            // possibilities:
            // we're at the begining of the document already
            // the previous token is whitespace
            // the previous token is a word separator
            // the previous token is a regular character

            if( result == 0 )
            {
                // we're at the begining of the document already
                return result;
            }
            else
            {
                result--;
                char currentChar = getCharAt( document, result );

                if( startingPosition == lineStart )
                {
                    return result;
                }
                else if( Character.isWhitespace( currentChar ) )
                {
                    result = findStartOfWhitespace( document, result );
                }
                else if( isWordSeparatingCharacter( currentChar ) )
                {
                    result = findStartOfWordSeparators( document, result );
                }
                else
                {
                    result = findStartOfRegularCharacter( document, result );
                }
            }

            if( result < lineStart )
            {
                return lineStart;
            }

            return result;
        }
        catch( BadLocationException ble )
        {
            ble.printStackTrace();
        }
        return startingPosition;
    }

    public static int findStartOfWhitespace( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        char currentChar = getCharAt( document, result );

        while( result > 0 && Character.isWhitespace( currentChar ) )
        {
            result--;
            currentChar = getCharAt( document, result );
        }

        //if( result == 0 && !Character.isWhitespace( currentChar ) )
        if( !Character.isWhitespace( currentChar ) )
        {
            result++;
        }

        return result;
    }

    public static int findStartOfWordSeparators( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        char currentChar = getCharAt( document, result );

        while( result > 0 && isWordSeparatingCharacter( currentChar ) )
        {
            result--;
            currentChar = getCharAt( document, result );
        }

        if( !isWordSeparatingCharacter( currentChar ) )
        {
            result++;
        }

        return result;
    }

    public static int findStartOfRegularCharacter( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        char currentChar = getCharAt( document, result );

        while( result > 0 && !isWordSeparatingCharacter( currentChar )
               && !Character.isWhitespace( currentChar ) )
        {
            result--;
            currentChar = getCharAt( document, result );
        }

        if( isWordSeparatingCharacter( currentChar )
            || Character.isWhitespace( currentChar ) )
        {
            result++;
        }

        return result;
    }

    public static int getWordEnd( Document document, int startingPosition )
    {
        int lineNumber = getLineNumber( document, startingPosition );
        int lineEnd = getLineEndOffset( document, lineNumber );

        int length = document.getLength();
        int result = startingPosition;

        try
        {
            // possibilities:
            // we're at the end of the document already
            // the next token is whitespace
            // the next token is a word separator
            // the next token is a regular character

            if( result == length )
            {
                // we're at the begining of the document already
                return result;
            }
            else
            {
                char currentChar = getCharAt( document, result );

                if( currentChar == '\n' )
                {
                    return result + 1;
                }
                else if( Character.isWhitespace( currentChar ) )
                {
                    result = findEndOfWhitespace( document, result );
                }
                else if( isWordSeparatingCharacter( currentChar ) )
                {
                    result = findEndOfWordSeparators( document, result );
                }
                else
                {
                    result = findEndOfRegularCharacter( document, result );
                }
            }

            if( startingPosition != lineEnd && result > lineEnd )
            {
                return lineEnd;
            }

            return result;
        }
        catch( BadLocationException ble )
        {
            ble.printStackTrace();
        }
        return startingPosition;
    }

    public static int findEndOfWhitespace( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        int length = document.getLength();
        char currentChar = getCharAt( document, result );

        while( result < length && Character.isWhitespace( currentChar )
               && currentChar != '\n' )
        {
            result++;
            currentChar = getCharAt( document, result );
        }

        return result;
    }

    public static int findEndOfWordSeparators( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        int length = document.getLength();
        char currentChar = getCharAt( document, result );

        while( result < length && isWordSeparatingCharacter( currentChar ) )
        {
            result++;
            currentChar = getCharAt( document, result );
        }

        return result;
    }

    public static int findEndOfRegularCharacter( Document document, int position )
            throws BadLocationException
    {
        int result = position;
        int length = document.getLength();
        char currentChar = getCharAt( document, result );

        while( result < length && !isWordSeparatingCharacter( currentChar )
               && !Character.isWhitespace( currentChar ) )
        {
            result++;
            currentChar = getCharAt( document, result );
        }

        return result;
    }

    ////////////////////////
    // Element Mapinpulators
    ////////////////////////
    public static Element getElementForLine( Document document, int line )
    {
        Element[] roots = document.getRootElements();
        Element root = roots[0];
        return root.getElement( line - 1 );
    }

    public static List<Element> getParagraphElements( StyledDocument document, int startPosition, int endPosition )
    {
        List<Element> result = new ArrayList();
        for( int i = startPosition; i <= endPosition; i++ )
        {
            Element paragraph = document.getParagraphElement( i );
            if( !result.contains( paragraph ) )
            {
                result.add( paragraph );
            }
        }
        return result;
    }

    public static List<Element> getParagraphElements( StyledDocument document )
    {
        return getParagraphElements( document, 0, document.getLength() );
    }

    ////////
    //Styles
    ////////
    public static void addHyperlink( DefaultStyledDocument document, int start, int lenght, String url )
    {
        SimpleAttributeSet hyperlinkAttributes = new SimpleAttributeSet();
        hyperlinkAttributes.addAttribute( HTML.Attribute.HREF, url);
        hyperlinkAttributes.addAttribute( StyleConstants.Foreground, Color.BLUE );
        document.setCharacterAttributes( start, start, hyperlinkAttributes, false );
    }

    public static String getHyperlink( DefaultStyledDocument document, int start, int lenght )
    {
        for( int i = start; i < start + lenght; i++ )
        {
            AttributeSet attributes = document.getCharacterElement( i ).getAttributes();
            Object url = attributes.getAttribute( HTML.Attribute.HREF );
            if( url != null)
            {
                return url.toString();
            }
        }

        return null;
    }

    public static void removeHyperlink( DefaultStyledDocument document, int start, int lenght )
    {
        SimpleAttributeSet hyperlinkAttributes = new SimpleAttributeSet();
        document.setCharacterAttributes( start, start, hyperlinkAttributes, true );
    }
    
    public static void overrideStyle( String styleName, StyledDocument document, Style newStyle )
    {
        Style style = document.getStyle( styleName );
        if( style == null )
        {
            style = styleContext.addStyle( styleName, null );
            document.addStyle( styleName, style );
        }

        overrideStyle( style, newStyle );
    }

    public static void overrideStyle( String styleName, JTextPane textPane, Style newStyle )
    {
        Style style = textPane.getStyle( styleName );
        if( style == null )
        {
            style = styleContext.addStyle( styleName, null );
            textPane.addStyle( styleName, style );
        }

        overrideStyle( style, newStyle );
    }

    public static void overrideStyle( Style originalStyle, Style newStyle )
    {
        overrideStyleElement( originalStyle, newStyle, StyleConstants.FontFamily );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Family );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.FontSize );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Size );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Foreground );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Background );

        overrideStyleElement( originalStyle, newStyle, StyleConstants.Bold );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Italic );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.StrikeThrough );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Subscript );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Superscript );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Underline );

        overrideStyleElement( originalStyle, newStyle, StyleConstants.RightIndent );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.LeftIndent );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.SpaceAbove );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.SpaceBelow );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.FirstLineIndent );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.LineSpacing );

        overrideStyleElement( originalStyle, newStyle, StyleConstants.Alignment );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.BidiLevel );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.Orientation );
        overrideStyleElement( originalStyle, newStyle, StyleConstants.TabSet );
    }

    public static void overrideDefaultStyle( StyledDocument document, Style newStyle )
    {
        overrideStyle( "default", document, newStyle );
    }

    public static boolean overrideStyleElement( Style originalStyle, Style newStyle, Object key )
    {
//        System.out.println( "DocumentUtils: setting " + key + " to " + newStyle.getAttribute( key ) );

        originalStyle.removeAttribute( key );

        Object attribute = newStyle.getAttribute( key );
        if( attribute != null )
        {
            originalStyle.addAttribute( key, attribute );
            return true;
        }

        return false;
    }

    public static void setLogicalStyle( DefaultStyledDocument document, int startIndex, int endIndex, Style style )
    {
        if( document.getStyle( style.getName() ) == null )
        {
            document.addStyle( style.getName(), style );
        }

        List<Element> paragraphs = DocumentUtils.getParagraphElements( document, startIndex, endIndex );
        for( Element paragraph : paragraphs )
        {
            int offset = paragraph.getStartOffset();
            document.setLogicalStyle( offset, style );
        }
    }

    public static Style getTextEditorStyle()
    {
        if( textEditorStyle == null )
        {
            textEditorStyle = styleContext.addStyle( textEditorStyleName, jvmDefaultStyle );

            textEditorStyle.addAttribute( StyleConstants.FontFamily, "Arial" );
            textEditorStyle.addAttribute( StyleConstants.FontSize, new Integer( 14 ) );

            textEditorStyle.addAttribute( StyleConstants.LineSpacing, new Float( 0.5 ) );
            textEditorStyle.addAttribute( StyleConstants.FirstLineIndent, new Float( 15 ) );
            textEditorStyle.addAttribute( StyleConstants.SpaceBelow, new Float( 10 ) );
            textEditorStyle.addAttribute( StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED );

            textEditorStyle.addAttribute( StyleConstants.Foreground, Color.black );
            textEditorStyle.addAttribute( StyleConstants.Background, Color.white );
            textEditorStyle.addAttribute( StyleConstants.Bold, Boolean.FALSE );
            textEditorStyle.addAttribute( StyleConstants.Italic, Boolean.FALSE );
        }

        return textEditorStyle;
    }

    public static Style getCodeEditorStyle()
    {
        if( codeEditorStyle == null )
        {
            codeEditorStyle = styleContext.addStyle( codeEditorStyleName, jvmDefaultStyle );

            codeEditorStyle.addAttribute( StyleConstants.FontFamily, "Arial" );
            codeEditorStyle.addAttribute( StyleConstants.FontSize, new Integer( 14 ) );
            codeEditorStyle.addAttribute( StyleConstants.Foreground, Color.black );
            codeEditorStyle.addAttribute( StyleConstants.Background, Color.white );
            codeEditorStyle.addAttribute( StyleConstants.Bold, Boolean.FALSE );
            codeEditorStyle.addAttribute( StyleConstants.Italic, Boolean.FALSE );
        }

        return codeEditorStyle;
    }
}