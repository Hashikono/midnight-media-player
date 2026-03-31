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
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import models.Media;

public class SongItem extends JPanel {
    private JPanel mainSongDetails;
    private JPanel coverContainer;
    private JButton playButton;
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

    java.awt.event.MouseAdapter hoverHandler = new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            playButton.setVisible(true);
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            if (!coverContainer.getBounds().contains(javax.swing.SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), coverContainer)))
            {
                playButton.setVisible(false);
            }
        }
    };

    public SongItem(Media data) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        mainSongDetails = new JPanel();
        mainSongDetails.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));
        mainSongDetails.setBorder(new EmptyBorder(2, 0, 0, 0));
        
        //Image part
        coverContainer = new JPanel();
        coverContainer.setLayout(new OverlayLayout(coverContainer));
        coverContainer.setPreferredSize(new Dimension(40, 40));

        songImage = new JLabel(ImageUtils.getScaledCover("TempSongImage.png", 40)); //used to be new ImageIcon("TempSongImage.png") Just tested and this func doesn't seem to work, lol. I'll look through it again in a sec
        songImage.setAlignmentX(.5f);
        songImage.setAlignmentY(.5f);
        // songImage.setPreferredSize(new Dimension(30, 30));

        playButton = new JButton("▶");
        playButton.setAlignmentX(.5f);
        playButton.setAlignmentY(.5f);
        playButton.setMinimumSize(new Dimension(40, 40));
        playButton.setMaximumSize(new Dimension(40, 40));

        playButton.setContentAreaFilled(true);
        playButton.setBorderPainted(false);
        playButton.setBackground(new Color(20, 20, 20, 10));
        playButton.setForeground(ColorScheme.DARK_BG);
        playButton.setVisible(false);

        coverContainer.add(playButton);
        coverContainer.add(songImage);

        // coverContainer.addMouseListener(new java.awt.event.MouseAdapter() {
        //     @Override
        //     public void mouseEntered(java.awt.event.MouseEvent e) {
        //         playButton.setVisible(true);
        //     }

        //     @Override
        //     public void mouseExited(java.awt.event.MouseEvent e) {
        //         playButton.setVisible(false);
        //     }
        // });

        coverContainer.addMouseListener(hoverHandler);
        playButton.addMouseListener(hoverHandler);
        songImage.addMouseListener(hoverHandler);

        songTitle = new JLabel(data.name);
        songTitle.setHorizontalAlignment(SwingConstants.LEADING);
        songTitle.setBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR));

        songAuthor = new JLabel(data.author);
        songAuthor.setBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR));


        mainSongDetails.add(coverContainer);
        mainSongDetails.add(songTitle);
        mainSongDetails.add(songAuthor);

        add(mainSongDetails);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int totalWidth = parent.getWidth();
            return new Dimension(totalWidth, 45);
        }

        return super.getPreferredSize();
    }

    @Override
    public void doLayout()
    {
        super.doLayout();

        int totalWidth = getWidth() - 30;

        songTitle.setPreferredSize(new Dimension((int)(totalWidth * .275), 30));
        songTitle.setPreferredSize(new Dimension((int)(totalWidth * .175), 30));
    }
}
