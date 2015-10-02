package galvin.swing.editor;

import galvin.StringUtils;
import galvin.swing.spell.SpellingHighlighter;
import galvin.swing.text.DocumentUtils;
import galvin.swing.text.TextControlUtils;
import galvin.swing.text.caret.InvisibleCaret;
import galvin.swing.text.caret.OverwriteCaret;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Keymap;

public class Editor
    extends JTextPane
    implements Editable {

    public static final int blinkRate = 500;
    private static final String textPaneKeymapName = "com.galvin.editor.Editor.KEYMAP";
//    private static final String PASTE_MIME_TYPE_RICH_TEXT = "text/rtf";
    private static final String MONACO = "Monaco";
    private static final String COURIER_NEW = "Courier New";
    private static final String COURIER = "Courier";
    private static final String MONOSPACED = "Monospaced";
    private static final int FONT_SIZE = 13;
    private static int spacesPerTab = 4;
    private static boolean anitAliasing = true;
    private static boolean overwriting = false;
    private boolean smartHomeEndKeys = true;
    private boolean floatingNewline = true;
    private int defaultFontSize = FONT_SIZE;
    private Caret insertCaret = new DefaultCaret();
    private Caret overwriteCaret = new OverwriteCaret();
    private boolean softTabs = false;
    private boolean stylesAllowed = true;
//    private boolean lineWrap = true;
    private SpellingHighlighter spellingHighlighter;

    public Editor() {
        this( new EditorDocument() );
    }

    public Editor( EditorDocument document ) {
        super( document );
        setUI( new BasicTextPaneUI() );
        init();
        setDocument( document );
    }

    private void init() {
        try {
            String[] fontsArray = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            List<String> fonts = new ArrayList();
            fonts.addAll( Arrays.asList( fontsArray ) );

//            for( String font : fonts )
//            {
//                System.out.println( font );
//            }
            if( fonts.contains( MONACO ) ) {
                Font font = new Font( MONACO, Font.PLAIN, getDefaultFontSize() );
                setFont( font );
            }
            else {
                Font font = new Font( MONOSPACED, Font.PLAIN, getDefaultFontSize() );
                setFont( font );
            }
        }
        catch( Throwable t ) {
            Font font = new Font( MONOSPACED, Font.PLAIN, getDefaultFontSize() );
            setFont( font );

            t.printStackTrace();
        }

        TextControlUtils.addLineSelectingClickListener( this );
        setEditorKit( new LocalEditorKit() );
        EditorUtils.buildKeyMap( this );
        selectCaret();
        setHighlighter( new EditorHighlighter() );
    }

    public EditorDocument getEditorDocument() {
        return (EditorDocument) getDocument();
    }

    public void setDocument( EditorDocument doc ) {
        super.setDocument( doc );
        if( getSpellingHighlighter() != null ) {
            getSpellingHighlighter().refreshDocument();
        }
    }

    public SpellingHighlighter getSpellingHighlighter() {
        return spellingHighlighter;
    }

    public void setSpellingHighlighter( SpellingHighlighter spellingHighlighter ) {
        this.spellingHighlighter = spellingHighlighter;
        spellingHighlighter.setTextComponent( this );
    }

    public void undo() {
        try {
            getEditorDocument().undo();
        }
        catch( ClassCastException cce ) {
        }
    }

    public void redo() {
        try {
            getEditorDocument().redo();
        }
        catch( ClassCastException cce ) {
        }
    }

    public void discardAllEdits() {
        try {
            getEditorDocument().discardAllEdits();
        }
        catch( ClassCastException cce ) {
        }
    }

    /**
     * Overridden to provide insert/overwrite functionality.
     */
    @Override
    public void replaceSelection( String content ) {
        if( softTabs ) {
            content = StringUtils.tabsToSpaces( content, spacesPerTab );
        }

        //overwrite code
        Document doc = getDocument();
        if( doc != null ) {
            if( overwriting == true && getSelectionStart() == getSelectionEnd() ) {
                int insertPosition = getCaretPosition();
                int overwriteLength = doc.getLength() - insertPosition;
                int length = content.length();
                if( overwriteLength > length ) {
                    overwriteLength = length;
                }

                try {
                    doc.remove( insertPosition, overwriteLength );
                }
                catch( BadLocationException ble ) {
                }
            }
        }
        
        //System.out.println( "content: [" + content + "]" );
        super.replaceSelection( content );
    }

    public String getLocalKeymapName() {
        return textPaneKeymapName;
    }

    /**
     * Returns the Keymap that contains specialized bindings.
     */
    public Keymap getLocalKeymap() {
        return getKeymap( textPaneKeymapName );
    }

    public int getLineCount() {
        return DocumentUtils.getLineCount( getDocument() );
    }

    public int getLineStartOffset( int line ) {
        return DocumentUtils.getLineStartOffset( getDocument(), line );
    }

    public int getLineEndOffset( int line ) {
        return DocumentUtils.getLineEndOffset( getDocument(), line );
    }

    public int getLineOfOffset( int offset ) {
        Document document = getDocument();
        return DocumentUtils.getLineNumber( document, offset );
    }

    public int getCaretLineNumber() {
        Document document = getDocument();
        int offset = getCaretPosition();
        return DocumentUtils.getLineNumber( document, offset );
    }

    /**
     * Used to determine the behavior of the home and end keys.
     */
    public boolean getSmartHomeEndKeys() {
        return smartHomeEndKeys;
    }

    /**
     * If this is set to Boolean.TRUE, the home/end keys will support 'smart'
     * behaviour: (one press = first/last non-whitespace character, two presses
     * = start/end of line). By default, this property is set to true.
     */
    public void setSmartHomeEndKeys( boolean smartHomeEndKeys ) {
        this.smartHomeEndKeys = smartHomeEndKeys;
    }

    /**
     * True sets all text controls to Overwrite mode, false sets all controls to
     * Insert mode. Adapted from Core SWING Advanced Programming by Kim Topley
     */
    public static void setOverwriting( boolean overwriting ) {
        Editor.overwriting = overwriting;
    }

    /**
     * Returns if buffers are overwriting or not.
     */
    public static boolean getOverwriting() {
        return overwriting;
    }

    /**
     * Toggles between overwrite and insert mode.
     */
    public static void toggleOverwriting() {
        setOverwriting( !getOverwriting() );
    }

    /**
     * Allows the replacement of the default overwrite caret.
     */
    public void setOverwriteCaret( Caret c ) {
        overwriteCaret = c;
    }

    /**
     * Overridden to select the appropriate caret when focus is gained.
     */
    @Override
    public void processFocusEvent( FocusEvent e ) {
        super.processFocusEvent( e );
        if( e.getID() == FocusEvent.FOCUS_GAINED ) {
            selectCaret();
        }
    }

    /**
     * Sets the appropriate caret for the current mode.
     */
    public void selectCaret() {
        Caret oldCaret = getCaret();
        if( oldCaret instanceof InvisibleCaret ) {
            return;
        }

        Caret newCaret;
        if( overwriting ) {
            newCaret = overwriteCaret;
        }
        else {
            newCaret = insertCaret;
        }

        int start = getSelectionStart();
        int end = getSelectionEnd();

        super.setCaret( newCaret );
        newCaret.setBlinkRate( blinkRate );

        setCaretPosition( start );
        moveCaretPosition( end );

        newCaret.setVisible( true );
    }

    public boolean getFloatingNewline() {
        return floatingNewline;
    }

    public void setFloatingNewline( boolean floatingNewline ) {
        this.floatingNewline = floatingNewline;
    }

    /**
     * Sets whether or not tabs are expanded into spaces.
     */
    public void setSoftTabs( boolean softTabs ) {
        this.softTabs = softTabs;
    }

    /**
     * Returns whether or not tabs are expanded into spaces.
     */
    public boolean getSoftTabs() {
        return softTabs;
    }

    /**
     * Sets the number of spaces that will be substituted for a tab. Default is
     * 2.
     */
    public void setSpacesPerTab( int spacesPerTab ) {
        Editor.spacesPerTab = spacesPerTab;
    }

    /**
     * Returns the number of spaces that will be substituted for a tab.
     */
    public int getSpacesPerTab() {
        return spacesPerTab;
    }

    /**
     * Returns true if this text control will allow styles the styles convinience
     * methods to be used.
     */
    public boolean getStylesAllowed() {
        return stylesAllowed;
    }

    /**
     * Sets if this text control will allow styles the styles convinence methods
     * to be used.
     */
    public void setStylesAllowed( boolean stylesAllowed ) {
        this.stylesAllowed = stylesAllowed;
    }
    
    public boolean getAntiAliasing() {
        return anitAliasing;
    }

    public void setAntiAliasing( boolean anitAliasing ) {
        Editor.anitAliasing = anitAliasing;
    }

    @Override
    public void paint( Graphics g ) {
        try {
            Graphics2D g2 = (Graphics2D) g;
            if( anitAliasing ) {
                g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
            }

            super.paint( g2 );
        }
        catch( Throwable t ) {
            t.printStackTrace();
        }
    }

    public void repaintLine( int line )
        throws BadLocationException {
        Rectangle startRectangle = modelToView( DocumentUtils.getLineStartOffset( getDocument(), line ) );

        int x = 0;
        int y = (int) startRectangle.getY();
        int width = getWidth();
        int height = (int) startRectangle.getHeight();

        Rectangle highlightRectangle = new Rectangle( x, y, width, height );
        repaint( highlightRectangle );
    }

    public void pasteRichText( Document document ) {
        replaceSelection( DocumentUtils.getText( document ) );
    }

    public int getDefaultFontSize() {
        return defaultFontSize;
    }

    public void setDefaultFontSize( int defaultFontSize ) {
        this.defaultFontSize = defaultFontSize;
    }

}
