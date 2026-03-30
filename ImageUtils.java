import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ImageUtils {
    public static ImageIcon getScaledCover(String path, int size)
    {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        
        int sideLength = img.getWidth(null);

        float scale = (float)size / sideLength; //Random notes from where I was getting this stuff from... use Math.max(val, val) to get the largest of 2 vals.

        Image scaledImage = img.getScaledInstance((int)(sideLength * scale), (int)(sideLength * scale), Image.SCALE_SMOOTH);

        BufferedImage crop = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = crop.createGraphics();

        int finalSide = (size - (int)scale) / 2;
        g.drawImage(scaledImage, finalSide, finalSide, null);
        g.dispose();

        return new ImageIcon(crop);
    }
}
