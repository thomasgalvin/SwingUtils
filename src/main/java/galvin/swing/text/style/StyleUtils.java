package galvin.swing.text.style;

import galvin.swing.text.DocumentUtils;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public final class StyleUtils
{

    public static final String TITLE_STYLE_LOGICAL_NAME = "title";
    public static final String SUBTITLE_STYLE_LOGICAL_NAME = "titletwo";
    public static final String BYLINE_STYLE_LOGICAL_NAME = "byline";
    public static final String DOCUMENT_BYLINE_STYLE_LOGICAL_NAME = "documentbyline";
    public static final String HEADING_ONE_STYLE_LOGICAL_NAME = "headingone";
    public static final String HEADING_TWO_STYLE_LOGICAL_NAME = "headingtwo";
    public static final String HEADING_THREE_STYLE_LOGICAL_NAME = "headingthree";
    public static final String HEADING_FOUR_STYLE_LOGICAL_NAME = "headingfour";
    public static final String HEADING_FIVE_STYLE_LOGICAL_NAME = "headingfive";
    public static final String BODY_STYLE_LOGICAL_NAME = "default";
    public static final String BODY_ONE_STYLE_LOGICAL_NAME = "bodyone";
    public static final String BODY_TWO_STYLE_LOGICAL_NAME = "bodytwo";
    public static final String BODY_THREE_STYLE_LOGICAL_NAME = "bodythree";
    public static final String BODY_FOUR_STYLE_LOGICAL_NAME = "bodyfour";
    public static final String BODY_FIVE_STYLE_LOGICAL_NAME = "bodyfive";
    public static final String QUOTATION_STYLE_LOGICAL_NAME = "quotation";
    public static final String CAPTION_STYLE_LOGICAL_NAME = "caption";
    public static final String SEPARATOR_STYLE_LOGICAL_NAME = "separator";
    public static final String TITLE_STYLE_DISPLAY_NAME = "Title";
    public static final String SUBTITLE_STYLE_DISPLAY_NAME = "Subtitle";
    public static final String BYLINE_STYLE_DISPLAY_NAME = "Byline";
    public static final String DOCUMENT_BYLINE_STYLE_DISPLAY_NAME = "Document Byline";
    public static final String HEADING_ONE_STYLE_DISPLAY_NAME = "Heading One";
    public static final String HEADING_TWO_STYLE_DISPLAY_NAME = "Heading Two";
    public static final String HEADING_THREE_STYLE_DISPLAY_NAME = "Heading Three";
    public static final String HEADING_FOUR_STYLE_DISPLAY_NAME = "Heading Four";
    public static final String HEADING_FIVE_STYLE_DISPLAY_NAME = "Heading Five";
    public static final String BODY_STYLE_DISPLAY_NAME = "Body";
    public static final String BODY_ONE_STYLE_DISPLAY_NAME = "Body One";
    public static final String BODY_TWO_STYLE_DISPLAY_NAME = "Body Two";
    public static final String BODY_THREE_STYLE_DISPLAY_NAME = "Body Three";
    public static final String BODY_FOUR_STYLE_DISPLAY_NAME = "Body Four";
    public static final String BODY_FIVE_STYLE_DISPLAY_NAME = "Body Five";
    public static final String QUOTATION_STYLE_DISPLAY_NAME = "Quotation";
    public static final String CAPTION_STYLE_DISPLAY_NAME = "Caption";
    public static final String SEPARATOR_STYLE_DISPLAY_NAME = "Separator";
    public static final StyleContext styleContext = new StyleContext();
    public static final String STYLE_SET_EXTENSION = ".style.xml";
    public static final Style noAttributesStyle = styleContext.addStyle( "COM_GALVIN_EDITOR_COMPONENTS_TEXT_PANE_NO_ATTRIBUTES_STYLE", null );
    public static final Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);// = styleContext.addStyle( "COM_GALVIN_EDITOR_COMPONENTS_TEXT_PANE_DEFAULT_STYLE", null );

    static //initialize defaultStyle
    {
//        Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        defaultStyle.addAttribute( StyleConstants.FontFamily, "Monospaced" );
        defaultStyle.addAttribute( StyleConstants.FontSize, new Integer( 12 ) );
        defaultStyle.addAttribute( StyleConstants.Background, Color.white );
        defaultStyle.addAttribute( StyleConstants.Foreground, Color.black );
        defaultStyle.addAttribute( StyleConstants.Bold, Boolean.FALSE );
        defaultStyle.addAttribute( StyleConstants.Italic, Boolean.FALSE );
    }
    protected static final String BOLD = "bold";
    public static final Style boldStyle = styleContext.addStyle( BOLD, null );

    static //initialize boldStyle
    {
        boldStyle.addAttribute( StyleConstants.Bold, Boolean.TRUE );
    }
    protected static final String UNBOLD = "unbold";
    public static final Style unboldStyle = styleContext.addStyle( UNBOLD, null );

    static //initialize unboldStyle
    {
        unboldStyle.addAttribute( StyleConstants.Bold, Boolean.FALSE );
    }
    protected static final String ITALIC = "italic";
    public static final Style italicStyle = styleContext.addStyle( ITALIC, null );

    static //initialize italic style
    {
        italicStyle.addAttribute( StyleConstants.Italic, Boolean.TRUE );
    }
    protected static final String UNITALIC = "unitalic";
    public static final Style unitalicStyle = styleContext.addStyle( UNITALIC, null );

    static //initialize unitalic style
    {
        unitalicStyle.addAttribute( StyleConstants.Italic, Boolean.FALSE );
    }
    protected static final String UNDERLINE = "underline";
    public static final Style underlineStyle = styleContext.addStyle( UNDERLINE, null );

    static //initialize underlineStyle
    {
        underlineStyle.addAttribute( StyleConstants.Underline, Boolean.TRUE );
    }
    protected static final String UNUNDERLINE = "ununderline";
    public static final Style ununderlineStyle = styleContext.addStyle( UNUNDERLINE, null );

    static //initialize ununderlineStyle
    {
        ununderlineStyle.addAttribute( StyleConstants.Underline, Boolean.FALSE );
    }
    protected static final String STRIKETHROUGH = "strikeThrough";
    public static final Style strikeThroughStyle = styleContext.addStyle( STRIKETHROUGH, null );

    static //initialize strikeThroughStyle
    {
        strikeThroughStyle.addAttribute( StyleConstants.StrikeThrough, Boolean.TRUE );
    }
    protected static final String UNSTRIKETHROUGH = "unstrikeThrough";
    public static final Style unstrikeThroughStyle = styleContext.addStyle( UNSTRIKETHROUGH, null );

    static //initialize unstrikeThroughStyle
    {
        unstrikeThroughStyle.addAttribute( StyleConstants.StrikeThrough, Boolean.FALSE );
    }
    protected static final String SUPERSCRIPT = "superscript";
    public static final Style superscriptStyle = styleContext.addStyle( SUPERSCRIPT, null );

    static //initialize superscriptStyle
    {
        superscriptStyle.addAttribute( StyleConstants.Superscript, Boolean.TRUE );
    }
    protected static final String UNSUPERSCRIPT = "unsuperscript";
    public static final Style unsuperscriptStyle = styleContext.addStyle( UNSUPERSCRIPT, null );

    static //initialize unsuperscriptStyle
    {
        unsuperscriptStyle.addAttribute( StyleConstants.Superscript, Boolean.FALSE );
    }
    protected static final String SUBSCRIPT = "subscript";
    public static final Style subscriptStyle = styleContext.addStyle( SUBSCRIPT, null );

    static //initialize subscriptStyle
    {
        subscriptStyle.addAttribute( StyleConstants.Subscript, Boolean.TRUE );
    }
    protected static final String UNSUBSCRIPT = "unsubscript";
    public static final Style unsubscriptStyle = styleContext.addStyle( UNSUBSCRIPT, null );

    static //initialize unsubscriptStyle
    {
        unsubscriptStyle.addAttribute( StyleConstants.Subscript, Boolean.FALSE );
    }
    protected static final String ALIGN_LEFT = "align-left";
    public static final Style alignLeftStyle = styleContext.addStyle( ALIGN_LEFT, null );

    static //initialize alignLeftStyle
    {
        alignLeftStyle.addAttribute( StyleConstants.Alignment, new Integer( StyleConstants.ALIGN_LEFT ) );
    }
    protected static final String ALIGN_CENTER = "align-center";
    public static final Style alignCenterStyle = styleContext.addStyle( ALIGN_CENTER, null );

    static //initialize alignCenterStyle
    {
        alignCenterStyle.addAttribute( StyleConstants.Alignment, new Integer( StyleConstants.ALIGN_CENTER ) );
    }
    protected static final String ALIGN_RIGHT = "align-right";
    public static final Style alignRightStyle = styleContext.addStyle( ALIGN_RIGHT, null );

    static //initialize alignRightStyle
    {
        alignRightStyle.addAttribute( StyleConstants.Alignment, new Integer( StyleConstants.ALIGN_RIGHT ) );
    }
    protected static final String ALIGN_JUSTIFIED = "align-justified";
    public static final Style alignJustifiedStyle = styleContext.addStyle( ALIGN_JUSTIFIED, null );

    static //initialize alignJustifiedStyle
    {
        alignJustifiedStyle.addAttribute( StyleConstants.Alignment, new Integer( StyleConstants.ALIGN_JUSTIFIED ) );
    }
    protected static final String NORMAL = "normal";
    public static final Style normalStyle = styleContext.addStyle( NORMAL, null );

    static //initialize normal style
    {
        normalStyle.addAttribute( StyleConstants.Subscript, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.Superscript, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.StrikeThrough, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.Underline, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.Italic, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.Bold, Boolean.FALSE );
        normalStyle.addAttribute( StyleConstants.Alignment, new Integer( StyleConstants.ALIGN_LEFT ) );
        //normalStyle.addAttribute(StyleConstants.FontSize, new Integer(12));
    }

    private StyleUtils()
    {
    }

    public static Style createStyle( String logicalName )
    {
        Style result = styleContext.addStyle( logicalName, null );
        return result;
    }

    public static StyleSet createStyleSet( DefaultStyledDocument document )
    {
        if( document != null )
        {
            StyleSet styleSet = new StyleSet( "document-style-set" );

            Enumeration styleNames = document.getStyleNames();
            while( styleNames.hasMoreElements() )
            {
                Object name = styleNames.nextElement();

                Style style = document.getStyle( name.toString() );
                StyleInfo styleInfo = new StyleInfo();
                styleInfo.setLogicalName( name.toString() );
                styleInfo.setDisplayName( name.toString() );
                StyleUtils.annotateStyleInfo( styleInfo, style );
                styleSet.addStyle( styleInfo );
            }

            return styleSet;
        }
        return null;
    }

    public static void annotateStyleInfo( StyleInfo styleInfo, AttributeSet source )
    {
        if( source instanceof Style )
        {
            Style style = ( Style ) source;
            styleInfo.setLogicalName( style.getName() );
        }

        if( source.getAttribute( StyleConstants.FontFamily ) != null )
        {
            styleInfo.setFontName( source.getAttribute( StyleConstants.FontFamily ).toString() );
        }

        if( source.getAttribute( StyleConstants.FontSize ) != null )
        {
            styleInfo.setFontSize( ( Integer ) source.getAttribute( StyleConstants.FontSize ) );
        }

        if( source.getAttribute( StyleConstants.Alignment ) != null )
        {
            styleInfo.setAlignment( ( Integer ) source.getAttribute( StyleConstants.Alignment ) );
        }

        if( source.getAttribute( StyleConstants.Foreground ) != null )
        {
            styleInfo.setForeground( ( Color ) source.getAttribute( StyleConstants.Foreground ) );
        }

        if( source.getAttribute( StyleConstants.Background ) != null )
        {
            styleInfo.setBackground( ( Color ) source.getAttribute( StyleConstants.Background ) );
        }

        if( source.getAttribute( StyleConstants.Bold ) != null )
        {
            styleInfo.setBold( ( Boolean ) source.getAttribute( StyleConstants.Bold ) );
        }

        if( source.getAttribute( StyleConstants.Italic ) != null )
        {
            styleInfo.setItalic( ( Boolean ) source.getAttribute( StyleConstants.Italic ) );
        }

        if( source.getAttribute( StyleConstants.Underline ) != null )
        {
            styleInfo.setUnderline( ( Boolean ) source.getAttribute( StyleConstants.Underline ) );
        }

        if( source.getAttribute( StyleConstants.StrikeThrough ) != null )
        {
            styleInfo.setStrikethrough( ( Boolean ) source.getAttribute( StyleConstants.StrikeThrough ) );
        }

        if( source.getAttribute( StyleConstants.Superscript ) != null )
        {
            styleInfo.setSuperscript( ( Boolean ) source.getAttribute( StyleConstants.Superscript ) );
        }

        if( source.getAttribute( StyleConstants.Subscript ) != null )
        {
            styleInfo.setSubscript( ( Boolean ) source.getAttribute( StyleConstants.Subscript ) );
        }

        if( source.getAttribute( StyleConstants.FirstLineIndent ) != null )
        {
            styleInfo.setFirstLineIndent( ( Float ) source.getAttribute( StyleConstants.FirstLineIndent ) );
        }

        if( source.getAttribute( StyleConstants.LeftIndent ) != null )
        {
            styleInfo.setLeftIndent( ( Float ) source.getAttribute( StyleConstants.LeftIndent ) );
        }

        if( source.getAttribute( StyleConstants.RightIndent ) != null )
        {
            styleInfo.setRightIndent( ( Float ) source.getAttribute( StyleConstants.RightIndent ) );
        }

        if( source.getAttribute( StyleConstants.LineSpacing ) != null )
        {
            styleInfo.setLineSpacing( ( Float ) source.getAttribute( StyleConstants.LineSpacing ) );
        }

        if( source.getAttribute( StyleConstants.SpaceAbove ) != null )
        {
            styleInfo.setSpaceAbove( ( Float ) source.getAttribute( StyleConstants.SpaceAbove ) );
        }

        if( source.getAttribute( StyleConstants.SpaceBelow ) != null )
        {
            styleInfo.setSpaceBelow( ( Float ) source.getAttribute( StyleConstants.SpaceBelow ) );
        }

        if( source.getAttribute( StyleConstants.BidiLevel ) != null )
        {
            styleInfo.setBidiLevel( ( Integer ) source.getAttribute( StyleConstants.BidiLevel ) );
        }

        if( source.getAttribute( HTML.Attribute.HREF ) != null )
        {
            styleInfo.setHyperlink( ( String ) source.getAttribute( HTML.Attribute.HREF ) );
        }
    }

    public static void annotateStyle( Style style, StyleInfo source )
    {
        Object hyperlinkObject = style.getAttribute( HTML.Attribute.HREF );
        String hyperlink = hyperlinkObject == null ? null : hyperlinkObject.toString();

        annotateStyle( style,
                       source.getFontName(), source.getFontSize(), source.getAlignment(),
                       source.getForeground(), source.getBackground(),
                       source.getBold(), source.getItalic(), source.getUnderline(), source.getStrikethrough(), source.getSuperscript(), source.getSuperscript(),
                       source.getLineSpacing(), source.getSpaceAbove(), source.getSpaceBelow(),
                       source.getFirstLineIndent(), source.getLeftIndent(), source.getRightIndent(),
                       source.getBidiLevel(), hyperlink );
    }

    public static void annotateStyle( Style style,
                                      String fontFamily, Integer fontSize, Integer alignment,
                                      Color foreground, Color background,
                                      Boolean bold, Boolean italic, Boolean underline, Boolean strikethrough, Boolean superscript, Boolean subscript,
                                      Float lineSpacing, Float spaceAbove, Float spaceBelow,
                                      Float firstLineIndent, Float leftIndent, Float rightIndent,
                                      Integer bidiLevel, String hyperlink )
    {
        annotateStyle( style, StyleConstants.FontFamily, fontFamily );
        annotateStyle( style, StyleConstants.FontSize, fontSize );
        annotateStyle( style, StyleConstants.Alignment, alignment );

        annotateStyle( style, StyleConstants.Foreground, foreground );
        annotateStyle( style, StyleConstants.Background, background );

        annotateStyle( style, StyleConstants.Bold, bold );
        annotateStyle( style, StyleConstants.Italic, italic );
        annotateStyle( style, StyleConstants.Underline, underline );
        annotateStyle( style, StyleConstants.StrikeThrough, strikethrough );
        annotateStyle( style, StyleConstants.Superscript, superscript );
        annotateStyle( style, StyleConstants.Subscript, subscript );

        annotateStyle( style, StyleConstants.FirstLineIndent, firstLineIndent );
        annotateStyle( style, StyleConstants.LeftIndent, leftIndent );
        annotateStyle( style, StyleConstants.RightIndent, rightIndent );

        annotateStyle( style, StyleConstants.LineSpacing, lineSpacing );
        annotateStyle( style, StyleConstants.SpaceAbove, spaceAbove );
        annotateStyle( style, StyleConstants.SpaceBelow, spaceBelow );

        annotateStyle( style, StyleConstants.BidiLevel, bidiLevel );

        annotateStyle( style, HTML.Attribute.HREF, hyperlink );
    }

    public static void annotateStyle( Style style, Object key, Object value )
    {
        if( value != null )
        {
            style.addAttribute( key, value );
        }
        else
        {
            style.removeAttribute( key );
        }
    }

    public static StyleSet readStyleSet( File file )
            throws JAXBException
    {
        JAXBContext context = JAXBContext.newInstance( StyleSet.class );
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StyleSet styleSet = ( StyleSet ) unmarshaller.unmarshal( file );
        return styleSet;
    }

    public static void writeStyleSet( File file, StyleSet styleSet )
            throws JAXBException
    {
        file.getParentFile().mkdirs();
        JAXBContext context = JAXBContext.newInstance( StyleSet.class );
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        marshaller.marshal( styleSet, file );
    }

    public static void updateStyles( StyleSet styleSet, StyledDocument document )
    {
        for( StyleInfo style : styleSet.getStyles() )
        {
            DocumentUtils.overrideStyle( style.getLogicalName(), document, style.getStyle() );
        }
    }

    public static StyleSet collateStyles( List<DefaultStyledDocument> documents )
    {
        List<StyleSet> styleSets = new ArrayList();
        for( DefaultStyledDocument document : documents )
        {
            if( document != null )
            {
                StyleSet styleSet = createStyleSet( document );
                if( styleSet != null )
                {
                    styleSets.add( styleSet );
                }
            }
        }
        return collate( styleSets );
    }

    public static StyleSet collate( List<StyleSet> styleSets )
    {
        StyleSet result = new StyleSet();
        HashMap<String, StyleInfo> logicalNameMap = new HashMap();

        for( StyleSet styleSet : styleSets )
        {
            for( StyleInfo styleInfo : styleSet.getStyles() )
            {
                if( !logicalNameMap.containsKey( styleInfo.getLogicalName() ) )
                {
                    logicalNameMap.put( styleInfo.getLogicalName(), styleInfo );
                    result.addStyle( styleInfo );
                }
            }
        }

        return result;
    }

    public static List<StyleInfo> getMissingStyles( List<StyleInfo> authoritative, List<StyleInfo> replacement )
    {
        List<StyleInfo> result = new ArrayList();

        for( StyleInfo styleInfo : authoritative )
        {
            if( !replacement.contains( styleInfo ) )
            {
                result.add( styleInfo );
            }
        }

        return result;
    }

    public static void normalize( StyleInfo source, StyleInfo duplicate )
    {
        if( isDuplicate( source.getFontName(), duplicate.getFontName() ) )
        {
            duplicate.setFontName( null );
        }

        if( isDuplicate( source.getFontSize(), duplicate.getFontSize() ) )
        {
            duplicate.setFontSize( null );
        }

        if( isDuplicate( source.getAlignment(), duplicate.getAlignment() ) )
        {
            duplicate.setAlignment( null );
        }

        if( isDuplicate( source.getAlignment(), duplicate.getAlignment() ) )
        {
            duplicate.setAlignment( null );
        }

        if( isDuplicate( source.getForeground(), duplicate.getForeground() ) )
        {
            duplicate.setForeground( null );
        }

        if( isDuplicate( source.getBackground(), duplicate.getBackground() ) )
        {
            duplicate.setBackground( null );
        }

        if( isDuplicate( source.getBold(), duplicate.getBold() ) )
        {
            duplicate.setBold( null );
        }

        if( isDuplicate( source.getItalic(), duplicate.getItalic() ) )
        {
            duplicate.setItalic( null );
        }

        if( isDuplicate( source.getUnderline(), duplicate.getUnderline() ) )
        {
            duplicate.setUnderline( null );
        }

        if( isDuplicate( source.getStrikethrough(), duplicate.getStrikethrough() ) )
        {
            duplicate.setStrikethrough( null );
        }

        if( isDuplicate( source.getSuperscript(), duplicate.getSuperscript() ) )
        {
            duplicate.setSuperscript( null );
        }

        if( isDuplicate( source.getSubscript(), duplicate.getSubscript() ) )
        {
            duplicate.setSubscript( null );
        }

        if( isDuplicate( source.getSubscript(), duplicate.getSubscript() ) )
        {
            duplicate.setSubscript( null );
        }

        if( isDuplicate( source.getLineSpacing(), duplicate.getLineSpacing() ) )
        {
            duplicate.setLineSpacing( null );
        }

        if( isDuplicate( source.getSpaceAbove(), duplicate.getSpaceAbove() ) )
        {
            duplicate.setSpaceAbove( null );
        }

        if( isDuplicate( source.getSpaceBelow(), duplicate.getSpaceBelow() ) )
        {
            duplicate.setSpaceBelow( null );
        }

        if( isDuplicate( source.getFirstLineIndent(), duplicate.getFirstLineIndent() ) )
        {
            duplicate.setFirstLineIndent( null );
        }

        if( isDuplicate( source.getLeftIndent(), duplicate.getLeftIndent() ) )
        {
            duplicate.setLeftIndent( null );
        }

        if( isDuplicate( source.getRightIndent(), duplicate.getRightIndent() ) )
        {
            duplicate.setRightIndent( null );
        }

        if( isDuplicate( source.getBidiLevel(), duplicate.getBidiLevel() ) )
        {
            duplicate.setBidiLevel( null );
        }

        if( isDuplicate( source.getHyperlink(), duplicate.getHyperlink() ) )
        {
            duplicate.setHyperlink( null );
        }
    }

    private static boolean isDuplicate( Object source, Object duplicate )
    {
        if( source != null && duplicate != null )
        {
            return source.equals( duplicate );
        }

        return false;
    }

    public static StyleInfo getDefaultStyle( String logicalName, String displayName )
    {
        StyleInfo style = new StyleInfo( logicalName, displayName );
        style.setFontName( "Arial" );
        style.setFontSize( 12 );
        style.setForeground( Color.BLACK );
        style.setBackground( new Color( 1.0f, 1.0f, 1.0f, 0.0f ) );
        style.setBold( false );
        style.setItalic( false );
        style.setUnderline( false );
        style.setStrikethrough( false );
        style.setSubscript( false );
        style.setSuperscript( false );
        style.setFirstLineIndent( 0.0f );
        style.setLeftIndent( 0.0f );
        style.setRightIndent( 0.0f );
        style.setLineSpacing( 0.0f );
        style.setSpaceAbove( 15f );
        style.setSpaceBelow( 15f );
        style.setBidiLevel( 0 );
        style.setAlignment( StyleConstants.ALIGN_JUSTIFIED );
        return style;
    }

    public static StyleSet getDefaultStyleSet()
    {
        StyleSet styleSet = new StyleSet( "Default" );


        StyleInfo style = getDefaultStyle( TITLE_STYLE_LOGICAL_NAME, TITLE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 27 );
        style.setBold( true );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        style = getDefaultStyle( SUBTITLE_STYLE_LOGICAL_NAME, SUBTITLE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 24 );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        style = new StyleInfo( BYLINE_STYLE_LOGICAL_NAME, BYLINE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( DOCUMENT_BYLINE_STYLE_LOGICAL_NAME, DOCUMENT_BYLINE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_ONE_STYLE_LOGICAL_NAME, HEADING_ONE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 20 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_TWO_STYLE_LOGICAL_NAME, HEADING_TWO_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 19 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_THREE_STYLE_LOGICAL_NAME, HEADING_THREE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 18 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_FOUR_STYLE_LOGICAL_NAME, HEADING_FOUR_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 17 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_FIVE_STYLE_LOGICAL_NAME, HEADING_FIVE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 16 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_STYLE_LOGICAL_NAME, BODY_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_ONE_STYLE_LOGICAL_NAME, BODY_ONE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_TWO_STYLE_LOGICAL_NAME, BODY_TWO_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_THREE_STYLE_LOGICAL_NAME, BODY_THREE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_FOUR_STYLE_LOGICAL_NAME, BODY_FOUR_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_FIVE_STYLE_LOGICAL_NAME, BODY_FIVE_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( QUOTATION_STYLE_LOGICAL_NAME, QUOTATION_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( CAPTION_STYLE_LOGICAL_NAME, CAPTION_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_RIGHT );
        styleSet.addStyle( style );
        
        style = getDefaultStyle( SEPARATOR_STYLE_LOGICAL_NAME, SEPARATOR_STYLE_DISPLAY_NAME );
        style.setFontName( "Arial" );
        style.setFontSize( 10 );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        return styleSet;
    }

    public static StyleSet getManuscriptStyleSet()
    {
        StyleSet styleSet = new StyleSet( "Manuscript" );

        StyleInfo style = getDefaultStyle( TITLE_STYLE_LOGICAL_NAME, TITLE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 27 );
        style.setBold( true );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        style = getDefaultStyle( SUBTITLE_STYLE_LOGICAL_NAME, SUBTITLE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 24 );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        style = new StyleInfo( BYLINE_STYLE_LOGICAL_NAME, BYLINE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( DOCUMENT_BYLINE_STYLE_LOGICAL_NAME, DOCUMENT_BYLINE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_ONE_STYLE_LOGICAL_NAME, HEADING_ONE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 20 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_TWO_STYLE_LOGICAL_NAME, HEADING_TWO_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 19 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_THREE_STYLE_LOGICAL_NAME, HEADING_THREE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 18 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_FOUR_STYLE_LOGICAL_NAME, HEADING_FOUR_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 17 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( HEADING_FIVE_STYLE_LOGICAL_NAME, HEADING_FIVE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 16 );
        style.setBold( true );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_STYLE_LOGICAL_NAME, BODY_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 14 );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_ONE_STYLE_LOGICAL_NAME, BODY_ONE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_TWO_STYLE_LOGICAL_NAME, BODY_TWO_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_THREE_STYLE_LOGICAL_NAME, BODY_THREE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_FOUR_STYLE_LOGICAL_NAME, BODY_FOUR_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( BODY_FIVE_STYLE_LOGICAL_NAME, BODY_FIVE_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        styleSet.addStyle( style );

        style = new StyleInfo( QUOTATION_STYLE_LOGICAL_NAME, QUOTATION_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        style.setItalic( true );
        style.setLeftIndent( 50.0f );
        style.setRightIndent( 50.0f );
        styleSet.addStyle( style );

        style = new StyleInfo( CAPTION_STYLE_LOGICAL_NAME, CAPTION_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setLineSpacing( 0.5f );
        style.setSpaceAbove( 10f );
        style.setItalic( true );
        style.setAlignment( StyleConstants.ALIGN_RIGHT );
        styleSet.addStyle( style );
        
        style = getDefaultStyle( SEPARATOR_STYLE_LOGICAL_NAME, SEPARATOR_STYLE_DISPLAY_NAME );
        style.setFontName( "Courier" );
        style.setFontSize( 10 );
        style.setAlignment( StyleConstants.ALIGN_CENTER );
        styleSet.addStyle( style );

        return styleSet;
    }

    public static boolean isCentered( AttributeSet attributes )
    {
        if( attributes != null )
        {
            if( attributes.getAttribute( StyleConstants.Alignment ) != null )
            {
                Integer value = ( Integer ) attributes.getAttribute( StyleConstants.Alignment );
                if( value == StyleConstants.ALIGN_CENTER )
                {
                    return true;
                }
            }
        }

        return false;
    }
}
