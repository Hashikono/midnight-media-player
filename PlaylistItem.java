import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import models.Playlist;

public class PlaylistItem extends JButton {
    private JLabel coverImage;
    private JLabel playListName;

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
        coverImage = new JLabel(ImageUtils.resizeImageIcon(new ImageIcon(ImageUtils.bytesToImage(data.image)), imageSize, imageSize));
        coverImage.setPreferredSize(new Dimension(30, 30));
        playListName = new JLabel(data.name);

        add(coverImage);
        add(playListName);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int totalWidth = parent.getWidth();
            int newSide = totalWidth / 3;
            return new Dimension(newSide, newSide);
        }

        return super.getPreferredSize();
    }

    @Override
    public void doLayout()
    {
        super.doLayout();

        int totalWidth = getWidth();

        int imageSize = (totalWidth - 6) / 3;
        coverImage.setIcon(ImageUtils.resizeImageIcon(new ImageIcon((Image)coverImage.getIcon()), imageSize, imageSize));

        // songTitle.setPreferredSize(new Dimension((int)(totalWidth * .275), 30));
        // songAuthor.setPreferredSize(new Dimension((int)(totalWidth * .175), 30));
    }
}
