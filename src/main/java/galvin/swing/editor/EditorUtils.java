package galvin.swing.editor;

import galvin.swing.text.KeyBindings;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.Keymap;

public class EditorUtils
{

    public static void buildKeyMap( JTextPane textPane )
    {
        Keymap keymap = textPane.getKeymap();

        //basic functions

        keymap.removeKeyStrokeBinding( KeyBindings.newlineKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.newlineKeyStroke, new TextActions.NewlineAction() );

        //editing functions

        keymap.removeKeyStrokeBinding( KeyBindings.undoKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.undoKeyStroke, new TextActions.UndoAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.redoKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.redoKeyStroke, new TextActions.RedoAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.cutKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.cutKeyStroke, new TextActions.CutAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.copyKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.copyKeyStroke, new TextActions.CopyAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.pasteKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.pasteKeyStroke, new TextActions.PasteAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleOverwriteKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleOverwriteKeyStroke, new TextActions.ToggleOverwriteAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleUpperCaseKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleUpperCaseKeyStroke, new TextActions.ToggleUpperCaseAction() );

        //movement, etc

        keymap.removeKeyStrokeBinding( KeyBindings.goToMatchingCharKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.goToMatchingCharKeyStroke, new TextActions.GoToMatchingCharAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.selectToMatchingCharKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectToMatchingCharKeyStroke, new TextActions.SelectToMatchingCharAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.homeKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.homeKeyStroke, new TextActions.HomeAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectHomeKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectHomeKeyStroke, new TextActions.HomeAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.endKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.endKeyStroke, new TextActions.EndAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectEndKeyStroke, new TextActions.EndAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.wordStartKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.wordStartKeyStroke, new TextActions.WordStartAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectWordStartKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectWordStartKeyStroke, new TextActions.WordStartAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.wordEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.wordEndKeyStroke, new TextActions.WordEndAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectWordEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectWordEndKeyStroke, new TextActions.WordEndAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.deleteCurrentLineKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.deleteCurrentLineKeyStroke, new TextActions.DeleteCurrentLineAction() );
    }
    
    public static void buildStylesKeyMap( JTextPane textPane )
    {
        Keymap keymap = textPane.getKeymap();
        
        keymap.removeKeyStrokeBinding( KeyBindings.toggleBoldKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleBoldKeyStroke, new TextActions.ToggleBoldAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleItalicKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleItalicKeyStroke, new TextActions.ToggleItalicAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleUnderlineKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleUnderlineKeyStroke, new TextActions.ToggleUnderlineAction() );
    }

    public static void buildKeyMap( JTextField textField )
    {
        Keymap keymap = textField.getKeymap();

        //editing functions

        keymap.removeKeyStrokeBinding( KeyBindings.undoKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.undoKeyStroke, new TextActions.UndoAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.redoKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.redoKeyStroke, new TextActions.RedoAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.cutKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.cutKeyStroke, new TextActions.CutAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.copyKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.copyKeyStroke, new TextActions.CopyAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.pasteKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.pasteKeyStroke, new TextActions.PasteAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleOverwriteKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleOverwriteKeyStroke, new TextActions.ToggleOverwriteAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleUpperCaseKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleUpperCaseKeyStroke, new TextActions.ToggleUpperCaseAction() );

        //movement, etc

        keymap.removeKeyStrokeBinding( KeyBindings.goToMatchingCharKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.goToMatchingCharKeyStroke, new TextActions.GoToMatchingCharAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.selectToMatchingCharKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectToMatchingCharKeyStroke, new TextActions.SelectToMatchingCharAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.homeKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.homeKeyStroke, new TextActions.HomeAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectHomeKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectHomeKeyStroke, new TextActions.HomeAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.endKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.endKeyStroke, new TextActions.EndAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectEndKeyStroke, new TextActions.EndAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.wordStartKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.wordStartKeyStroke, new TextActions.WordStartAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectWordStartKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectWordStartKeyStroke, new TextActions.WordStartAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.wordEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.wordEndKeyStroke, new TextActions.WordEndAction( false ) );

        keymap.removeKeyStrokeBinding( KeyBindings.selectWordEndKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.selectWordEndKeyStroke, new TextActions.WordEndAction( true ) );

        keymap.removeKeyStrokeBinding( KeyBindings.deleteCurrentLineKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.deleteCurrentLineKeyStroke, new TextActions.DeleteCurrentLineAction() );
    }
}