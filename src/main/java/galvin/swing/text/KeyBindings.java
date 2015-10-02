package galvin.swing.text;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class KeyBindings
{
  public static final int PREFERED_MODIFIER_KEY;
  public static final int SECONDARY_MODIFIER_KEY;
  static
  {
    if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1)
    {
      PREFERED_MODIFIER_KEY = ActionEvent.META_MASK;
      SECONDARY_MODIFIER_KEY = ActionEvent.ALT_MASK;
    }
    else
    {
      PREFERED_MODIFIER_KEY = ActionEvent.CTRL_MASK;
      SECONDARY_MODIFIER_KEY = ActionEvent.ALT_MASK;
    }
  }
  
  ///////
  // File
  ///////
  
  public static final int fileMnemonic = KeyEvent.VK_F;
  
  public static final KeyStroke newKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_N, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke openKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_O, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke saveKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_S, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke closeKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_W, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke printKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_P, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke exitKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_Q, PREFERED_MODIFIER_KEY);
    
  ///////
  // Edit
  ///////
  
  public static final int editMnemonic = KeyEvent.VK_E;
  
  public static final KeyStroke undoKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_Z, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke redoKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_Y, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke cutKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_X, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke copyKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_C, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke pasteKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_V, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke findKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_F, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke findNextKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_G, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke findPreviousKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_G, SECONDARY_MODIFIER_KEY);
    
  public static final KeyStroke toggleOverwriteKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0);
    
  public static final KeyStroke toggleUpperCaseKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_U, SECONDARY_MODIFIER_KEY);
    
  ////////
  // Style
  ////////
  
  public static final KeyStroke toggleBoldKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_B, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke toggleItalicKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_I, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke toggleUnderlineKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_U, PREFERED_MODIFIER_KEY);
    
  /////////
  // Syntax
  /////////
  
  public static final int syntaxMnemonic = KeyEvent.VK_S;
  
  public static final KeyStroke goToMatchingCharKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_CLOSE_BRACKET, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke selectToMatchingCharKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, PREFERED_MODIFIER_KEY);
  
  //////////
  // Options
  //////////
  
  public static final int optionsMnemonic = KeyEvent.VK_O;
  
  ///////
  // Misc
  ///////
  
  public static final KeyStroke newlineKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
    
  public static final KeyStroke homeKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0);
    
  public static final KeyStroke selectHomeKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_HOME, ActionEvent.SHIFT_MASK);
    
  public static final KeyStroke endKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_END, 0);
    
  public static final KeyStroke selectEndKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_END, ActionEvent.SHIFT_MASK);
    
  public static final KeyStroke wordStartKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke selectWordStartKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK);
    
  public static final KeyStroke wordEndKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, PREFERED_MODIFIER_KEY);
    
  public static final KeyStroke selectWordEndKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK);
    
  public static final KeyStroke deleteCurrentLineKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_D, PREFERED_MODIFIER_KEY );

  public static final KeyStroke expandMacroKeyStroke =
    KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, PREFERED_MODIFIER_KEY );
}