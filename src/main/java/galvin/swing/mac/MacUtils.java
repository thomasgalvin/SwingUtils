package galvin.swing.mac;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MacUtils
{

    public static void useMacScreenMenuBar()
    {
        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
    }

    public static void registerApplicationListener( String className )
        throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
//            com.apple.eawt.Application macApplication = com.apple.eawt.Application.getApplication();
//            macApplication.addApplicationListener( new MarkdownApplicationAdapter() );

        Class appClass = Class.forName( "com.apple.eawt.Application" );
        Method[] allMethods = appClass.getDeclaredMethods();
        for(Method m : allMethods)
        {
            if( m.getName().equals( "addApplicationListener" ) )
            {
                Object macApplication = appClass.newInstance();
                m.setAccessible(true);
                
                Object adapter = Class.forName( className ).newInstance();
                m.invoke( macApplication, adapter );
            }
        }
    }
}
