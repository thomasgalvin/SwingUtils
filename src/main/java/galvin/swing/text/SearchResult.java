package galvin.swing.text;

import javax.swing.text.Document;

public class SearchResult
{
    private Document document;
    private String searchWord;
    private String snippet;
    private int index;

    public SearchResult()
    {
        this( null, null, -1 );
    }

    public SearchResult( Document document, String searchWord, int index )
    {
        this( document, searchWord, null, index );
    }

    public SearchResult( Document document, String searchWord, String snippet, int index )
    {
        this.document = document;
        this.searchWord = searchWord;
        this.snippet = snippet;
        this.index = index;
    }

    public Document getDocument()
    {
        return document;
    }

    public void setDocument( Document document )
    {
        this.document = document;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    public String getSearchWord()
    {
        return searchWord;
    }

    public void setSearchWord( String searchWord )
    {
        this.searchWord = searchWord;
    }

    public String getSnippet()
    {
        return snippet;
    }

    public void setSnippet( String snippet )
    {
        this.snippet = snippet;
    }

}