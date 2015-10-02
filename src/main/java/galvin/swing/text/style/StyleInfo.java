package galvin.swing.text.style;

import java.awt.Color;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "StyleInfo" )
@XmlAccessorType( XmlAccessType.FIELD )
public class StyleInfo
{

    private transient Style style;
    private String logicalName;
    private String displayName;
    private String fontName = "Arial";
    private Integer fontSize = new Integer( 12 );
    private Integer alignment = StyleConstants.ALIGN_JUSTIFIED;
    private SerializableColor foreground;
    private SerializableColor background;
    private Boolean bold = Boolean.FALSE;
    private Boolean italic = Boolean.FALSE;
    private Boolean underline = Boolean.FALSE;
    private Boolean strikethrough = Boolean.FALSE;
    private Boolean superscript = Boolean.FALSE;
    private Boolean subscript = Boolean.FALSE;
    private Float lineSpacing = new Float( 0 );
    private Float spaceAbove = new Float( 0 );
    private Float spaceBelow = new Float( 0 );
    private Float firstLineIndent = new Float( 0 );
    private Float leftIndent = new Float( 0 );
    private Float rightIndent = new Float( 0 );
    private Integer bidiLevel = new Integer( 0 );
    private String hyperlink;

    public StyleInfo()
    {
        this( null, null );
    }

    public StyleInfo( String logicalName, String displayName )
    {
        this.logicalName = logicalName;
        this.displayName = displayName;
    }

    public Style getStyle()
    {
        if( style == null )
        {
            style = StyleUtils.createStyle( logicalName );
        }

            StyleUtils.annotateStyle( style,
                                      getFontName(), getFontSize(), getAlignment(),
                                      getForeground(), getBackground(),
                                      getBold(), getItalic(), getUnderline(), getStrikethrough(), getSuperscript(), getSuperscript(),
                                      getLineSpacing(), getSpaceAbove(), getSpaceBelow(),
                                      getFirstLineIndent(), getLeftIndent(), getRightIndent(),
                                      getBidiLevel(), getHyperlink() );
        return style;
    }

    public boolean containsAttributes( StyleInfo styleInfo )
    {
        return getStyle().containsAttributes( styleInfo.getStyle() );
    }
    
    public Integer getAlignment()
    {
        return alignment;
    }

    public void setAlignment( Integer alignment )
    {
        this.alignment = alignment;
    }

    public Color getBackground()
    {
        if( background != null )
        {
            return background.getColor();
        }
        return null;
    }

    public void setBackground( Color background )
    {
        this.background = new SerializableColor( background );
    }

    public Integer getBidiLevel()
    {
        return bidiLevel;
    }

    public void setBidiLevel( Integer bidiLevel )
    {
        this.bidiLevel = bidiLevel;
    }

    public Boolean getBold()
    {
        return bold;
    }

