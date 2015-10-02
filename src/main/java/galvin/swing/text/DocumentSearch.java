/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.text;

import galvin.StringUtils;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class DocumentSearch
{

    public static int find( Document document, String searchText )
    {
        return find( document, searchText, false );
    }

    public static int find( Document document, String searchText, boolean ignoreCase )
    {
        return find( document, searchText, ignoreCase, 0 );
    }

    public static int find( Document document, String searchText, boolean ignoreCase, int index )
    {
        String text = DocumentUtils.getText( document );
        if( ignoreCase )
        {
            text = text.toLowerCase();
            searchText = searchText.toLowerCase();
        }

        return text.indexOf( searchText, index );
    }

    public static List<SearchResult> findAll( Document document, String searchText )
    {
        return findAll( document, searchText, false );
    }

    public static List<SearchResult> findAll( List<Document> documents, String searchText )
    {
        List<SearchResult> result = new ArrayList();

        for(Document document : documents)
        {
            result.addAll( findAll( document, searchText ) );
        }

        return result;
    }

    public static List<SearchResult> findAll( Document document, String searchText, boolean ignoreCase )
    {
        return findAll( document, searchText, ignoreCase, 0 );
    }

    public static List<SearchResult> findAll( List<Document> documents, String searchText, boolean ignoreCase )
    {
        List<SearchResult> result = new ArrayList();

        for(Document document : documents)
        {
            result.addAll( findAll( document, searchText, ignoreCase ) );
        }

        return result;
    }

    public static List<SearchResult> findAll( Document document, String searchText, boolean ignoreCase, int index )
    {
        String text = DocumentUtils.getText( document );
        String actualText = text;
        String actualSearchText = searchText;

        if( ignoreCase )
        {
            actualText = text.toLowerCase();
            actualSearchText = searchText.toLowerCase();
        }

        List<SearchResult> result = new ArrayList();
        while( index != -1 )
        {
            index = actualText.indexOf( actualSearchText, index );
            if( index != -1 )
            {
                String snippet = getSearchSnippet( text, index );
                SearchResult searchResult = new SearchResult( document, searchText, snippet, index );
                result.add( searchResult );

                index += searchText.length();
            }
        }

        return result;
    }

    public static List<SearchResult> findAll( List<Document> documents, String searchText, boolean ignoreCase, int index )
    {
        List<SearchResult> result = new ArrayList();

        for(Document document : documents)
        {
            result.addAll( findAll( document, searchText, ignoreCase, index ) );
        }

        return result;
    }

    public static String getSearchSnippet( String text, int index )
    {
        return getSearchSnippet( text, index, 35 );
    }

    public static String getSearchSnippet( String text, int index, int padding )
    {
        int startIndex = index;
        int endIndex = index;

        startIndex -= padding;
        if( startIndex < 0 )
        {
            startIndex = 0;
        }
        else
        {
            while( startIndex >= 0 && !Character.isWhitespace( text.charAt( startIndex ) ) )
            {
                startIndex--;
            }
        }

        endIndex += padding;
        if( endIndex >= text.length() )
        {
            endIndex = text.length();
        }
        else
        {
            while( endIndex <= text.length() && !Character.isWhitespace( text.charAt( endIndex ) ) )
            {
                endIndex--;
            }
        }

        String result = text.substring( startIndex, endIndex );
        result = StringUtils.replaceAll( result, "\n", " " );
        return result;
    }

    public static void replaceAllStyled( DefaultStyledDocument document, String originalText, String replacementText )
    {
        replaceAllStyled( document, originalText, replacementText, false );
    }

    public static void replaceAllStyled( List<DefaultStyledDocument> documents, String originalText, String replacementText )
    {
        for(DefaultStyledDocument document : documents)
        {
            replaceAllStyled( document, originalText, replacementText );
        }
    }

    public static void replaceAllStyled( DefaultStyledDocument document,
                                         String originalText,
                                         String replacementText,
                                         boolean ignoreCase )
    {
        replaceAllStyled( document, originalText, replacementText, false, 0 );
    }

    public static void replaceAllStyled( List<DefaultStyledDocument> documents,
                                         String originalText,
                                         String replacementText,
                                         boolean ignoreCase )
    {
        for(DefaultStyledDocument document : documents)
        {
            replaceAllStyled( document, originalText, replacementText, ignoreCase );
        }
    }

    public static void replaceAllStyled( DefaultStyledDocument document, String originalText, String replacementText, boolean ignoreCase, int index )
    {
        try
        {
            while( index != -1 )
            {
                index = find( document, originalText, ignoreCase, index );
                if( index != -1 )
                {
                    Element characterElement = document.getCharacterElement( index );
                    document.remove( index, originalText.length() );
                    document.insertString( index, replacementText, characterElement.getAttributes() );
                    index += replacementText.length();
                }
            }
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public static void replaceAllStyled( List<DefaultStyledDocument> documents, String originalText, String replacementText, boolean ignoreCase, int index )
    {
        for(DefaultStyledDocument document : documents)
        {
            replaceAllStyled( document, originalText, replacementText, ignoreCase, index );
        }
    }

    public static void replaceAllPlain( Document document,
                                        String originalText,
                                        String replacementText )
    {
        replaceAllPlain( document, originalText, replacementText, false );
    }

    public static void replaceAllPlain( List<Document> documents,
                                        String originalText,
                                        String replacementText )
    {
        for(Document document : documents)
        {
            replaceAllPlain( document, originalText, replacementText );
        }
    }

    public static void replaceAllPlain( Document document,
                                        String originalText,
                                        String replacementText,
                                        boolean ignoreCase )
    {
        replaceAllPlain( document, originalText, replacementText, ignoreCase, 0 );
    }

    public static void replaceAllPlain( List<Document> documents,
                                        String originalText,
                                        String replacementText,
                                        boolean ignoreCase )
    {
        for(Document document : documents)
        {
            replaceAllPlain( document, originalText, replacementText, ignoreCase );
        }
    }

    public static void replaceAllPlain( Document document,
                                        String originalText,
                                        String replacementText,
                                        boolean ignoreCase,
                                        int index )
    {
        StringBuilder text = new StringBuilder( DocumentUtils.getText( document ) );
        StringUtils.replaceAll( text, originalText, replacementText, ignoreCase );
        DocumentUtils.setText( document,
                               text.toString() );
    }

    public static void replaceAllPlain( List<Document> documents,
                                        String originalText,
                                        String replacementText,
                                        boolean ignoreCase,
                                        int index )
    {
        for(Document document : documents)
        {
            replaceAllPlain( document, originalText, replacementText, ignoreCase, index );
        }
    }
}
