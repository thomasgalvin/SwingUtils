package galvin.swing.binding;

import galvin.StringUtils;
import galvin.swing.SimpleDateWidget;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
            else if( component instanceof SimpleDateWidget ){
                toDateWidget( value );
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
    
    private void toDateWidget( Object value ){
        SimpleDateWidget dateWidget = (SimpleDateWidget)component;
        if( value instanceof LocalDate ) {
            LocalDate date = (LocalDate)value;
            dateWidget.setDate( date );
        }
        else if( value instanceof LocalDateTime ) {
            LocalDateTime date = (LocalDateTime)value;
            dateWidget.setDateTime( date );
        }
        else if( value instanceof Date ){
            Date date = (Date)value;
            LocalDate local = new LocalDate( date.getTime() );
            dateWidget.setDate( local );
        }
        else if( value instanceof Calendar ){
            Calendar calendar = (Calendar)value;
            LocalDateTime local = new LocalDateTime( calendar.getTimeInMillis() );
            dateWidget.setDateTime( local );
        }
        else if( value instanceof Long ){
            Long time = (Long)value;
            LocalDateTime local = new LocalDateTime( time );
            dateWidget.setDateTime( local );
        }
        else {
            dateWidget.epoc();
        }
    }
}
