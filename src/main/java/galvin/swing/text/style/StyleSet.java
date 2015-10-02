package galvin.swing.text.style;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StyleSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class StyleSet
{
    private String name;
    private List<StyleInfo> styles;

    public StyleSet()
    {
        this( null, new ArrayList() );
    }

    public StyleSet( String name )
    {
        this( name, new ArrayList() );
    }

    public StyleSet( String name, List<StyleInfo> styles )
    {
        this.name = name;
        this.styles = styles;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public StyleInfo getStyle( String name )
    {
        for( StyleInfo styleInfo : getStyles() )
        {
            if( styleInfo.getLogicalName().equals( name ) )
            {
                return styleInfo;
            }
        }

        for( StyleInfo styleInfo : getStyles() )
        {
            if( styleInfo.getDisplayName().equals( name ) )
            {
                return styleInfo;
            }
        }

        return null;
    }

    public List<StyleInfo> getStyles()
    {
        List<StyleInfo> result = new ArrayList();
        result.addAll( styles );
        return result;
    }

    public void setStyles( List<StyleInfo> styles )
    {
        this.styles.clear();
        this.styles.addAll( styles );
    }

    public void addStyle( StyleInfo style )
    {
        styles.add( style );
    }

    public void removeStyle( StyleInfo style )
    {
        styles.remove( style );
    }

    public String getDisplayName( String logicalName )
    {
        for( StyleInfo style : getStyles() )
        {
            if( logicalName.equals( style.getLogicalName() ) )
            {
                return style.getDisplayName();
            }
        }

        return null;
    }

    public String getLogicalName( String displayName )
    {
        for( StyleInfo style : getStyles() )
        {
            if( displayName.equals( style.getDisplayName() ) )
            {
                return style.getLogicalName();
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return getName();
    }

}
