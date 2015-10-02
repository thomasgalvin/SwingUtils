package galvin.swing.editor;

import java.awt.Toolkit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * DocumentUndoManager.
 */
public class DocumentUndoManager
        extends UndoManager
{
    private boolean recordChanges = true;

    public void setRecordChanges( boolean recordChanges )
    {
        this.recordChanges = recordChanges;
    }

    public boolean getRecordChanges()
    {
        return recordChanges;
    }

    /**
    Overridden to allow the edits to be optionally ignored.
     */
    public boolean addEdit( UndoableEdit anEdit )
    {
        if( getRecordChanges() )
        {
            return super.addEdit( anEdit );
        }
        else
        {
            return true;
        }
    }

    /**
    If possible, undoes the last action, otherwise emits a beep.
     */
    public void undo()
    {
        if( canUndo() )
        {
            super.undo();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
    If possible, redoes the last action, otherwise emits a beep.
     */
    public void redo()
    {
        if( canRedo() )
        {
            super.redo();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
