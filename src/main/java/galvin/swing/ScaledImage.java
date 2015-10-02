/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class ScaledImage
extends JComponent
{
    private ImageIcon imageIcon;

    public ScaledImage( ImageIcon imageIcon )
    {
        this.imageIcon = imageIcon;
        setPreferredSize( new Dimension( 0, 0 ) );
    }

    @Override
    public void paint( Graphics g )
    {
        Graphics2D g2 = (Graphics2D)g;
        
        ImageIcon scaledImage = scaleImage();
        int width = scaledImage.getIconWidth();
        int height = scaledImage.getIconHeight();
        
        Dimension size = getSize();
        int x = ( size.width - width ) / 2;
        int y = ( size.height - height ) / 2;
        
        g2.drawImage( scaledImage.getImage(), x, y, this );
    }
    
    public ImageIcon scaleImage()
    {
        Dimension size = getSize();
        
        int width = imageIcon.getIconWidth();
        int height = imageIcon.getIconHeight();
        
        float widthRatio = (float)size.width / width;
        float heightRatio = (float)size.height / height;
        
        if( widthRatio < 1 || heightRatio < 1 )
        {
            float ratio = Math.min( widthRatio, heightRatio );
            int newWidth = (int)( width * ratio );
            int newHeight = (int)( height * ratio );
            
            Image image = imageIcon.getImage().getScaledInstance( newWidth, newHeight, Image.SCALE_DEFAULT );
            ImageIcon newImageIcon = new ImageIcon( image );
            return newImageIcon;
        }
        else
        {
            return imageIcon;
        }
    }
    
//    @Override
//    public void setBounds( int i, int i1, int i2, int i3 )
//    {
//        super.setBounds( i, i1, i2, i3 );
//        scaleImage();
//    }
//
//    @Override
//    public void setBounds( Rectangle rctngl )
//    {
//        super.setBounds( rctngl );
//        scaleImage();
//    }
//    
//    @Override
//    public void setSize( int i, int i1 )
//    {
//        setSize( new Dimension( i, i1 ) );
//        scaleImage();
//    }
//
//    @Override
//    public void setSize( Dimension size )
//    {
//        super.setSize( size );
//        scaleImage();
//    }
    
}
