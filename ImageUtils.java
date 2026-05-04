// import java.awt.Color;
// import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
// import java.awt.RenderingHints;
// import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Blob;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.swing.Icon;
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

    public static ImageIcon resizeImageIcon(ImageIcon image, int width, int height)
    {
        return new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public static ImageIcon resizeImageIcon(ImageIcon image, int size)
    {
        return resizeImageIcon(image, size, size);
    }

    public static BufferedImage bytesToImage(byte[] data) throws Exception
    {
        if(data == null || data.length == 0)
            return ImageIO.read(new File("Images/TempSongImage.png"));

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return ImageIO.read(bis);
    }

    public static ImageIcon bytesToIcon(byte[] data) {
        if (data == null || data.length == 0)
            return null;

        return new ImageIcon(data);
    }

    public static BufferedImage bytesToImage(Blob blob) throws Exception
    {
        if(blob == null)
            return ImageIO.read(new File("Images/TempSongImage.png"));
            //return ImageIO.read(ImageUtils.class.getResourceAsStream("/Images/TempSongImage.png"));

        return bytesToImage(blob.getBytes(1, (int) blob.length()));
    }

    public static Icon resizeImageIcon(Icon icon) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resizeImageIcon'");
    }

    public static byte[] getBytesFromFile(String path) throws Exception
    {
        return Files.readAllBytes(Path.of(path));
    }

    public static Blob bytesToBlob(byte[] data) throws SerialException, SQLException
    {
        if(data != null)
        {
            java.sql.Blob blob = new SerialBlob(data);
            return blob;
        }
        else
            return null;
    }

    public static byte[] blobToBytes(Blob blob) throws SQLException
    {
        if(blob != null)
            return blob.getBytes(1, (int)blob.length());
        else
            return null;
    }
}
