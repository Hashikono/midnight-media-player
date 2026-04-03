import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
        BufferedImage scaledImage = scaleImageHighQuality(img, scaledW, scaledH);
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(img, 0, 0, scaledW, scaledH, null);
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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

    private static BufferedImage scaleImageHighQuality(Image img, int targetW, int targetH)
    {
        int w = img.getWidth(null);
        int h = img.getHeight(null);

        BufferedImage current = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = current.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        while (w > targetW || h > targetH) {
            w = Math.max(w / 2, targetW);
            h = Math.max(h / 2, targetH);

            BufferedImage tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = tmp.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2.drawImage(current, 0, 0, w, h, null);
            g2.dispose();

            current = tmp;
        }

        return current;
    }
}
