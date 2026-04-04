// import java.awt.Color;
// import java.awt.Graphics2D;
import java.awt.Image;
// import java.awt.RenderingHints;
// import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ImageUtils {
    // public static ImageIcon getScaledCover(String path, int size)
    // {
        //Grab image from mp3 files?
    // }

    public static ImageIcon getResizedImage(String path, int size)
    {
        ImageIcon image = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));

        return image;
    }

    public static ImageIcon getResizedImage(String path, int width, int height)
    {
        ImageIcon image = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));

        return image;
    }
}
