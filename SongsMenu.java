import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import models.Media;

import java.util.ArrayList;
import java.util.List;

public class SongsMenu extends JPanel {
    private JPanel MusicListContainer;
    private List<Media> allSongs;
    private List<SongItem> allListedMedia = new ArrayList<>();

    public void CreateMediaList()
    {
        try {
            allSongs = Database.getAllMedia();

            for(Media song : allSongs)
            {
                SongItem newListItem = new SongItem(song);
                allListedMedia.add((SongItem) newListItem);
                MusicListContainer.add(newListItem);
                newListItem.setOpaque(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SongsMenu()
    {
        setSize(1000, 10);
        setLayout(new BorderLayout(10, 7));
        setBorder(new EmptyBorder(12, 12, 12, 20));
        setBackground(ColorScheme.DARK_BG);
        
        var title = new JLabel("Songs");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        title.setForeground(ColorScheme.TEXT_COLOR);
        title.setHorizontalAlignment(SwingConstants.LEADING);

        MusicListContainer = new JPanel();
        MusicListContainer.setLayout(new FlowLayout(1,100000000, 1));
        MusicListContainer.setBackground(ColorScheme.LIGHT_BG);
        CreateMediaList();

        add(title, BorderLayout.NORTH);
        add(MusicListContainer);

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
    }
}
