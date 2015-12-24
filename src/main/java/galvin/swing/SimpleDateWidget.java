/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class SimpleDateWidget
    extends JPanel
{

    private JComboBox monthComboBox = new JComboBox( new String[]
    {
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    } );
    private JComboBox daysInMonthComboBox = new JComboBox();
    private JTextField yearField = new JTextField( 4 );

    private static final int[] DAYS_IN_MONTH = new int[]
    {
        31, //jan
        29, //feb
        31, //mar
        30, //apr
        31, //may
        30, //jun
        31, //jul
        31, //aug
        30, //sep
        31, //oct
        30, //nov
        31, //dec
    };

    public SimpleDateWidget()
    {
        super( new GridBagLayout() );

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.EAST;

        add( monthComboBox, constraints );

        constraints.gridx++;
        add( daysInMonthComboBox, constraints );

        constraints.gridx++;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add( yearField, constraints );

        setDate( new LocalDate() );
        new LocalActionListener();
    }

    public LocalDate getDate()
    {
        LocalDate result = new LocalDate( Integer.parseInt( yearField.getText() ),
                                          monthComboBox.getSelectedIndex() + 1,
                                          daysInMonthComboBox.getSelectedIndex() + 1 );
        return result;
    }

    public LocalDateTime getDateTime()
    {
        LocalDateTime result = new LocalDateTime( Integer.parseInt( yearField.getText() ),
                                                  monthComboBox.getSelectedIndex() + 1,
                                                  daysInMonthComboBox.getSelectedIndex() + 1,
                                                  0, 0, 0 );
        return result;
    }

    public void setDate( LocalDate date )
    {
        if( date != null )
        {
            yearField.setText( date.getYear() + "" );
            monthComboBox.setSelectedItem( date.getMonthOfYear() - 1 );
            setupDays();
            daysInMonthComboBox.setSelectedIndex( date.getDayOfMonth() - 1 );
        }
        else
        {
            setDate( new LocalDate( 0, 01, 01 ) );
        }
    }

    public void setDateTime( LocalDateTime date )
    {
        if( date != null )
        {
            setDate( date.toLocalDate() );
        }
        else
        {
            setDate( null );
        }
    }
    
    public void now(){
        setDate( new LocalDate() );
    }
    
    public void epoc(){
        setDate( new LocalDate( 1970, 1, 1 ) );
    }
    
    @Override
    public void setToolTipText( String text )
    {
        super.setToolTipText( text );
        daysInMonthComboBox.setToolTipText( text );
        monthComboBox.setToolTipText( text );
        yearField.setToolTipText( text );
    }

    private void setupDays()
    {
        int index = daysInMonthComboBox.getSelectedIndex();

        daysInMonthComboBox.removeAllItems();

        int count = DAYS_IN_MONTH[ monthComboBox.getSelectedIndex()];
        for(int i = 1; i <= count; i++)
        {
            daysInMonthComboBox.addItem( i );
        }

        index = Math.min( count - 1, index );
        daysInMonthComboBox.setSelectedIndex( index );
    }

    private class LocalActionListener
        implements ActionListener
    {

        public LocalActionListener()
        {
            monthComboBox.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if( source == monthComboBox )
            {
                setupDays();
            }
        }
    }

    public static void main( String[] args )
    {
        try
        {
            ApplicationWindow window = new ApplicationWindow( "Testing date widget" );
            SimpleDateWidget widget = new SimpleDateWidget();
            window.getContentPane().add( widget );
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
