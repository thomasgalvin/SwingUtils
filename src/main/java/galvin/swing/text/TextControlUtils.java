package galvin.swing.text;

import galvin.swing.text.style.StyleUtils;
import galvin.StringUtils;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class TextControlUtils
{

    private static final HashMap matchingCharacters = new HashMap();

    static
    {
        MatchingCharacters mc = new MatchingCharacters( '{', '}' );
        matchingCharacters.put( "{", mc );
        matchingCharacters.put( "}", mc );

        mc = new MatchingCharacters( '[', ']' );
        matchingCharacters.put( "[", mc );
        matchingCharacters.put( "]", mc );

        mc = new MatchingCharacters( '(', ')' );
        matchingCharacters.put( "(", mc );
        matchingCharacters.put( ")", mc );

        mc = new MatchingCharacters( '<', '>' );
        matchingCharacters.put( "<", mc );
        matchingCharacters.put( ">", mc );
    }

    private static class MatchingCharacters
    {

        private char start;
        private char end;

        public MatchingCharacters( char start, char end )
        {
            this.start = start;
            this.end = end;
        }

        public char getStart()
        {
            return start;
        }

        public char getEnd()
        {
            return end;
        }

        public boolean isStart( char character )
        {
            return character == start;
        }

        public boolean isEnd( char character )
        {
            return character == end;
        }
    }

    static MatchingCharacters getMatchingCharacter( char c )
    {
        return (MatchingCharacters) matchingCharacters.get( "" + c );
    }

    public static void addToStartOfLine( JTextComponent component, String text )
    {
        try
        {
            Document doc = component.getDocument();
            int caretPosition = component.getCaretPosition();

            int currentLine = DocumentUtils.getLineNumber( doc, caretPosition );
            int lineStart = DocumentUtils.getLineStartOffset( doc, currentLine );

            component.setCaretPosition( lineStart );
            component.replaceSelection( text );
            component.setCaretPosition( caretPosition + text.length() );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public static void spellCheckReplace( JTextComponent component, String badWord, String goodWord, int caretPosition )
    {
        StringBuilder builder = new StringBuilder( component.getText() );
        StringUtils.replaceAll( builder, "\r", "" );
        String text = builder.toString();
        int length = text.length();

        int badLength = badWord.length();
        while( caretPosition >= 0 )
        {
            if( caretPosition + badLength <= length )
            {
                String test = text.substring( caretPosition, caretPosition + badLength );

                component.setCaretPosition( caretPosition );
                component.moveCaretPosition( caretPosition + badLength );

                if( test.equals( badWord ) )
                {
                    break;
                }
                else
                {
                    caretPosition--;
                }
            }
            else
            {
                caretPosition--;
            }
        }

        if( caretPosition >= 0 )
        {
            component.setCaretPosition( caretPosition );
            component.moveCaretPosition( caretPosition + badLength );
            replaceSelectionAndSelect( component, goodWord );
        }
    }

    public static void replaceSelectionAndSelect( JTextComponent component, String text )
    {
        int position = Math.min( component.getSelectionStart(), component.getSelectionEnd() );
        component.replaceSelection( text );
        component.setCaretPosition( position );
        component.moveCaretPosition( position + text.length() );
    }

    public static void markup( JTextComponent editor, String startMarkup )
    {
        markup( editor, startMarkup, startMarkup );
    }

    public static void markup( JTextComponent editor, String startMarkup, String endMarkup )
    {
        int start = Math.min( editor.getSelectionStart(), editor.getSelectionEnd() );
        int end = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );

        String selectedText = StringUtils.neverNull( editor.getSelectedText() );
        String newText = StringUtils.markup( selectedText, startMarkup, endMarkup );
        editor.replaceSelection( newText );

        start += startMarkup.length();
        end += startMarkup.length();

        editor.setCaretPosition( start );
        editor.moveCaretPosition( end );
    }

    public static void deleteCurrentLine( JTextComponent textPane )
    {
        Document document = textPane.getDocument();
        int length = document.getLength();

        int currentLine = DocumentUtils.getCaretLine( document, textPane.getCaretPosition() );
        int lineCount = DocumentUtils.getLineCount( document );
        int start = DocumentUtils.getLineStartOffset( document, currentLine );
        int end = DocumentUtils.getLineEndOffset( document, currentLine );

        if( currentLine == lineCount && currentLine != 1 )
        {
            start--;
        }

        if( end > length )
        {
            end = length;
        }

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
        textPane.replaceSelection( "" );

    }

    public static String[] getAvailableFontFamilyNames()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public static void goToMatchingChar( JTextComponent textPane, boolean select )
    {
        try
        {
            int pos = textPane.getCaretPosition();
            if( pos > 0 )
            {
                pos--;

                Document doc = textPane.getDocument();
                int length = doc.getLength();
                String text = doc.getText( 0, length );
                char c = text.charAt( pos );

                MatchingCharacters mc = getMatchingCharacter( c );
                if( mc != null )
                {
                    int result = -1;
                    if( mc.isStart( c ) )
                    {
                        result = searchMatchingCharForwards( text, mc.getStart(), mc.getEnd(), pos );
                    }
                    else
                    {
                        result = searchMatchingCharBackwards( text, mc.getStart(), mc.getEnd(), pos );
                    }

                    if( result != -1 )
                    {
                        if( mc.isStart( c ) )
                        {
                            if( select )
                            {
                                textPane.setCaretPosition( pos );
                                textPane.moveCaretPosition( result + 1 );
                            }
                            else
                            {
                                textPane.setCaretPosition( result + 1 );
                            }
                        }
                        else
                        {
                            if( select )
                            {
                                textPane.setCaretPosition( pos + 1 );
                                textPane.moveCaretPosition( result );
                            }
                            else
                            {
                                textPane.setCaretPosition( result );
                            }
                        }
                    }
                }
            }
        }
        catch( BadLocationException ble )
        {
        }
    }

    private static int searchMatchingCharForwards( String text, char start, char end,
                                                   int pos )
    {
        int length = text.length();
        int startsFound = 0;
        char c;

        for( int i = pos; i < length; i++ )
        {
            c = text.charAt( i );
            if( c == start )
            {
                startsFound++;
            }
            else if( c == end )
            {
                startsFound--;

                if( startsFound == 0 )
                {
                    return i;
                }
            }
        }

        return -1;
    }

    private static int searchMatchingCharBackwards( String text, char start, char end,
                                                    int pos )
    {
        int endsFound = 0;
        char c;

        for( int i = pos; i >= 0; i-- )
        {
            c = text.charAt( i );
            if( c == end )
            {
                endsFound++;
            }
            else if( c == start )
            {
                endsFound--;

                if( endsFound == 0 )
                {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     Forces a portion of the document to become visible.
     */
    public static void scrollTo( JTextPane textPane, int position )
        throws BadLocationException
    {
        Rectangle r = textPane.modelToView( position );
        if( r != null )
        {
            textPane.scrollRectToVisible( r );
        }
    }

    public static void selectCurrentWord( JTextPane textPane )
    {
        int caret = textPane.getCaretPosition();
        int start = DocumentUtils.getWordStart( textPane.getDocument(), caret );
        int end = DocumentUtils.getWordEnd( textPane.getDocument(), caret );

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static String getCurrentWord( JTextComponent textControl )
    {
        int caret = textControl.getCaretPosition();
        return getCurrentWord( textControl, caret );
    }

    public static String getCurrentWord( JTextComponent textControl, int caret )
    {
        int start = DocumentUtils.getWordStart( textControl.getDocument(), caret );
        int end = DocumentUtils.getWordEnd( textControl.getDocument(), caret );

        try
        {
            return textControl.getDocument().getText( start, end - start );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }

        return "";
    }

    public static void selectCurrentLineNoWhitespace( JTextPane textPane )
    {
        Document document = textPane.getDocument();
        int caret = textPane.getCaretPosition();
        int line = DocumentUtils.getCaretLine( document, caret );
        int start = DocumentUtils.getLineStartOffsetNoWhitespace( document, line );
        int end = DocumentUtils.getLineEndOffsetNoWhitespace( document, line );

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static void selectCurrentLine( JTextPane textPane )
    {
        Document document = textPane.getDocument();
        int caret = textPane.getCaretPosition();
        int line = DocumentUtils.getCaretLine( document, caret );
        int start = DocumentUtils.getLineStartOffset( document, line );
        int end = DocumentUtils.getLineEndOffset( document, line ) - 1;

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static void selectionTabsToSpaces( JTextPane textPane,
                                              int spacesPerTab )
    {
        String text = StringUtils.tabsToSpaces( textPane.getSelectedText(), spacesPerTab );
        textPane.replaceSelection( text );
    }

    public static void selectionSpacesToTabs( JTextPane textPane,
                                              int spacesPerTab )
    {
        String text = StringUtils.spacesToTabs( textPane.getSelectedText(), spacesPerTab );
        textPane.replaceSelection( text );
    }

    public static boolean isSelectionUpperCase( JTextComponent textPane )
    {
        String text = textPane.getSelectedText();
        byte[] bytes = text.getBytes();

        for( int i = 0; i < bytes.length; i++ )
        {
            if( Character.isLowerCase( (char) bytes[i] ) )
            {
                return false;
            }
        }

        return true;
    }

    public static void selectionToUpperCase( JTextComponent textPane )
    {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if( Math.abs( start - end ) > 0 )
        {
            String upper = textPane.getSelectedText().toUpperCase();
            textPane.replaceSelection( upper );

            textPane.setCaretPosition( start );
            textPane.moveCaretPosition( end );
        }
    }

    public static boolean isSelectionLowerCase( JTextComponent textPane )
    {
        String text = textPane.getSelectedText();
        byte[] bytes = text.getBytes();

        for( int i = 0; i < bytes.length; i++ )
        {
            if( Character.isUpperCase( (char) bytes[i] ) )
            {
                return false;
            }
        }

        return true;
    }

    public static void selectionToLowerCase( JTextComponent textPane )
    {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if( Math.abs( start - end ) > 0 )
        {
            String lower = textPane.getSelectedText().toLowerCase();
            textPane.replaceSelection( lower );

            textPane.setCaretPosition( start );
            textPane.moveCaretPosition( end );
        }
    }

    public static void toggleSelectionUpperCase( JTextComponent textPane )
    {
        if( isSelectionUpperCase( textPane ) )
        {
            selectionToLowerCase( textPane );
        }
        else
        {
            selectionToUpperCase( textPane );
        }
    }

    public static void reverseSelectionChars( JTextComponent textPane )
    {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        String text = StringUtils.reverseChars( textPane.getSelectedText() );
        textPane.replaceSelection( text );

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static void selectionROT13( JTextComponent textPane )
    {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        String text = StringUtils.rot13( textPane.getSelectedText() );
        textPane.replaceSelection( text );

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static void selectionROT135( JTextComponent textPane )
    {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        String text = StringUtils.rot135( textPane.getSelectedText() );
        textPane.replaceSelection( text );

        textPane.setCaretPosition( start );
        textPane.moveCaretPosition( end );
    }

    public static void overrideDefaultStyle( JTextPane textPane, Style newStyle )
    {
        StyledDocument document = textPane.getStyledDocument();
        DocumentUtils.overrideDefaultStyle( document, newStyle );

        int caretPosition = textPane.getCaretPosition();
        textPane.setCaretPosition( 0 );

        Style currentStyle = textPane.getLogicalStyle();
        if( currentStyle == null
            || "default".equalsIgnoreCase( currentStyle.getName() ) )
        {
            textPane.setLogicalStyle( newStyle );
        }
        textPane.setCaretPosition( caretPosition );
    }

    /**
     Sets the background color of the selected text.
     */
    public static void setSelectionBackground( JTextPane textPane, Color color )
    {
        if( color != null )
        {
            String BACKGROUND = "backgroundColor." + color.toString();
            Style backgroundStyle = new StyleContext().addStyle( BACKGROUND, null );
            backgroundStyle.addAttribute( StyleConstants.Background, color );
            textPane.setCharacterAttributes( backgroundStyle, false );
        }
    }

    /**
     Returns the current foreground color.
     */
    public static Color getForegroundColor( JTextPane textPane )
    {
        Color color = (Color) ( textPane.getInputAttributes().getAttribute( StyleConstants.Foreground ) );
        if( color == null )
        {
            color = new Color( 0, 0, 0 );
        }
        return color;
    }

    /**
     Sets the foreground color of the selection.
     */
    public static void setSelectionForeground( JTextPane textPane, Color color )
    {
        if( color != null )
        {
            String FOREGROUND = "foregroundColor." + color.toString();
            Style foregroundStyle = new StyleContext().addStyle( FOREGROUND, null );
            foregroundStyle.addAttribute( StyleConstants.Foreground, color );
            textPane.setCharacterAttributes( foregroundStyle, false );
        }
    }

    /**
     Returns the font family of the current character.
     */
    public static String getFontFamily( JTextPane textPane )
    {
        String family = (String) ( textPane.getInputAttributes().getAttribute( StyleConstants.FontFamily ) );
        return family;
    }

    /**
     Sets the font family of the selected text.
     */
    public static void setSelectionFontFamily( JTextPane textPane, String fontFamily )
    {
        if( fontFamily != null )
        {
            String FONTFAMILY = "fontFamily." + fontFamily;
            Style fontFamilyStyle = new StyleContext().addStyle( FONTFAMILY, null );
            fontFamilyStyle.addAttribute( StyleConstants.FontFamily, fontFamily );
            textPane.setCharacterAttributes( fontFamilyStyle, false );
        }
    }

    /**
     Returns the font size of the current character.
     */
    public static int getFontSize( JTextPane textPane )
    {
        Integer fontSize = (Integer) ( textPane.getInputAttributes().getAttribute( StyleConstants.FontSize ) );
        if( fontSize == null )
        {
            fontSize = new Integer( 12 );
        }
        return fontSize.intValue();
    }

    /**
     Sets the font size of the selected text.
     */
    public static void setSelectionFontSize( JTextPane textPane, int fontSize )
    {
        String FONTFAMILY = "fontSize." + fontSize;
        Style fontSizeStyle = new StyleContext().addStyle( FONTFAMILY, null );
        fontSizeStyle.addAttribute( StyleConstants.FontSize, new Integer( fontSize ) );
        textPane.setCharacterAttributes( fontSizeStyle, false );
    }

    /**
     Returns true if all of the selection is bold, otherwise false.
     */
    public static boolean isSelectionBold( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.Bold, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.Bold, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either bold or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionBold( JTextPane textPane, boolean bold )
    {
        if( bold )
        {
            textPane.setCharacterAttributes( StyleUtils.boldStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.unboldStyle, false );
        }
    }

    /**
     If any part of the selection is currently not bold, the entire selection will be
     bolded, otherwise, the entire selection will be unbolded.
     */
    public static void toggleSelectionBold( JTextPane textPane )
    {
        setSelectionBold( textPane, !isSelectionBold( textPane ) );
    }

    /**
     Returns true if all of the selection is italic, otherwise false.
     */
    public static boolean isSelectionItalic( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.Italic, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.Italic, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either italic or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionItalic( JTextPane textPane, boolean italic )
    {
        if( italic )
        {
            textPane.setCharacterAttributes( StyleUtils.italicStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.unitalicStyle, false );
        }
    }

    /**
     If any part of the selection is currently not italic, the entire selection will be
     italiced, otherwise, the entire selection will be unitaliced.
     */
    public static void toggleSelectionItalic( JTextPane textPane )
    {
        setSelectionItalic( textPane, !isSelectionItalic( textPane ) );
    }

    /**
     Returns true if all of the selection is underline, otherwise false.
     */
    public static boolean isSelectionUnderline( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.Underline, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.Underline, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either underline or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionUnderline( JTextPane textPane, boolean underline )
    {
        if( underline )
        {
            textPane.setCharacterAttributes( StyleUtils.underlineStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.ununderlineStyle, false );
        }
    }

    /**
     If any part of the selection is currently not underline, the entire selection will be
     underlineed, otherwise, the entire selection will be ununderlineed.
     */
    public static void toggleSelectionUnderline( JTextPane textPane )
    {
        setSelectionUnderline( textPane, !isSelectionUnderline( textPane ) );
    }

    /**
     Returns true if all of the selection is strikeThrough, otherwise false.
     */
    public static boolean isSelectionStrikeThrough( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.StrikeThrough, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.StrikeThrough, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either strikeThrough or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionStrikeThrough( JTextPane textPane,
                                                  boolean strikeThrough )
    {
        if( strikeThrough )
        {
            textPane.setCharacterAttributes( StyleUtils.strikeThroughStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.unstrikeThroughStyle, false );
        }
    }

    /**
     If any part of the selection is currently not strikeThrough, the entire selection will be
     strikeThroughed, otherwise, the entire selection will be unstrikeThroughed.
     */
    public static void toggleSelectionStrikeThrough( JTextPane textPane )
    {
        setSelectionStrikeThrough( textPane, !isSelectionStrikeThrough( textPane ) );
    }

    /**
     Returns true if all of the selection is superscript, otherwise false.
     */
    public static boolean isSelectionSuperscript( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.Superscript, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.Superscript, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either superscript or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionSuperscript( JTextPane textPane, boolean superscript )
    {
        if( superscript )
        {
            textPane.setCharacterAttributes( StyleUtils.superscriptStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.unsuperscriptStyle, false );
        }
    }

    /**
     If any part of the selection is currently not superscript, the entire selection will be
     superscripted, otherwise, the entire selection will be unsuperscripted.
     */
    public static void toggleSelectionSuperscript( JTextPane textPane )
    {
        setSelectionSuperscript( textPane, !isSelectionSuperscript( textPane ) );
    }

    /**
     Returns true if all of the selection is subscript, otherwise false.
     */
    public static boolean isSelectionSubscript( JTextPane textPane )
    {
        int selectionStart = textPane.getSelectionStart();
        int selectionEnd = textPane.getSelectionEnd();
        StyledDocument doc = (StyledDocument) ( textPane.getDocument() );
        if( selectionStart == selectionEnd )
        {
            AttributeSet attr = textPane.getInputAttributes();
            if( !attr.containsAttribute( StyleConstants.Subscript, new Boolean( true ) ) )
            {
                return false;
            }
        }
        else
        {
            for( int i = selectionStart; i < selectionEnd; i++ )
            {
                AttributeSet attr = doc.getCharacterElement( i ).getAttributes();
                if( !attr.containsAttribute( StyleConstants.Subscript, new Boolean( true ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     Sets the selection to either subscript or normal, depending on the
     boolean value pased.
     */
    public static void setSelectionSubscript( JTextPane textPane, boolean subscript )
    {
        if( subscript )
        {
            textPane.setCharacterAttributes( StyleUtils.subscriptStyle, false );
        }
        else
        {
            textPane.setCharacterAttributes( StyleUtils.unsubscriptStyle, false );
        }
    }

    /**
     If any part of the selection is currently not subscript, the entire selection will be
     subscripted, otherwise, the entire selection will be unsubscripted.
     */
    public static void toggleSelectionSubscript( JTextPane textPane )
    {
        setSelectionSubscript( textPane, !isSelectionSubscript( textPane ) );
    }

    public static void alignLeft( JTextPane textPane )
    {
        textPane.setParagraphAttributes( StyleUtils.alignLeftStyle, false );
    }

    public static void alignCenter( JTextPane textPane )
    {
        textPane.setParagraphAttributes( StyleUtils.alignCenterStyle, false );
    }

    public static void alignRight( JTextPane textPane )
    {
        textPane.setParagraphAttributes( StyleUtils.alignRightStyle, false );
    }

    public static void alignJustify( JTextPane textPane )
    {
        textPane.setParagraphAttributes( StyleUtils.alignJustifiedStyle, false );
    }

    /**
     Removes formatting form the selection.
     */
    public static void setSelectionNormal( JTextPane textPane )
    {
        textPane.setCharacterAttributes( StyleUtils.normalStyle, true );
//        alignLeft( textPane );
//        setSelectionBold( textPane, false );
//        setSelectionItalic( textPane, false );
//        setSelectionUnderline( textPane, false );
//        setSelectionStrikeThrough( textPane, false );
//        setSelectionSuperscript( textPane, false );
//        setSelectionSubscript( textPane, false );
    }

    public static MouseListener addLineSelectingClickListener( JTextPane textPane )
    {
        LocalMouseListener localMouseListener = new LocalMouseListener( textPane );
        return localMouseListener;
    }

    private static class LocalMouseListener
        implements MouseListener
    {

        private JTextPane textPane;

        public LocalMouseListener( JTextPane textPane )
        {
            this.textPane = textPane;
            textPane.addMouseListener( this );
        }

        public void mouseClicked( MouseEvent evt )
        {
            int clickCount = evt.getClickCount();
            if( clickCount > 1 )
            {
                evt.consume();
                textPane.setCaretPosition( textPane.viewToModel( evt.getPoint() ) );
                if( clickCount == 2 )
                {
                    selectCurrentWord( textPane );
                }
                else if( clickCount == 3 )
                {
                    selectCurrentLineNoWhitespace( textPane );
                }
                else if( clickCount == 4 )
                {
                    selectCurrentLine( textPane );
                }
            }
        }

        public void mouseEntered( MouseEvent evt )
        {
        }

        public void mouseExited( MouseEvent evt )
        {
        }

        public void mousePressed( MouseEvent evt )
        {
        }

        public void mouseReleased( MouseEvent evt )
        {
        }
    }
}
