/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing.text.macros;

import galvin.swing.ApplicationWindow;
import galvin.swing.GuiUtils;
import galvin.StringUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MacroEditor
    extends JPanel
{

    private DefaultListModel model = new DefaultListModel();
    private JList list = new JList( model );
    private JScrollPane listScrollPane = new JScrollPane( list );
    private JTextField nameField = new JTextField();
    private JLabel beforeCursorLabel = new JLabel( "Text before cursor:" );
    private JTextArea beforeCursorArea = new JTextArea();
    private JScrollPane beforeCursorScrollPane = new JScrollPane( beforeCursorArea );
    private JLabel afterCursorLabel = new JLabel( "Text after cursor:" );
    private JTextArea afterCursorArea = new JTextArea();
    private JScrollPane afterCursorScrollPane = new JScrollPane( afterCursorArea );
    private JButton newButton = new JButton( "+" );
    private JButton removeButton = new JButton( "-" );
    private JButton doneButton = new JButton( "Done" );
    private JButton applyButton = new JButton( "Apply" );
    private JLabel[] labels = new JLabel[]
    {
        beforeCursorLabel, afterCursorLabel
    };
    Dimension labelSize = GuiUtils.sameSize( labels );
    private Dimension fieldSize = GuiUtils.preferredSize( nameField );
    private JButton[] controlButtons = new JButton[]
    {
        doneButton, applyButton
    };
    private Dimension controlButtonSize = GuiUtils.sameSize( controlButtons );
    private JButton[] addRemoveButtons = new JButton[]
    {
        newButton, removeButton
    };
    private Dimension addRemoveButtonSize = GuiUtils.sameSize( addRemoveButtons );
    private List<Macro> macros = new ArrayList();
    private Macro selectedMacro;
    private boolean listenToSelections = true;

    public MacroEditor()
    {
        setLayout( null );
        add( listScrollPane );
        add( nameField );
        add( beforeCursorLabel );
        add( beforeCursorScrollPane );
        add( afterCursorLabel );
        add( afterCursorScrollPane );
        add( newButton );
        add( removeButton );
        add( doneButton );
        add( applyButton );

        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setPreferredSize( new Dimension( 700, 500 ) );

        new Listener();
    }

    public void editSelectedMacro()
    {
        Object tmp = list.getSelectedValue();
        listenToSelections = false;
        saveMacro();

        Object selection = tmp;
        if( selection != null && selection instanceof Macro )
        {
            selectedMacro = (Macro) selection;
            editMacro( selectedMacro );
        }
        listenToSelections = true;
    }

    public void editMacro( Macro macro )
    {
        selectedMacro = macro;

        if( macro != null )
        {
            String abb = macro.getAddbreviation();
            nameField.setText( abb );
            beforeCursorArea.setText( macro.getBeforeCursor() );
            afterCursorArea.setText( macro.getAfterCursor() );

            if( StringUtils.empty( abb ) )
            {
                nameField.requestFocus();
            }
            else
            {
                beforeCursorArea.requestFocus();
            }
        }
        else
        {
            nameField.setText( "" );
            beforeCursorArea.setText( "" );
            afterCursorArea.setText( "" );
        }
    }

    public void saveMacro()
    {
        if( selectedMacro == null )
        {
            selectedMacro = new Macro();
        }
        String abb = nameField.getText();
        String before = beforeCursorArea.getText();
        String after = afterCursorArea.getText();

        if( !StringUtils.empty( before ) || !StringUtils.empty( after ) )
        {
            if( !StringUtils.empty( abb ) )
            {
                selectedMacro.setAddbreviation( abb );

                selectedMacro.setBeforeCursor( before );
                selectedMacro.setAfterCursor( after );

                if( !macros.contains( selectedMacro ) )
                {
                    macros.add( selectedMacro );
                    model.addElement( selectedMacro );

                    notifyMacroAdded( selectedMacro );
                }
                else
                {
                    notifyMacroUpdated( selectedMacro );
                }

                if( listenToSelections )
                {
                    list.setSelectedValue( selectedMacro, true );
                }
            }

            GuiUtils.forceRepaint( list );
        }
    }

    public void newMacro()
    {
        editMacro( new Macro() );
    }

    public void removeMacro()
    {
        if( selectedMacro != null )
        {
            model.removeElement( selectedMacro );
            macros.remove( selectedMacro );
            notifyMacroRemoved( selectedMacro );
            newMacro();
        }
    }

    public void done()
    {
        saveMacro();
        setVisible( false );
        notifyDone();
    }

    public List<Macro> getMacros()
    {
        List<Macro> result = new ArrayList();
        result.addAll( macros );
        return result;
    }

    public void setMacros( List<Macro> macros )
    {
        this.macros.clear();
        this.macros.addAll( macros );

        model.clear();
        for(Macro macro : macros)
        {
            model.addElement( macro );
        }
    }

    @Override
    public void doLayout()
    {
        Dimension size = getSize();
        int width = size.width - GuiUtils.PADDING * 3;
        width /= 3;

        int height = size.height - GuiUtils.PADDING * 3;
        height -= controlButtonSize.height;

        Dimension listSize = new Dimension( width, height );
        listScrollPane.setSize( listSize );

        height -= fieldSize.height + GuiUtils.PADDING * 2;
        height -= labelSize.height * 2 + GuiUtils.PADDING * 2;
        height /= 2;
        Dimension textAreaSize = new Dimension( width * 2, height );
        beforeCursorScrollPane.setSize( textAreaSize );
        afterCursorScrollPane.setSize( textAreaSize );

        fieldSize.width = textAreaSize.width;
        nameField.setSize( fieldSize );

        int x = GuiUtils.PADDING;
        int y = GuiUtils.PADDING;

        listScrollPane.setLocation( x, y );
        x += listSize.width + GuiUtils.PADDING;

        nameField.setLocation( x, y );
        y += fieldSize.height + GuiUtils.PADDING;

        beforeCursorLabel.setLocation( x, y );
        y += labelSize.height + GuiUtils.PADDING;

        beforeCursorScrollPane.setLocation( x, y );
        y += textAreaSize.height + GuiUtils.PADDING;

        afterCursorLabel.setLocation( x, y );
        y += labelSize.height + GuiUtils.PADDING;

        afterCursorScrollPane.setLocation( x, y );

        int buttonX = GuiUtils.PADDING;
        int buttonY = size.height - addRemoveButtonSize.height - GuiUtils.PADDING;
        newButton.setLocation( buttonX, buttonY );

        int minButtonWidth = addRemoveButtonSize.width * 2 + GuiUtils.PADDING;
        if( listSize.width >= minButtonWidth )
        {
            buttonX += listSize.width;
            buttonX -= addRemoveButtonSize.width;
            removeButton.setLocation( buttonX, buttonY );
        }
        else
        {
            buttonX += addRemoveButtonSize.width + GuiUtils.PADDING;
            removeButton.setLocation( buttonX, buttonY );
        }

        buttonX = size.width - controlButtonSize.width - GuiUtils.PADDING;
        applyButton.setLocation( buttonX, buttonY );

        buttonX -= controlButtonSize.width + GuiUtils.PADDING;
        doneButton.setLocation( buttonX, buttonY );
    }

    private class Listener
        implements ActionListener,
                   ListSelectionListener
    {

        public Listener()
        {
            list.addListSelectionListener( this );
            applyButton.addActionListener( this );
            doneButton.addActionListener( this );
            newButton.addActionListener( this );
            removeButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent ae )
        {
            Object source = ae.getSource();

            if( source == applyButton )
            {
                saveMacro();
            }
            else if( source == doneButton )
            {
                done();
            }
            else if( source == newButton )
            {
                newMacro();
            }
            else if( source == removeButton )
            {
                removeMacro();
            }
        }

        public void valueChanged( ListSelectionEvent lse )
        {
            if( listenToSelections )
            {
                editSelectedMacro();
            }
        }
    }

    public interface MacroListListener
    {

        public void macroAdded( Macro macro, Object source );

        public void macroRemoved( Macro macro, Object source );

        public void macroUpdated( Macro macro, Object source );

        public void macroEditingComplete( Object source );
    }

    private List<MacroListListener> macroListListeners = new ArrayList();

    public void addMacroListListener( MacroListListener listener )
    {
        macroListListeners.add( listener );
    }

    public void removeMacroListListener( MacroListListener listener )
    {
        macroListListeners.remove( listener );
    }

    public void notifyMacroAdded( Macro macro )
    {
        for(int i = macroListListeners.size() - 1; i >= 0; i--)
        {
            MacroListListener listener = macroListListeners.get( i );
            listener.macroAdded( macro, this );
        }
    }

    public void notifyMacroRemoved( Macro macro )
    {
        for(int i = macroListListeners.size() - 1; i >= 0; i--)
        {
            MacroListListener listener = macroListListeners.get( i );
            listener.macroRemoved( macro, this );
        }
    }

    public void notifyMacroUpdated( Macro macro )
    {
        for(int i = macroListListeners.size() - 1; i >= 0; i--)
        {
            MacroListListener listener = macroListListeners.get( i );
            listener.macroUpdated( macro, this );
        }
    }

    public void notifyDone()
    {
        for(int i = macroListListeners.size() - 1; i >= 0; i--)
        {
            MacroListListener listener = macroListListeners.get( i );
            listener.macroEditingComplete( this );
        }
    }

    public static void main( String[] args )
    {
        try
        {
            List<Macro> macros = new ArrayList();

            Macro macro = new Macro( "sout", "System.out.println( \"", " \");" );
            macros.add( macro );

            macro = new Macro( "main", "public static void main( String[] args ){", "}" );
            macros.add( macro );

            MacroEditor editor = new MacroEditor();
            editor.setMacros( macros );

            MacroListListener listener = new MacroListListener()
            {

                public void macroAdded( Macro macro, Object source )
                {
                    System.out.println( "added: " + macro.getAddbreviation() );
                }

                public void macroRemoved( Macro macro, Object source )
                {
                    System.out.println( "removed: " + macro.getAddbreviation() );
                }

                public void macroUpdated( Macro macro, Object source )
                {
                    System.out.println( "updated: " + macro.getAddbreviation() );
                }

                public void macroEditingComplete( Object source )
                {
                    System.out.println( "macro editing done." );
                }
            };
            editor.addMacroListListener( listener );

            ApplicationWindow window = new ApplicationWindow( "Macros" );
            window.getContentPane().add( editor );
            window.pack();
            window.center();
            window.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}