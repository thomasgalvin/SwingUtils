package galvin.swing.editor.style;

import galvin.swing.editor.Editor;
import galvin.swing.editor.EditorDocument;
import galvin.swing.text.DocumentUtils;
import galvin.swing.text.caret.InvisibleCaret;
import galvin.swing.text.style.StyleInfo;
import galvin.swing.text.style.StyleSet;
import galvin.swing.text.style.StyleUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

public class StyleManager
{

    private List<StyledDocument> documents = new ArrayList();
    private List<JTextPane> textPanes = new ArrayList();
    private HashMap<JTextPane, TextPaneListener> textPaneListeners = new HashMap();
    private JTextPane currentTextPane;
    private StyleControlEditor styleControlEditor;
    private EditorDocument styleDocument;
    private StyleSet styleSet;
    private StyleControlCaretListener styleControlCaretListener;
    private LocalStyleManagerListener localStyleManagerListener;
    private List<StyleManagerListener> listeners = new ArrayList();

    public StyleManager()
    {
        this( StyleUtils.getDefaultStyleSet() );
//        this( StyleUtils.getManuscriptStyleSet() );
    }

    public StyleManager( StyleSet styleSet )
    {
        styleControlEditor = new StyleControlEditor();
        styleControlEditor.setEditable( false );
        styleControlEditor.setCaret( new InvisibleCaret() );
        styleDocument = styleControlEditor.getEditorDocument();
        setStyleSet( styleSet );
        styleControlCaretListener = new StyleControlCaretListener();
        localStyleManagerListener = new LocalStyleManagerListener();
    }

    public Editor getStyleControlEditor()
    {
        return styleControlEditor;
    }

    public void addDocument( StyledDocument document )
    {
        if(  ! documents.contains( document ) )
        {
            documents.add( document );
            StyleUtils.updateStyles( styleSet, document );
        }
    }

    public void removeDocument( StyledDocument document )
    {
        documents.remove( document );
    }

    public List<StyledDocument> getDocuments()
    {
        List<StyledDocument> result = new ArrayList();
        result.addAll( documents );
        return result;
    }

    public void addTextPane( JTextPane textPane )
    {
        if(  ! textPanes.contains( textPane ) )
        {
            textPanes.add( textPane );

            TextPaneListener textPaneFocusListener = new TextPaneListener( textPane );
            textPaneListeners.put( textPane, textPaneFocusListener );

            updateStyles( textPane );
        }
    }

    public void removeTextPane( JTextPane textPane )
    {
        textPanes.remove( textPane );

        TextPaneListener textPaneFocusListener = textPaneListeners.get( textPane );
        textPane.removeFocusListener( textPaneFocusListener );
        textPane.removeCaretListener( textPaneFocusListener );
        textPaneListeners.remove( textPane );

    }

    public List<JTextPane> getTextPanes()
    {
        List<JTextPane> result = new ArrayList();
        result.addAll( textPanes );
        return result;
    }

    public void addListener( StyleManagerListener listener )
    {
        listeners.add( listener );
    }

    public void removeListener( StyleManagerListener listener )
    {
        listeners.remove( listener );
    }

    public void notifyListeners( Style style )
    {
        for( int i = listeners.size() - 1; i >= 0; i -- )
        {
            listeners.get( i ).styleSelected( this, style );
        }
    }

    public void highlightStyle( String styleName )
    {
        styleControlCaretListener.setListening( false );
        styleControlEditor.setNotifyingCaretEvents( false );

        List<Element> paragraphElements = DocumentUtils.getParagraphElements( styleDocument );
        for( Element paragraphElement : paragraphElements )
        {

            Style logicalStyle = styleDocument.getLogicalStyle( paragraphElement.getStartOffset() );
            if( logicalStyle != null )
            {
                if( logicalStyle.getName().equals( styleName ) )
                {
                    styleControlEditor.setCaretPosition( paragraphElement.getStartOffset() );
                    break;
                }
            }
        }

        styleControlCaretListener.setListening( true );
        styleControlEditor.setNotifyingCaretEvents( true );
        styleControlEditor.repaint();
    }

    public void updateStyles()
    {
        for( StyledDocument document : documents )
        {
            StyleUtils.updateStyles( styleSet, document );
        }

        for( JTextPane textPane : textPanes )
        {
            updateStyles( textPane );
        }
    }

    public void updateStyles( JTextPane textPane )
    {
        for( StyleInfo style : styleSet.getStyles() )
        {
            DocumentUtils.overrideStyle( style.getLogicalName(), textPane, style.getStyle() );
        }
    }

    public StyleSet getStyleSet()
    {
        return styleSet;
    }

