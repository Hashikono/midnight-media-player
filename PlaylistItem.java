import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Playlist;

public class PlaylistItem extends JButton {
    private JLabel coverImage;
    private JLabel playListName;

    private ImageIcon originalIcon;

    private void OpenContextMenu(int heldIndex)
    {
        JDialog dialog = MediaAddingMenu.OpenMediaAddingMenu(App.player, heldIndex);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true);
        // System.out.println(heldIndex);
    }


    public PlaylistItem(Playlist data) throws SQLException, Exception {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int imageSize = 100;
        originalIcon = new ImageIcon(ImageUtils.bytesToImage(data.image));
        coverImage = new JLabel(ImageUtils.resizeImageIcon(originalIcon, imageSize, imageSize));
        coverImage.setPreferredSize(new Dimension(imageSize, imageSize));

        var tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        tempPanel.setPreferredSize(new Dimension(0, 0));

        playListName = new JLabel(data.name);
        playListName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        playListName.setHorizontalAlignment(SwingConstants.CENTER);

        tempPanel.add(playListName);

        add(coverImage);
        add(tempPanel);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int totalWidth = parent.getWidth();
            int newSide = totalWidth / 3;
            int imageSides = newSide - 10;
            coverImage.setIcon(ImageUtils.resizeImageIcon(originalIcon, imageSides, imageSides));
            coverImage.setPreferredSize(new Dimension(imageSides - 50, imageSides - 50));
            return new Dimension(newSide, newSide);
        }

        return super.getPreferredSize();
    }

    @Override
    public void doLayout()
    {
        super.doLayout();

        // int totalWidth = getWidth();

        // int imageSize = (totalWidth - 6) / 3;
        // coverImage.setIcon(ImageUtils.resizeImageIcon(originalIcon, imageSize, imageSize));
        // coverImage.setPreferredSize(new Dimension(imageSize, imageSize));
    }
}
