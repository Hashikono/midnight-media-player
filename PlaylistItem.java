// import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
// import java.awt.Image;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
// import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Playlist;

public class PlaylistItem extends JButton {
    private JPanel imageContainer;
    private JLabel coverImage;
    private JPanel labelContainer;
    private JLabel playListName;

    private ImageIcon originalIcon;

    private void OpenPlaylist(Playlist data)
    {
        //Open Songs Menu with playlist thing
        App.player.OpenMediaCollection(data);
        App.player.nav.UnselectButtons();
    }


    public PlaylistItem(Playlist data) throws SQLException, Exception {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorScheme.SECONDARY_COLOR); //Idk why it's not changing the color

        int imageSize = 100;
        
        originalIcon = new ImageIcon(ImageUtils.bytesToImage(data.image));
        coverImage = new JLabel(ImageUtils.resizeImageIcon(originalIcon, imageSize, imageSize));
        coverImage.setAlignmentX(CENTER_ALIGNMENT);

        playListName = new JLabel(data.name);
        playListName.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        playListName.setHorizontalAlignment(SwingConstants.CENTER);
        playListName.setAlignmentX(CENTER_ALIGNMENT);


        add(coverImage);
        add(playListName);

        addActionListener(e -> OpenPlaylist(data));
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            // int totalWidth = parent.getWidth();
            // int newSide = (totalWidth - 10) / 3;
            int newSide = getWidth();
            int imageSides = newSide - 80;
            coverImage.setIcon(ImageUtils.resizeImageIcon(originalIcon, imageSides, imageSides));
            // imageContainer.setSize(new Dimension(imageSides, imageSides));
            return new Dimension(newSide, newSide - 50);
        }

        return super.getPreferredSize();
    }

    // @Override
    // public void doLayout()
    // {
    //     super.doLayout();

    //     int totalWidth = getWidth();

    //     int imageSides = totalWidth - 80;
    //     coverImage.setIcon(ImageUtils.resizeImageIcon(originalIcon, imageSides, imageSides));
    //     // coverImage.setIcon(ImageUtils.resizeImageIcon(originalIcon, imageSize, imageSize));
    //     // imageContainer.setPreferredSize(new Dimension(imageSize, imageSize));
    // }
}
