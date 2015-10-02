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
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSelectionWidget
    extends JPanel
    implements FilesSelectionWidget.Listener
{

    public static final String FILE_LIST_MIME_TYPE = "application/x-java-file-list; class=java.util.List";
    private final Logger logger = LoggerFactory.getLogger( getClass() );
    private JFileChooser fileChooser;
    private JLabel outputFileLabel;
    private JTextField outputFileField = new JTextField( 10 );
    private JButton outputFileButton;
    private String chooseMessage;
    private int fileChooserMode;
    private LocalActionListener localActionListener;

    public FileSelectionWidget( String filesLabel,
                                String chooseLabel,
                                JFileChooser fileChooser,
                                String chooseMessage,
                                int fileChooserMode,
                                String toolTip )
    {
        super( new GridBagLayout() );
        
        this.fileChooser = fileChooser;
        
        outputFileLabel = new JLabel( filesLabel );
        outputFileButton = new JButton( chooseLabel );
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridheight = 1;
        labelConstraints.gridwidth = 2;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.fill = GridBagConstraints.NONE;
        labelConstraints.insets = new Insets( 5, 0, 5, 0 );

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridheight = 1;
        fieldConstraints.gridwidth = 1;
        fieldConstraints.weightx = 1.0;
        fieldConstraints.gridx = 0;
        fieldConstraints.anchor = GridBagConstraints.WEST;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets( 5, 0, 5, 5 );

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridheight = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.gridx = 1;
        buttonConstraints.anchor = GridBagConstraints.EAST;
        buttonConstraints.fill = GridBagConstraints.NONE;
        buttonConstraints.insets = new Insets( 5, 5, 5, 0 );
        
        add( outputFileLabel, labelConstraints );

        fieldConstraints.gridy = 1;
        add( outputFileField, fieldConstraints );

        buttonConstraints.gridy = 1;
        add( outputFileButton, buttonConstraints );
        
        outputFileLabel.setToolTipText( toolTip );
        outputFileField.setToolTipText( toolTip );
        outputFileButton.setToolTipText( toolTip );
        
        outputFileField.setDropTarget( new DropTarget( outputFileField, new OutputFilesDropTarget() ) );
        
        localActionListener = new LocalActionListener();
    }
    
    ////////////////////
    // file selection //
    ////////////////////

    public void chooseFile()
    {
        fileChooser.setMultiSelectionEnabled( false );
        fileChooser.setFileSelectionMode( fileChooserMode );
        int option = fileChooser.showDialog( this, chooseMessage );
        if( option == JFileChooser.APPROVE_OPTION )
        {
            setFile( fileChooser.getSelectedFile() );
        }
    }

    public void setFile( File file )
    {
        if( file == null )
        {
            outputFileField.setText( "" );
        }
        else
        {
            outputFileField.setText( file.getAbsolutePath() );
        }
    }

    public File getFile()
    {
        String outputFileText = outputFileField.getText();
        if( !StringUtils.isBlank(outputFileText ) )
        {
            return new File( outputFileText );
        }

        return null;
    }
    
    public void setString( String string )
    {
        if( string == null )
        {
            outputFileField.setText( "" );
        }
        else
        {
            outputFileField.setText( string );
        }
    }

    public String getString()
    {
        String outputFileText = outputFileField.getText();
        return outputFileText;
    }

    public void change( FilesSelectionWidget widget )
    {
        if( StringUtils.empty( getString() ) )
        {
            List<File> files = widget.getFiles();
            if( files != null )
            {
                for( File file : files )
                {
                    setString( file.getParent() );
                    return;
                }
            }
        }
    }
    
    /////////////////////
    // action listener //
    /////////////////////
    
    private class LocalActionListener
        implements ActionListener
    {

        public LocalActionListener()
        {
            outputFileButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Object source = evt.getSource();
            if( source == outputFileButton )
            {
                chooseFile();
            }
        }
    }
    
    ///////////////////
    // drag and drop //
    ///////////////////
    private class OutputFilesDropTarget
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

                            for( Object tmp : tmpList )
                            {
                                if( tmp instanceof File )
                                {
                                    File file = (File) tmp;
                                    setFile( file );
                                }
                            }
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
}