    public void setStyleSet( StyleSet styleSet )
    {
        try
        {
            this.styleSet = styleSet;
            styleControlEditor.setText( "" );
            String name = null;
            int index = 0;
            int length = 0;

            for( StyleInfo styleInfo : styleSet.getStyles() )
            {
                if( index != 0 )
                {
                    styleDocument.insertString( index, "\n", null );
                    index ++;
                }

                name = styleInfo.getDisplayName();
                length = name.length();
                styleDocument.insertString( index, name, null );
                styleDocument.setLogicalStyle( index, styleInfo.getStyle() );
                index += name.length();
            }

            updateStyles();
            updateStyles( styleControlEditor );
            styleControlEditor.setCaretPosition( 0 );
        }
        catch( BadLocationException ble )
        {
            ble.printStackTrace();
        }
    }

    private class StyleControlCaretListener
            implements CaretListener
    {
        private boolean listening = true;

        public StyleControlCaretListener()
        {
            styleControlEditor.addCaretListener( this );
        }

        public void caretUpdate( CaretEvent caretEvent )
        {
            int position = caretEvent.getMark();
            Style style = styleDocument.getLogicalStyle( position );
            for( StyleInfo styleInfo : styleSet.getStyles() )
            {
                Style testStyle = styleInfo.getStyle();
                if( testStyle.equals( style ) )
                {
                    notifyListeners( style );
                }
            }
        }

        public boolean isListening()
        {
            return listening;
        }

        public void setListening( boolean listening )
        {
            this.listening = listening;
        }
    }

    ///////////////
    // Line Numbers
    ///////////////
    private static Font lineNumberFont = new Font( "Monospaced", 0, 14 );

    public static Font getLineNumberFont()
    {
        return lineNumberFont;
    }
    private static Color lineNumberColor = new Color( 100, 100, 255 );

    public static Color getLineNumberColor()
    {
        return lineNumberColor;
    }
    private static Color lineNumberBevelColor = new Color( 100, 200, 200 );

    public static Color getLineNumberBevelColor()
    {
        return lineNumberBevelColor;
    }
    private static Color currentLineColor = new Color( 225, 225, 225 );

    public static Color getCurrentLineColor()
    {
        return currentLineColor;
    }
    private static Color currentBlockColor = new Color( 225, 225, 255 );

    public static Color getCurrentBlockColor()
    {
        return currentBlockColor;
    }
    
    private class TextPaneListener
            implements FocusListener, CaretListener
    {

        private JTextPane textPane;

        public TextPaneListener( JTextPane textPane )
        {
            this.textPane = textPane;
            textPane.addFocusListener( this );
            textPane.addCaretListener( this );
        }

        public void focusGained( FocusEvent e )
        {
            currentTextPane = textPane;
        }

        public void focusLost( FocusEvent e )
        {
        }

        public void caretUpdate( CaretEvent caretEvent )
        {
            int mark = caretEvent.getMark();
            StyledDocument document = textPane.getStyledDocument();
            Style style = document.getLogicalStyle( mark );
            highlightStyle( style.getName() );
        }
    }

    private class LocalStyleManagerListener
            implements StyleManagerListener
    {

        public LocalStyleManagerListener()
        {
            StyleManager.this.addListener( this );
        }

        public void styleSelected( Object source, Style style )
        {
            if( currentTextPane != null )
            {
                StyledDocument document = currentTextPane.getStyledDocument();
                if( documents.contains( document ) )
                {
                    int start = currentTextPane.getSelectionStart();
                    int end = currentTextPane.getSelectionEnd();

                    List<Element> paragraphs = new ArrayList();
                    for( int index = start; index <= end; index ++ )
                    {
                        Element paragraph = document.getParagraphElement( index );
                        if(  ! paragraphs.contains( paragraph ) )
                        {
                            paragraphs.add( paragraph );
                            
                            //document.setCharacterAttributes( paragraph.getStartOffset(), paragraph.getEndOffset()-1, style, true );
                            document.setCharacterAttributes( paragraph.getStartOffset(), paragraph.getEndOffset()-1, StyleUtils.noAttributesStyle, true );
                            document.setLogicalStyle( index, style );
                        }
                    }

                    currentTextPane.setCaretPosition( start );
                    currentTextPane.moveCaretPosition( end );
                    currentTextPane.requestFocus();
                }
            }
        }
    }

    private class StyleControlEditor
        extends Editor
    {
        private boolean notifyingCaretEvents;

        public boolean isNotifyingCaretEvents()
        {
            return notifyingCaretEvents;
        }

        public void setNotifyingCaretEvents( boolean notifyingCaretEvents )
        {
            this.notifyingCaretEvents = notifyingCaretEvents;
        }

        @Override
        protected void fireCaretUpdate(CaretEvent e)
        {
            if( notifyingCaretEvents )
            {
                super.fireCaretUpdate( e );
            }
        }
    }
}