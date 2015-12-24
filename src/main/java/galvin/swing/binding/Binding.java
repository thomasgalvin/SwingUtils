package galvin.swing.binding;

import java.lang.reflect.Field;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Binding
{
    private static final Logger logger = LoggerFactory.getLogger( Binding.class );
    
    private final JTextComponent text;
    private final Document document;
    private final Object source;
    private final String property;
    private final DocumentListener listener;
    private final Field field;

    public Binding( JTextComponent text, Object source, String property ) 
    throws NoSuchFieldException {
        Class clazz = source.getClass();
        field = clazz.getField( property );
        field.setAccessible( true );
        
        this.text = text;
        this.source = source;
        this.property = property;
        
        document = text.getDocument();
        
        listener = new LocalDocumentListener();
        document.addDocumentListener( listener );
    }
    
    public void toObject(){
        
    }
    
    public void toText(){
        try {
            Object value = field.get( text );
            if( value != null ){
                document.removeDocumentListener( listener );
                text.setText( value.toString() );
                document.addDocumentListener( listener );
            }
            else{
                text.setText( "" );
            }
        }
        catch( IllegalAccessException iae ){
            logger.error( "Error accessing field: " + field.getName(), iae );
        }
    }
    
    public void destroy(){
        document.removeDocumentListener( listener );
    }
    
    private class LocalDocumentListener implements DocumentListener{
        @Override
        public void insertUpdate( DocumentEvent e ) {
            toObject();
        }

        @Override
        public void removeUpdate( DocumentEvent e ) {
            toObject();
        }

        @Override
        public void changedUpdate( DocumentEvent e ) {
            toObject();
        }
    }
}
