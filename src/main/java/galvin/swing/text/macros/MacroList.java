/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.text.macros;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "MacroList" )
@XmlAccessorType( XmlAccessType.FIELD )
public class MacroList
{
    private List<Macro> macros = new ArrayList();

    public List<Macro> getMacros()
    {
        return macros;
    }

    public void setMacros( List<Macro> macros )
    {
        this.macros = macros;
    }
}
