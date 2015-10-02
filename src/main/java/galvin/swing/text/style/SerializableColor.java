package galvin.swing.text.style;


import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Color")
@XmlAccessorType(XmlAccessType.FIELD)
public class SerializableColor
{
    private int red;
    private int green;
    private int blue;
    private int alpha;

    public SerializableColor()
    {
        this( 0, 0, 0, 0 );
    }

    public SerializableColor( int red, int green, int blue, int alpha )
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public SerializableColor( Color color )
    {
        this( color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() );
    }

    public int getAlpha()
    {
        return alpha;
    }

    public void setAlpha( int alpha )
    {
        this.alpha = alpha;
    }

    public int getBlue()
    {
        return blue;
    }

    public void setBlue( int blue )
    {
        this.blue = blue;
    }

    public int getGreen()
    {
        return green;
    }

    public void setGreen( int green )
    {
        this.green = green;
    }

    public int getRed()
    {
        return red;
    }

    public void setRed( int red )
    {
        this.red = red;
    }

    public Color getColor()
    {
        return new Color( red, green, blue, alpha );
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final SerializableColor other = ( SerializableColor ) obj;
        if( this.red != other.red )
        {
            return false;
        }
        if( this.green != other.green )
        {
            return false;
        }
        if( this.blue != other.blue )
        {
            return false;
        }
        if( this.alpha != other.alpha )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + this.red;
        hash = 53 * hash + this.green;
        hash = 53 * hash + this.blue;
        hash = 53 * hash + this.alpha;
        return hash;
    }

    @Override
    public String toString()
    {
        return "SerializableColor{" + " red=" + red + " green=" + green + " blue=" +
               blue + " alpha=" + alpha + " }";
    }

}
