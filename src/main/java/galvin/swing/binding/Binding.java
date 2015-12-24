package galvin.swing.binding;

import galvin.StringUtils;
import java.lang.reflect.Field;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Binding
{
    private static final Logger logger = LoggerFactory.getLogger( Binding.class );
    
    private final JComponent component;
    private final Object object;
    private final String property;
    private final Field field;

    public Binding( JComponent component, Object object, String property ) 
    throws NoSuchFieldException {
        Class clazz = object.getClass();
        field = clazz.getDeclaredField( property );
        field.setAccessible( true );
        
        this.component = component;
        this.object = object;
        this.property = property;
    }
    
    public void setObjectValue(){
        
    }
    
    public void setComponentValue(){
        try {
            Object value = field.get( object );
            
            if( component instanceof JTextComponent ){
                toTextComponent( value );
            }
            else if( component instanceof JComboBox ){
                toComboBox( value );
            }
        }
        catch( IllegalAccessException iae ){
            logger.error( "Error accessing field: " + field.getName(), iae );
        }
    }
    
    private void toTextComponent( Object value ){
        JTextComponent text = (JTextComponent)component;
        if( value != null ) {
            boolean multiline = text instanceof JTextArea || text instanceof JEditorPane;
            
            if( value instanceof List && multiline ){
                List list = (List)value;
                
                StringBuilder builder = new StringBuilder();
                for( Object tmp : list ){
                    builder.append( tmp.toString() );
                    builder.append( "\n" );
                }
                
                text.setText( builder.toString().trim() );
            }
            else if( value instanceof List ){
                List list = (List)value;
                text.setText( StringUtils.csv( list ) );
            }
            else {
                text.setText( value.toString() );
            }
        }
        else {
            text.setText( "" );
        }
    }
    
    private void toComboBox( Object value ){
        JComboBox combo = (JComboBox)component;
        combo.setSelectedItem( value );
    }
}
