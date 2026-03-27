import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.Media;

public class SongItem extends JPanel {
    private JPanel mainSongDetails;
    private JLabel songImage;
    private JLabel songTitle;
    private JLabel songAuthor;

    // JPanel panel = new JPanel(new GridBagLayout());
    // GridBagConstraints gbc = new GridBagConstraints();

    // gbc.fill = GridBagConstraints.BOTH;
    // gbc.gridy = 0;

    // // Left (30%)
    // gbc.gridx = 0;
    // gbc.weightx = 0.3;
    // panel.add(leftPanel, gbc);

    // // Right (70%)
    // gbc.gridx = 1;
    // gbc.weightx = 0.7;
    // panel.add(rightPanel, gbc);

    public SongItem(Media data) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        mainSongDetails = new JPanel();
        mainSongDetails.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));
        
        songImage = new JLabel(new ImageIcon("TempSongImage.png"));
        songTitle = new JLabel(data.name);
        songAuthor = new JLabel(data.author);


        // mainSongDetails.add(songImage);
        mainSongDetails.add(songTitle);
        mainSongDetails.add(songAuthor);

        add(mainSongDetails);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int width = parent.getWidth();
            return new Dimension(width, 35);
        }

        return super.getPreferredSize();
    }
}
