/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package galvin.swing;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JPanel;

public final class ImageUtils
{
    private ImageUtils(){}
    
    private static final JPanel IMAGE_OBSERVER = new JPanel();
    public static int SCALE_METHOD = Image.SCALE_DEFAULT;
    
    public static Image scaleImage( Image image, int width, int height )
    {
        return scaleImage( image, width, height, false );
    }

    public static Image scaleImage( Image image, int width, int height,
                                    boolean keepAspectRatio )
    {
        if( keepAspectRatio )
        {
            Dimension originalImageSize = new Dimension( image.getWidth( IMAGE_OBSERVER ), image.getHeight( IMAGE_OBSERVER ) );
            float widthPercentage = (float) width / originalImageSize.width;
            float heightPercentage = (float) height / originalImageSize.height;
            float scalePercentage = Math.min( widthPercentage, heightPercentage );

            return scaleImage( image, scalePercentage, scalePercentage );
        }
        else
        {
            return image.getScaledInstance( width, height, SCALE_METHOD );
        }
    }

    public static Image scaleImage( Image image, float widthPercentage,
                                    float heightPercentage )
    {
        Dimension imageSize = new Dimension( image.getWidth( IMAGE_OBSERVER ), image.getHeight( IMAGE_OBSERVER ) );
        imageSize.width *= widthPercentage;
        imageSize.height *= heightPercentage;

        Image scaledImage = image.getScaledInstance( imageSize.width, imageSize.height, SCALE_METHOD );
        return scaledImage;
    }
}
