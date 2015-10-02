/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.text.macros;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "Macro" )
@XmlAccessorType( XmlAccessType.FIELD )
public class Macro
{
    public String addbreviation;
    public String beforeCursor;
    public String afterCursor;
    public MacroType type = MacroType.PLAIN_TEXT;

    public Macro()
    {
    }

    public Macro( String addbreviation, String beforeCursor, String afterCursor )
    {
        this.addbreviation = addbreviation;
        this.beforeCursor = beforeCursor;
        this.afterCursor = afterCursor;
    }

    public Macro( String addbreviation, String beforeCursor, String afterCursor, MacroType type )
    {
        this.addbreviation = addbreviation;
        this.beforeCursor = beforeCursor;
        this.afterCursor = afterCursor;
        this.type = type;
    }

    public String getAddbreviation()
    {
        return addbreviation;
    }

    public void setAddbreviation( String addbreviation )
    {
        this.addbreviation = addbreviation;
    }

    public String getAfterCursor()
    {
        return afterCursor;
    }

    public void setAfterCursor( String afterCursor )
    {
        this.afterCursor = afterCursor;
    }

    public String getBeforeCursor()
    {
        return beforeCursor;
    }

    public void setBeforeCursor( String beforeCursor )
    {
        this.beforeCursor = beforeCursor;
    }

    public MacroType getType()
    {
        return type;
    }

    public void setType( MacroType type )
    {
        this.type = type;
    }
    
    @Override
    public String toString()
    {
        return getAddbreviation();
    }
}
