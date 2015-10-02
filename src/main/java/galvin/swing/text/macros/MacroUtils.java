package galvin.swing.text.macros;

import galvin.swing.text.DocumentUtils;
import java.util.List;
import javax.swing.text.JTextComponent;

public class MacroUtils
{
    public static boolean expandMacro( JTextComponent textPane, List<MacroList> macroLists )
    {
        int wordStart = DocumentUtils.getWordStart( textPane.getDocument(), textPane.getCaretPosition() );
        int wordEnd = DocumentUtils.getWordEnd( textPane.getDocument(), textPane.getCaretPosition() );

        try
        {
            String currentWord = textPane.getDocument().getText( wordStart, wordEnd - wordStart );
            if( currentWord != null && !currentWord.trim().isEmpty() )
            {
                currentWord = currentWord.trim();
                for( MacroList macroList : macroLists )
                {
                    for( Macro macro : macroList.getMacros() )
                    {
                        String abb = macro.getAddbreviation().trim();
                        if( currentWord.equals( abb ) )
                        {
                            textPane.setCaretPosition( wordStart );
                            textPane.moveCaretPosition( wordEnd );
                            
                            String textAtCarret = textPane.getDocument().getText( wordEnd-1, 1 );
                            
                            String before = macro.getBeforeCursor();
                            String after = macro.getAfterCursor();
                            StringBuilder text = new StringBuilder( before.length() + after.length() + 1 );
                            text.append( before );
                            text.append( after );
                            
                            if( "\n".equals( textAtCarret) )
                            {
                                text.append( "\n" );
                            }

                            textPane.replaceSelection( text.toString() );
                            textPane.setCaretPosition( wordStart + before.length() );
                            
                            return true;
                        }
                    }
                }
            }
        }
        catch( Throwable ex )
        {
            ex.printStackTrace();
        }
        
        return false;
    }
}
