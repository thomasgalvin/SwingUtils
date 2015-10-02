package galvin.swing.editor;

import galvin.swing.text.DocumentUtils;
import galvin.swing.text.TextControlUtils;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

public class TextActions {
    /////////////////
    // EditorAction
    /////////////////

    /**
     * All of the actions in Editor have to poll the event passed to
     * ActionPreformed() to find out what Editor is responsible for the event;
     * this class implements that code, provides a convenience getEditor()
     * method to find the result, and has an abstract doAction() method that is
     * called from ActionPreformed(). All child classes have to do is implement
     * doAction() and call getEditor().
     */
    public static abstract class EditorAction
        extends TextAction {

        protected Editor editor;

        public EditorAction( String name ) {
            super( name );
        }

        public void actionPerformed( ActionEvent evt ) {
            Object target = evt.getSource();
            if( target instanceof Editor ) {
                editor = (Editor) target;
                doAction();
                editor.requestFocus();
            }
        }

        public Editor getEditor() {
            return editor;
        }

        /**
         * Child classes should implement this with a call to getEditor() to
         * find the component responsible for the event.
         */
        protected abstract void doAction();
    }

    //////////
    // Newline
    //////////
    public static class NewlineAction
        extends EditorAction {

        public static final String actionName = "newline-action";

        public NewlineAction() {
            super( actionName );
        }

        public void doAction() {
            Editor editor = getEditor();
            if( editor.getFloatingNewline() ) {
                Document document = editor.getDocument();

                int caretPosition = editor.getCaretPosition();
                int line = DocumentUtils.getCaretLine( document, caretPosition );
                int lineStart = DocumentUtils.getLineStartOffset( document, line );
                int relativeCaretPosition = caretPosition - lineStart;

                String text = DocumentUtils.getLineText( document, line );
                int len = text.length();

                //System.out.println( "line:    [" + text + "]" );
                StringBuilder builder = new StringBuilder( "\n" );
                for( int i = 0; i < relativeCaretPosition && i < len; i++ ) {
                    char c = text.charAt( i );
                    //System.out.println( "    testing: '" + c + "'" );
                    if( Character.isWhitespace( c ) && '\n' != c ) {
                        //System.out.println( "        appending '" + c + "'to hanging indent" );
                        builder.append( c );
                    }
                    else {
                        break;
                    }
                }
                //System.out.println( "front:   [" + builder.toString() + "]" );

                editor.replaceSelection( builder.toString() );
            }
            else {
                editor.replaceSelection( "\n" );
            }
        }
    }

    ////////////
    // Undo/Redo
    ////////////
    /**
     * A text action that allows keyboard bindings for the undo command.
     */
    public static class UndoAction
        extends EditorAction {

        public static final String actionName = "undo-action";

        public UndoAction() {
            super( actionName );
        }

        public void doAction() {
            getEditor().undo();
        }
    }

    /**
     * A text action that allows keyboard bindings for the redo command.
     */
    public static class RedoAction
        extends EditorAction {

        public static final String actionName = "redo-action";

        public RedoAction() {
            super( actionName );
        }

        public void doAction() {
            getEditor().redo();
        }
    }

    /////////////////
    // Cut/Copy/Paste
    /////////////////
    public static class CutAction
        extends EditorAction {

        public static final String actionName = "cut-action";

        public CutAction() {
            super( actionName );
        }

        public void doAction() {
            getEditor().cut();
        }
    }

    public static class CopyAction
        extends EditorAction {

        public static final String actionName = "copy-action";

        public CopyAction() {
            super( actionName );
        }

        public void doAction() {
            getEditor().copy();
        }
    }

    public static class PasteAction
        extends EditorAction {

        public static final String actionName = "paste-action";

        public PasteAction() {
            super( actionName );
        }

        public void doAction() {
            getEditor().paste();
        }
    }

