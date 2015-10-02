/**
Copyright &copy; 2010 Thomas Galvin.
 */
package galvin.swing.editor;

import galvin.swing.text.DocumentUtils;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

public class EditorDocument
    extends DefaultStyledDocument
{
//    private static final int GUTTER_PADDING = 1;
    private DocumentUndoManager undoManager = new DocumentUndoManager();
    private NeedsSavingListener needsSavingListener = new NeedsSavingListener();
    private boolean needsSaving = false;
    private boolean recordChanges = true;
//    private boolean showLineNumbers = true;

    public EditorDocument()
    {
        addUndoableEditListener( undoManager );
        addDocumentListener( needsSavingListener );
    }

    //////////////////////////
    // Line count, length, etc
    //////////////////////////
    public int getLineCount()
    {
        return DocumentUtils.getLineNumber( this, getLength() );
    }

    public String getText()
    {
        try
        {
            return getText( 0, getLength() );
        }
        catch( BadLocationException ble )
        {
            return "";
        }
    }

    public void setText( String text )
    {
        try
        {
            replace( 0, getLength(), text, null );
        }
        catch( BadLocationException ble )
        {
            ble.printStackTrace();
        }
    }
    
    public void setTextAndClearUndo( String text )
    {
        setText( text );
        undoManager.discardAllEdits();
    }

    /////////////
    // Attributes
    /////////////
    /**
    This function can be used to add a style to the document without alerting
    the undo/redo or needs-saving features.
     */
    public void setAttributesSilently( int offset, int length, AttributeSet s, boolean replace )
    {
        boolean rc = getRecordChanges();
        setRecordChanges( false );
        try
        {
            setCharacterAttributes( offset, length, s, replace );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        setRecordChanges( rc );
    }

    //////////////
    //needs saving
    //////////////
    /**
    Returns true if the document has been modified since the time it was
    last saved.
     */
    public boolean needsSaving()
    {
        return needsSaving;
    }

    /**
    Returns true if the document has been modified since the time it was
    last saved.
     */
    public boolean getNeedsSaving()
    {
        return needsSaving();
    }

    /**
    Sets whether or not the document needs saving, without actually writing
    it to disk.
     */
    public void setNeedsSaving( boolean needsSaving )
    {
        this.needsSaving = needsSaving;
    }

    ///////////
    //undo/redo
    ///////////
    /**
    Undoes the last edit made to the buffer.
     */
    public void undo()
    {
        undoManager.undo();
    }

    /**
    Redoes the last edit made to the buffer.
     */
    public void redo()
    {
        undoManager.redo();
    }

    public void discardAllEdits()
    {
        undoManager.discardAllEdits();
    }

    public void setRecordChanges( boolean recordChanges )
    {
        this.recordChanges = recordChanges;
        undoManager.setRecordChanges( recordChanges );
    }

    public boolean getRecordChanges()
    {
        return recordChanges;
    }

    /////////////////
    // line numbering
    /////////////////
//    public int getGutterPadding()
//    {
//        return GUTTER_PADDING;
//    }
//
//    public boolean showLineNumbers()
//    {
//        return showLineNumbers;
//    }
//
//    public void showLineNumbers( boolean showLineNumbers )
//    {
//        this.showLineNumbers = showLineNumbers;
//    }

    ////////////////
    // Inner Classes
    ////////////////

    private class NeedsSavingListener
        implements DocumentListener
    {

        public void changedUpdate( DocumentEvent evt )
        {
            //for some reason, gaining focus is considered a change event.
            //since there really is no change, this little hack will
            //get around it.
            if( evt.toString().compareTo( "[]" ) != 0 )
            {
                recordChange( evt );
            }
        }

        public void insertUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void removeUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void recordChange( DocumentEvent evt )
        {
            if( getRecordChanges() )
            {
                setNeedsSaving( true );
            }
        }
    }
}
