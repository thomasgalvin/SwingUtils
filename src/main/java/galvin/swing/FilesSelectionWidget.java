package galvin.swing;

import galvin.StringUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesSelectionWidget
    extends JPanel
{
    private static final Logger logger = LoggerFactory.getLogger( FilesSelectionWidget.class );
    public static final String FILE_LIST_MIME_TYPE = "application/x-java-file-list; class=java.util.List";
    private JLabel inputFilesLabel;
    private JTextArea inputFilesTextArea = new JTextArea();
    private JScrollPane inputFilesScrollPane = new JScrollPane( inputFilesTextArea );
    private JButton inputFilesButton;
    private LocalActionListener localActionListener;
    private LocalDocListener localDocListener;
    private JFileChooser fileChooser;
    private String chooseMessage;
    private int fileChooserMode;
    private List<Listener> listeners = new ArrayList();

    public FilesSelectionWidget( String filesLabel,
                                 String chooseLabel,
                                 JFileChooser fileChooser,
                                 String chooseMessage,
                                 int fileChooserMode,
                                 String toolTip )
    {
        super( new GridBagLayout() );

        this.fileChooser = fileChooser;
        this.chooseMessage = chooseMessage;
        this.fileChooserMode = fileChooserMode;

        inputFilesLabel = new JLabel( filesLabel );
        inputFilesButton = new JButton( chooseLabel );

        localActionListener = new LocalActionListener();
        localDocListener = new LocalDocListener();

        GridBagConstraints textAreaLabelConstraints = new GridBagConstraints();
        textAreaLabelConstraints.gridheight = 1;
        textAreaLabelConstraints.gridwidth = 1;
        textAreaLabelConstraints.gridx = 0;
        textAreaLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        textAreaLabelConstraints.insets = new Insets( 5, 0, 5, 0 );

        GridBagConstraints textAreaConstraints = new GridBagConstraints();
        textAreaConstraints.gridheight = 1;
        textAreaConstraints.gridwidth = 1;
        textAreaConstraints.gridx = 0;
        textAreaConstraints.weightx = 1.0;
        textAreaConstraints.weighty = 1.0;
        textAreaConstraints.fill = GridBagConstraints.BOTH;
        textAreaConstraints.insets = new Insets( 5, 0, 5, 0 );

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridheight = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.gridx = 0;
        buttonConstraints.anchor = GridBagConstraints.EAST;
        buttonConstraints.fill = GridBagConstraints.NONE;
        buttonConstraints.insets = new Insets( 5, 0, 5, 0 );

        textAreaLabelConstraints.gridy = 0;
        add( inputFilesLabel, textAreaLabelConstraints );

        textAreaConstraints.gridy = 1;
        add( inputFilesScrollPane, textAreaConstraints );

        buttonConstraints.gridy = 2;
        add( inputFilesButton, buttonConstraints );

        inputFilesTextArea.setDropTarget( new DropTarget( inputFilesTextArea, new InputFilesDropTarget() ) );
        
        inputFilesLabel.setToolTipText( toolTip );
        inputFilesTextArea.setToolTipText( toolTip );
        inputFilesButton.setToolTipText( toolTip );
    }

    public void chooseFiles()
    {
        fileChooser.setMultiSelectionEnabled( true );
        fileChooser.setFileSelectionMode( fileChooserMode );
        int option = fileChooser.showDialog( this, chooseMessage );
        if( option == JFileChooser.APPROVE_OPTION )
        {
            List<File> files = Arrays.asList( fileChooser.getSelectedFiles() );
            addFiles( files );
        }
    }

    public void addFiles( List<File> files )
    {
        StringBuilder fileText = new StringBuilder();

        if( files != null )
        {
            for( File file : files )
            {
                fileText.append( file.getAbsolutePath() );
                fileText.append( "\n" );
            }
        }

        inputFilesTextArea.append( fileText.toString() );
    }

    public List<File> getFiles()
    {
        List<File> result = new ArrayList();

        String inputFileText = inputFilesTextArea.getText();
        inputFileText = inputFileText.trim();
        String[] inputFileTextLines = inputFileText.split( "\n" );
        for( String inputFilePath : inputFileTextLines )
        {
            if( !StringUtils.empty( inputFilePath ) )
            {
                File file = new File( inputFilePath );
                result.add( file );
            }
        }

        return result;
    }

    public void setFiles( List<File> files )
    {
        inputFilesTextArea.setText( "" );
        addFiles( files );
    }
    
    public void addStrings( List<String> strings )
    {
        StringBuilder fileText = new StringBuilder();

        if( strings != null )
        {
            for( String string : strings )
            {
                fileText.append( string );
                fileText.append( "\n" );
            }
        }

        inputFilesTextArea.append( fileText.toString() );
    }
    
    public List<String> getStrings()
    {
        List<String> result = new ArrayList();

        String inputFileText = inputFilesTextArea.getText();
        inputFileText = inputFileText.trim();
        String[] inputFileTextLines = inputFileText.split( "\n" );
        for( String inputFilePath : inputFileTextLines )
        {
            if( !StringUtils.empty( inputFilePath ) )
            {
                result.add( inputFilePath );
            }
        }

        return result;
    }
    
    public void setStrings( List<String> strings )
    {
        inputFilesTextArea.setText( "" );
        addStrings( strings );
    }

    //////////////////////
    // action listeners //
    //////////////////////
    private class LocalActionListener
        implements ActionListener
    {

        public LocalActionListener()
        {
            inputFilesButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Object source = evt.getSource();
            if( source == inputFilesButton )
            {
                chooseFiles();
            }
        }
    }
    
    private class LocalDocListener
    implements DocumentListener
    {
        public LocalDocListener()
        {
            inputFilesTextArea.getDocument().addDocumentListener( this );
        }
        
        public void changedUpdate( DocumentEvent evt )
        {
            //for some reason, gaining focus is considered a change event.
            //since there really is no change, this little hack will
            //get around it.
            if( evt.toString().compareTo( "[]" ) != 0 )
            {
                recordChange( evt );
            }
        }

        public void insertUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void removeUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void recordChange( DocumentEvent evt )
        {
            notifyFileSelectionListeners();
        }
    }
    
    public interface Listener
    {
        public void change( FilesSelectionWidget widget );
    }
    
    public void addFileSelectionListener( Listener listener )
    {
        listeners.add( listener );
    }
    
    public void removeFileSelectionListener( Listener listener )
    {
        listeners.remove( listener );
    }
    
    public void notifyFileSelectionListeners()
    {
        List<Listener> actionListeners = new ArrayList();
        actionListeners.addAll( listeners );
        Collections.reverse( actionListeners );
        for( Listener listener : actionListeners )
        {
            listener.change( this );
        }
    }

    ///////////////////
    // drag and drop //
    ///////////////////
    private class InputFilesDropTarget
        extends DropTargetAdapter
    {

        @Override
        public void drop( DropTargetDropEvent dtde )
        {
            for( DataFlavor flavor : dtde.getCurrentDataFlavors() )
            {
                if( FILE_LIST_MIME_TYPE.equals( flavor.getMimeType() ) )
                {
                    dtde.acceptDrop( DnDConstants.ACTION_REFERENCE );
                    Transferable transferable = dtde.getTransferable();

                    try
                    {
                        Object transferableData = transferable.getTransferData( flavor );

                        if( transferableData instanceof List )
                        {
                            List tmpList = (List) transferableData;
                            List<File> fileList = new ArrayList();

                            for( Object tmp : tmpList )
                            {
                                if( tmp instanceof File )
                                {
                                    File file = (File) tmp;
                                    fileList.add( file );
                                }
                            }

                            addFiles( fileList );
                        }
                    }
                    catch( Throwable t )
                    {
                        logger.error( "Unsupported data flavor", t );
                    }
                }
            }
        }
    }

    ////////////
    // driver //
    ////////////
    public static void main( String[] args )
    {
        try
        {
            FilesSelectionWidget optionsPanel = new FilesSelectionWidget( "Select files",
                                                                          "Choose...",
                                                                          new JFileChooser(),
                                                                          "Select these files",
                                                                          JFileChooser.FILES_AND_DIRECTORIES,
                                                                          "Select files" );

            ApplicationWindow appWindow = new ApplicationWindow( "Testing file selection widget" );
            appWindow.getContentPane().add( new JScrollPane( optionsPanel ) );
            appWindow.pack();
            appWindow.center();
            appWindow.setVisible( true );

        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}
