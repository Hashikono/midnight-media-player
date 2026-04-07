import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

// import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
// import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import models.Media;

import java.util.ArrayList;
import java.util.List;

public class SongsMenu extends JPanel {
    private JPanel topComponents;
    private JButton openMediaAdderButton;

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
    
    private void OpenMediaAddingMenu() {
        JDialog dialog = MediaAddingMenu.OpenMediaAddingMenu(App.player);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true); // BLOCKS until dialog is closed
    }

    public SongsMenu()
    {
        setSize(1000, 10);
        setLayout(new BorderLayout(10, 7));
        setBorder(new EmptyBorder(12, 12, 12, 20));
        setBackground(ColorScheme.DARK_BG);
        
        topComponents = new JPanel();
        topComponents.setLayout(new BorderLayout());
        topComponents.setPreferredSize(new Dimension(1000, 50));
        topComponents.setBackground(ColorScheme.DARK_BG);

        var title = new JLabel("Songs");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        title.setForeground(ColorScheme.TEXT_COLOR);
        title.setHorizontalAlignment(SwingConstants.LEADING);

        openMediaAdderButton = new JButton("Add Media");
        openMediaAdderButton.setBackground(ColorScheme.PRIMARY_COLOR.brighter());
        openMediaAdderButton.setOpaque(true);
        openMediaAdderButton.setBorderPainted(false);
        openMediaAdderButton.setMaximumSize(new Dimension(20, 10)); //Not doing anything for some reason

        openMediaAdderButton.addActionListener(e -> OpenMediaAddingMenu());

        topComponents.add(title, BorderLayout.WEST);
        topComponents.add(openMediaAdderButton, BorderLayout.EAST);

        MusicListContainer = new JPanel();
        MusicListContainer.setLayout(new FlowLayout(1,100000000, 1));
        MusicListContainer.setBackground(ColorScheme.LIGHT_BG);
        CreateMediaList();

        add(topComponents, BorderLayout.NORTH);
        add(MusicListContainer);
    }
}
