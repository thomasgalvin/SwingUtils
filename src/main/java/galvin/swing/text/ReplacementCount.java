package galvin.swing.text;

public class ReplacementCount {
    public int totalCount;
    public int documentCount;

    public ReplacementCount() {
    }

    public ReplacementCount( int totalCount, int documentCount ) {
        this.totalCount = totalCount;
        this.documentCount = documentCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount( int totalCount ) {
        this.totalCount = totalCount;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount( int documentCount ) {
        this.documentCount = documentCount;
    }
    
}
