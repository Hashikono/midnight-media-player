import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ImageUtils {
    public static ImageIcon getScaledCover(String path, int size)
    {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();

        // System.out.println(icon.getIconWidth());
        
        int initialWidth = img.getWidth(null);
        int initialHeight = img.getHeight(null);

        float scale = Math.max((float)size / initialWidth, (float)size / initialHeight);

        int scaledW = (int)(initialWidth * scale);
        int scaledH = (int)(initialHeight * scale);
        
        // Image scaledImage = img.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(scaledW, scaledH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(img, 0, 0, scaledW, scaledH, null);
        g2.dispose();

        BufferedImage crop = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = crop.createGraphics();

        int finalW = (size - scaledW) / 2;
        int finalY = (size - scaledH) / 2;
        // g.setColor(Color.RED);
        // g.fillRect(0, 0, size, size);
        g.drawImage(scaledImage, finalW, finalY, null);
        g.dispose();

        return new ImageIcon(crop);
    }
}
