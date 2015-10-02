package galvin.swing.editor;

import javax.swing.text.Document;

public interface Editable
{
    public Document getDocument();
    public int getCaretPosition();
    public void moveCaretPosition( int pos );
    public void setCaretPosition( int pos );
    public boolean getFloatingNewline();
    public boolean getSmartHomeEndKeys();
    public void selectCaret();
    public void replaceSelection( String text );
    public void undo();
    public void redo();
    public void cut();
    public void copy();
    public void paste();
    public void pasteRichText( Document document );
    public void requestFocus();
}