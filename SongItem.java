import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

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

    // songTitle.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR), BorderFactory.createEmptyBorder(0, 10, 0, 10)));

    public SongItem(Media data) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        mainSongDetails = new JPanel();
        mainSongDetails.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));
        mainSongDetails.setBorder(new EmptyBorder(2, 0, 0, 0));
        
        // int totalWidth = getParent().getWidth() - 30;
        songImage = new JLabel(ImageUtils.getScaledCover("TempSongImage.png", 30)); //used to be new ImageIcon("TempSongImage.png") Just tested and this func doesn't seem to work, lol. I'll look through it again in a sec

        songTitle = new JLabel(data.name);
        songTitle.setHorizontalAlignment(SwingConstants.LEADING);
        // songTitle.setPreferredSize(new Dimension((int)(totalWidth * .3), 30));
        songTitle.setBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR));

        songAuthor = new JLabel(data.author);
        // songAuthor.setPreferredSize(new Dimension((int)(totalWidth * .3), 30));
        songAuthor.setBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR));


        mainSongDetails.add(songImage);
        mainSongDetails.add(songTitle);
        mainSongDetails.add(songAuthor);

        add(mainSongDetails);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int totalWidth = parent.getWidth();
            return new Dimension(totalWidth, 35);
        }

        return super.getPreferredSize();
    }

    @Override
    public void doLayout()
    {
        super.doLayout();

        int totalWidth = getWidth();

        songTitle.setPreferredSize(new Dimension((int)(totalWidth * .275), 30));
        songTitle.setPreferredSize(new Dimension((int)(totalWidth * .175), 30));
    }
}
