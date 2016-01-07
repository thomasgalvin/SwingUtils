package galvin.swing.binding;

import galvin.StringUtils;
import galvin.swing.SimpleDateWidget;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
        try {
            Object value = null;
            
            if( component instanceof JTextComponent ){
                value = fromTextComponent();
            }
            else if( component instanceof JComboBox ){
                value = fromComboBox();
            }
            else if( component instanceof SimpleDateWidget ){
                value = fromDateWidget();
            }
            else {
                value = fromGenericSetValue();
            }
            
            if( value != null ){
                Class fieldClass = field.getType();
                Class valueClass = value.getClass();
                
                
                if( fieldClass.isAssignableFrom( valueClass) ){
                    field.set( object, value );
                } 
                else {
                    convertAndSet( value, fieldClass, valueClass );
                }
            }
            else{
                field.set( object, value );
            }
        }
        catch( IllegalAccessException iae ){
            logger.error( "Error accessing field: " + field.getName(), iae );
        }
    }
    
    private void convertAndSet( Object value, Class fieldClass, Class valueClass ) throws IllegalAccessException {
        if( valueClass.getCanonicalName().equals( "org.joda.time.LocalDateTime" ) ){
            if( fieldClass.getCanonicalName().equals( "long" ) ){
                LocalDateTime date = (LocalDateTime)value;
                long val = date.toDate().getTime();
                field.set( object, val );
            }
        }
        else if( valueClass.getCanonicalName().equals( "org.joda.time.LocalDate" ) ){
            if( fieldClass.getCanonicalName().equals( "long" ) ){
                LocalDate date = (LocalDate)value;
                long val = date.toDate().getTime();
                field.set( object, val );
            }
        }
        else if( valueClass.getCanonicalName().equals( "java.util.Date" ) ){
            if( fieldClass.getCanonicalName().equals( "long" ) ){
                Date date = (Date)value;
                long val = date.getTime();
                field.set( object, val );
            }
        }
        else if( valueClass.isAssignableFrom( Calendar.class ) ){
            if( fieldClass.getCanonicalName().equals( "long" ) ){
                Calendar date = (Calendar)value;
                long val = date.getTimeInMillis();
                field.set( object, val );
            }
        }
        else if( valueClass.getCanonicalName().equals( "java.lang.String" ) ){
            if( fieldClass.isAssignableFrom( List.class ) ){
                JTextComponent text = (JTextComponent)component;
                boolean multiline = text instanceof JTextArea || text instanceof JEditorPane;
                
                String stringVal = (String)value;
                String[] tokens = null;
                
                if( multiline ){
                    tokens = stringVal.split( "\n" );
                }
                else {
                    tokens = stringVal.split( "," );
                }
                
                List list = new ArrayList();
                for( String string : tokens ){
                    list.add(  string.trim() );
                }
                
                field.set( object, list );
            }
        }
        else {
            field.set( object, customConversionFromComponent( value ) );
        }
    }
    
    public Object customConversionFromComponent( Object componentValue ){
        return componentValue;
    }
    
    private Object fromTextComponent(){
        JTextComponent text = (JTextComponent)component;
        String value = text.getText();
        return value;
    }
    
    private Object fromComboBox(){
        JComboBox combo = (JComboBox)component;
        Object value = combo.getSelectedItem();
        return value;
    }
    
    private Object fromDateWidget(){
        SimpleDateWidget dateWidget = (SimpleDateWidget)component;
        LocalDateTime value = dateWidget.getDateTime();
        return value;
    }
    
    private Object fromGenericSetValue(){
        Object value = null;
        
        try{
            Class clazz = component.getClass();
            
            Field[] fields = clazz.getDeclaredFields();
            for( Field componentField : fields ){
                if( componentField.getName().equals( "value" ) ){
                    componentField.setAccessible( true );
                    value = componentField.get( component );
                }
            }
            
            if( value == null ){
                Method[] methods = clazz.getDeclaredMethods();
                for( Method method : methods ){
                    if( method.getName().equals( "getValue" ) && method.getParameterCount() == 1){
                        method.setAccessible( true );
                        value = method.invoke( component );
                    }
                }
            }
        }
        catch( Throwable t){
            logger.error( "Error setting value", t );
        }
        
        return value;
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
            else {
                toGenericSetValue( value );
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
    
    private void toGenericSetValue( Object value ){
        try{
            Class clazz = component.getClass();
            
            Field[] fields = clazz.getDeclaredFields();
            for( Field componentField : fields ){
                if( componentField.getName().equals( "value" ) ){
                    componentField.setAccessible( true );
                    componentField.set( component, value );
                    return;
                }
            }
            
            Method[] methods = clazz.getDeclaredMethods();
            for( Method method : methods ){
                if( method.getName().equals( "setValue" ) && method.getParameterCount() == 1){
                    method.setAccessible( true );
                    method.invoke( component, value );
                }
            }
        }
        catch( Throwable t){
            logger.error( "Error setting value", t );
        }
    }
}