    /////////////////////////
    // Current Word Functions
    /////////////////////////
    public static class WordStartAction
        extends EditorAction {

        public static final String actionName = "move-caret-word-start-action";
        private boolean select;

        public WordStartAction( boolean select ) {
            super( actionName );
            this.select = select;
        }

        public void doAction() {
            Editor editor = getEditor();
            int caretPos = editor.getCaretPosition();
            Document document = editor.getDocument();
            int newPos = DocumentUtils.getWordStart( document, caretPos );

            if( newPos != 0 && newPos == caretPos ) {
                caretPos--;
                newPos = DocumentUtils.getWordStart( document, caretPos );
            }

            if( select ) {
                editor.moveCaretPosition( newPos );
            }
            else {
                editor.setCaretPosition( newPos );
            }
        }
    }

    public static class WordEndAction
        extends EditorAction {

        public static final String actionName = "move-caret-word-end-action";
        private boolean select;

        public WordEndAction( boolean select ) {
            super( actionName );
            this.select = select;
        }

        public void doAction() {
            Editor editor = getEditor();
            int caretPos = editor.getCaretPosition();
            Document document = editor.getDocument();
            int newPos = DocumentUtils.getWordEnd( document, caretPos );

            if( newPos != editor.getDocument().getLength() && newPos == caretPos ) {
                caretPos++;
                newPos = DocumentUtils.getWordStart( document, caretPos );
            }

            if( select ) {
                editor.moveCaretPosition( newPos );
            }
            else {
                editor.setCaretPosition( newPos );
            }
        }
    }