    public void setBold( Boolean bold )
    {
        this.bold = bold;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public Float getFirstLineIndent()
    {
        return firstLineIndent;
    }

    public void setFirstLineIndent( Float firstLineIndent )
    {
        this.firstLineIndent = firstLineIndent;
    }

    public String getFontName()
    {
        return fontName;
    }

    public void setFontName( String fontName )
    {
        this.fontName = fontName;
    }

    public Integer getFontSize()
    {
        return fontSize;
    }

    public void setFontSize( Integer fontSize )
    {
        this.fontSize = fontSize;
    }

    public Color getForeground()
    {
        if( foreground != null )
        {
            return foreground.getColor();
        }
        return null;
    }

    public void setForeground( Color foreground )
    {
        this.foreground = new SerializableColor( foreground );
    }

    public String getHyperlink()
    {
        return hyperlink;
    }

    public void setHyperlink( String hyperlink )
    {
        this.hyperlink = hyperlink;
    }

    public Boolean getItalic()
    {
        return italic;
    }

    public void setItalic( Boolean italic )
    {
        this.italic = italic;
    }

    public Float getLeftIndent()
    {
        return leftIndent;
    }

    public void setLeftIndent( Float leftIndent )
    {
        this.leftIndent = leftIndent;
    }

    public Float getLineSpacing()
    {
        return lineSpacing;
    }

    public void setLineSpacing( Float lineSpacing )
    {
        this.lineSpacing = lineSpacing;
    }

    public String getLogicalName()
    {
        return logicalName;
    }

    public void setLogicalName( String logicalName )
    {
        this.logicalName = logicalName;
    }

    public Float getRightIndent()
    {
        return rightIndent;
    }

    public void setRightIndent( Float rightIndent )
    {
        this.rightIndent = rightIndent;
    }

    public Float getSpaceAbove()
    {
        return spaceAbove;
    }

    public void setSpaceAbove( Float spaceAbove )
    {
        this.spaceAbove = spaceAbove;
    }

    public Float getSpaceBelow()
    {
        return spaceBelow;
    }

    public void setSpaceBelow( Float spaceBelow )
    {
        this.spaceBelow = spaceBelow;
    }

    public Boolean getStrikethrough()
    {
        return strikethrough;
    }

    public void setStrikethrough( Boolean strikethrough )
    {
        this.strikethrough = strikethrough;
    }

    public Boolean getSubscript()
    {
        return subscript;
    }

    public void setSubscript( Boolean subscript )
    {
        this.subscript = subscript;
    }

    public Boolean getSuperscript()
    {
        return superscript;
    }

    public void setSuperscript( Boolean superscript )
    {
        this.superscript = superscript;
    }

    public Boolean getUnderline()
    {
        return underline;
    }

    public void setUnderline( Boolean underline )
    {
        this.underline = underline;
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
        final StyleInfo other = ( StyleInfo ) obj;
        if( ( this.logicalName == null ) ? ( other.logicalName != null ) : !this.logicalName.equals( other.logicalName ) )
        {
            return false;
        }
        if( ( this.displayName == null ) ? ( other.displayName != null ) : !this.displayName.equals( other.displayName ) )
        {
            return false;
        }
        if( ( this.fontName == null ) ? ( other.fontName != null ) : !this.fontName.equals( other.fontName ) )
        {
            return false;
        }
        if( this.fontSize != other.fontSize &&
            ( this.fontSize == null || !this.fontSize.equals( other.fontSize ) ) )
        {
            return false;
        }
        if( this.alignment != other.alignment &&
            ( this.alignment == null || !this.alignment.equals( other.alignment ) ) )
        {
            return false;
        }
        if( this.foreground != other.foreground &&
            ( this.foreground == null ||
              !this.foreground.equals( other.foreground ) ) )
        {
            return false;
        }
        if( this.background != other.background &&
            ( this.background == null ||
              !this.background.equals( other.background ) ) )
        {
            return false;
        }
        if( this.bold != other.bold && ( this.bold == null || !this.bold.equals( other.bold ) ) )
        {
            return false;
        }
        if( this.italic != other.italic &&
            ( this.italic == null || !this.italic.equals( other.italic ) ) )
        {
            return false;
        }
        if( this.underline != other.underline &&
            ( this.underline == null || !this.underline.equals( other.underline ) ) )
        {
            return false;
        }
        if( this.strikethrough != other.strikethrough &&
            ( this.strikethrough == null ||
              !this.strikethrough.equals( other.strikethrough ) ) )
        {
            return false;
        }
        if( this.superscript != other.superscript &&
            ( this.superscript == null ||
              !this.superscript.equals( other.superscript ) ) )
        {
            return false;
        }
        if( this.subscript != other.subscript &&
            ( this.subscript == null || !this.subscript.equals( other.subscript ) ) )
        {
            return false;
        }
        if( this.lineSpacing != other.lineSpacing &&
            ( this.lineSpacing == null ||
              !this.lineSpacing.equals( other.lineSpacing ) ) )
        {
            return false;
        }
        if( this.spaceAbove != other.spaceAbove &&
            ( this.spaceAbove == null ||
              !this.spaceAbove.equals( other.spaceAbove ) ) )
        {
            return false;
        }
        if( this.spaceBelow != other.spaceBelow &&
            ( this.spaceBelow == null ||
              !this.spaceBelow.equals( other.spaceBelow ) ) )
        {
            return false;
        }
        if( this.firstLineIndent != other.firstLineIndent &&
            ( this.firstLineIndent == null ||
              !this.firstLineIndent.equals( other.firstLineIndent ) ) )
        {
            return false;
        }
        if( this.leftIndent != other.leftIndent &&
            ( this.leftIndent == null ||
              !this.leftIndent.equals( other.leftIndent ) ) )
        {
            return false;
        }
        if( this.rightIndent != other.rightIndent &&
            ( this.rightIndent == null ||
              !this.rightIndent.equals( other.rightIndent ) ) )
        {
            return false;
        }
        if( this.bidiLevel != other.bidiLevel &&
            ( this.bidiLevel == null || !this.bidiLevel.equals( other.bidiLevel ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 83 * hash +
               ( this.logicalName != null ? this.logicalName.hashCode() : 0 );
        hash = 83 * hash +
               ( this.displayName != null ? this.displayName.hashCode() : 0 );
        hash = 83 * hash + ( this.fontName != null ? this.fontName.hashCode() : 0 );
        hash = 83 * hash + ( this.fontSize != null ? this.fontSize.hashCode() : 0 );
        hash = 83 * hash +
               ( this.alignment != null ? this.alignment.hashCode() : 0 );
        hash = 83 * hash +
               ( this.foreground != null ? this.foreground.hashCode() : 0 );
        hash = 83 * hash +
               ( this.background != null ? this.background.hashCode() : 0 );
        hash = 83 * hash + ( this.bold != null ? this.bold.hashCode() : 0 );
        hash = 83 * hash + ( this.italic != null ? this.italic.hashCode() : 0 );
        hash = 83 * hash +
               ( this.underline != null ? this.underline.hashCode() : 0 );
        hash = 83 * hash +
               ( this.strikethrough != null ? this.strikethrough.hashCode() : 0 );
        hash = 83 * hash +
               ( this.superscript != null ? this.superscript.hashCode() : 0 );
        hash = 83 * hash +
               ( this.subscript != null ? this.subscript.hashCode() : 0 );
        hash = 83 * hash +
               ( this.lineSpacing != null ? this.lineSpacing.hashCode() : 0 );
        hash = 83 * hash +
               ( this.spaceAbove != null ? this.spaceAbove.hashCode() : 0 );
        hash = 83 * hash +
               ( this.spaceBelow != null ? this.spaceBelow.hashCode() : 0 );
        hash = 83 * hash +
               ( this.firstLineIndent != null ? this.firstLineIndent.hashCode() : 0 );
        hash = 83 * hash +
               ( this.leftIndent != null ? this.leftIndent.hashCode() : 0 );
        hash = 83 * hash +
               ( this.rightIndent != null ? this.rightIndent.hashCode() : 0 );
        hash = 83 * hash +
               ( this.bidiLevel != null ? this.bidiLevel.hashCode() : 0 );
        return hash;
    }

    @Override
    public String toString()
    {
        return getDisplayName();
    }
}