    ////////////////////
    // Home/End Movement
    ////////////////////
    public static class HomeAction
        extends EditorAction {

        public static final String actionName = "move-caret-home-action";
        private boolean select;

        public HomeAction( boolean select ) {
            super( actionName );
            this.select = select;
        }

        public void doAction() {
            Editor tp = getEditor();
            int caret = tp.getCaretPosition();
            Document document = tp.getDocument();

            if( caret == 0 ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            int newCaret = caret;
            int currentLine = DocumentUtils.getCaretLine( document, caret );
            int currentLineStart = DocumentUtils.getLineStartOffset( document, currentLine );

            if( tp.getSmartHomeEndKeys() ) {
                int currentLineStartNoWhitespace = DocumentUtils.getLineStartOffsetNoWhitespace( document, currentLine );
                if( caret > currentLineStartNoWhitespace ) {
                    newCaret = currentLineStartNoWhitespace;
                }
                else {
                    newCaret = currentLineStart;
                }
            }
            else {
                newCaret = currentLineStart;
            }

            if( select ) {
                tp.moveCaretPosition( newCaret );
            }
            else {
                tp.setCaretPosition( newCaret );
            }
        }
    }

    public static class EndAction
        extends EditorAction {

        public static final String actionName = "move-caret-end-action";
        private boolean select;

        public EndAction( boolean select ) {
            super( actionName );
            this.select = select;
        }

        public void doAction() {
            Editor tp = getEditor();
            int caret = tp.getCaretPosition();
            Document document = tp.getDocument();

            if( caret == document.getLength() ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            int newCaret = caret;
            int currentLine = DocumentUtils.getCaretLine( document, caret );
            int currentLineEnd = DocumentUtils.getLineEndOffset( document, currentLine )
                                 - 1;

            if( tp.getSmartHomeEndKeys() ) {
                int currentLineEndNoWhitespace = DocumentUtils.getLineEndOffsetNoWhitespace( document, currentLine );
                if( caret < currentLineEndNoWhitespace ) {
                    newCaret = currentLineEndNoWhitespace;
                }
                else {
                    newCaret = currentLineEnd;
                }
            }
            else {
                newCaret = currentLineEnd;
            }

            if( select ) {
                tp.moveCaretPosition( newCaret );
            }
            else {
                tp.setCaretPosition( newCaret );
            }
        }
    }

    ///////////////////
    // Bracket Matching
    ///////////////////
    /**
     * A text action that allows keyboard bindings for the goToMatchingChar
     * command.
     */
    public static class GoToMatchingCharAction
        extends EditorAction {

        public static final String actionName = "go-to-matching-char-action";

        public GoToMatchingCharAction() {
            super( actionName );
        }

        protected void doAction() {
            Editor editor = getEditor();
            if( editor instanceof JTextComponent ) {
                JTextComponent textComponent = (JTextComponent) editor;
                TextControlUtils.goToMatchingChar( textComponent, false );
            }
        }
    }

    /**
     * A text action that allows keyboard bindings for the goToMatchingChar
     * command.
     */
    public static class SelectToMatchingCharAction
        extends EditorAction {

        public static final String actionName = "select-to-matching-char-action";

        public SelectToMatchingCharAction() {
            super( actionName );
        }

        protected void doAction() {
            if( editor instanceof JTextComponent ) {
                JTextComponent textComponent = (JTextComponent) editor;
                TextControlUtils.goToMatchingChar( textComponent, true );
            }
        }
    }

    public static class ToggleOverwriteAction
        extends EditorAction {

        public static final String actionName = "toggle-overwrite-action";

        public ToggleOverwriteAction() {
            super( actionName );
        }

        protected void doAction() {
            getEditor().toggleOverwriting();
            getEditor().selectCaret();
        }
    }

    //////////////////////
    // Uppercase/Lowercase
    //////////////////////
    /**
     * A text action that allows keyboard bindings for the
     * toggleSelectionUpperCase command.
     */
    public static class ToggleUpperCaseAction
        extends EditorAction {

        public static final String actionName = "toggle-upper-case-action";

        public ToggleUpperCaseAction() {
            super( actionName );
        }

        protected void doAction() {
            if( editor instanceof JTextComponent ) {
                JTextComponent textComponent = (JTextComponent) editor;
                TextControlUtils.toggleSelectionUpperCase( textComponent );
            }
        }
    }

    //////////
    // Styles
    //////////
    /**
     * A text action that allows keyboard bindings for the toggleBold command.
     */
    public static class ToggleBoldAction
        extends EditorAction {

        public static final String actionName = "toggle-bold-action";

        public ToggleBoldAction() {
            super( actionName );
        }

        protected void doAction() {
            Editor editor = getEditor();
            if( editor instanceof Editor ) {
                Editor theEditor = (Editor) editor;
                if( theEditor.getStylesAllowed() ) {
                    TextControlUtils.toggleSelectionBold( (JTextPane) editor );
                }
            }
            else if( editor instanceof JTextPane ) {
                TextControlUtils.toggleSelectionBold( (JTextPane) editor );
            }
        }
    }

    /**
     * A text action that allows keyboard bindings for the toggleItalic command.
     */
    public static class ToggleItalicAction
        extends EditorAction {

        public static final String actionName = "toggle-italic-action";

        public ToggleItalicAction() {
            super( actionName );
        }

        protected void doAction() {
            Editor editor = getEditor();
            if( editor instanceof Editor ) {
                Editor theEditor = (Editor) editor;
                if( theEditor.getStylesAllowed() ) {
                    TextControlUtils.toggleSelectionBold( (JTextPane) editor );
                }
            }
            else if( editor instanceof JTextPane ) {
                TextControlUtils.toggleSelectionItalic( (JTextPane) editor );
            }
        }
    }

    /**
     * A text action that allows keyboard bindings for the toggleUnderline
     * command.
     */
    public static class ToggleUnderlineAction
        extends EditorAction {

        public static final String actionName = "toggle-underline-action";

        public ToggleUnderlineAction() {
            super( actionName );
        }

        protected void doAction() {
            Editor editor = getEditor();
            if( editor instanceof Editor ) {
                Editor theEditor = (Editor) editor;
                if( theEditor.getStylesAllowed() ) {
                    TextControlUtils.toggleSelectionBold( (JTextPane) editor );
                }
            }
            else if( editor instanceof JTextPane ) {
                TextControlUtils.toggleSelectionUnderline( (JTextPane) editor );
            }
        }
    }

    //////////////////////
    // delete current line
    //////////////////////
    /**
     * A text action that allows keyboard bindings for the delete line.
     */
    public static class DeleteCurrentLineAction
        extends EditorAction {

        public static final String actionName = "delete-current-line-action";

        public DeleteCurrentLineAction() {
            super( actionName );
        }

        protected void doAction() {
            if( editor instanceof JTextComponent ) {
                JTextComponent textComponent = (JTextComponent) editor;
                TextControlUtils.deleteCurrentLine( textComponent );
            }
        }
    }
}
