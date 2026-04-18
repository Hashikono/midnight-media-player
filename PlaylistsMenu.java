import java.awt.BorderLayout;
import java.awt.Dimension;
// import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

// import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
// import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import models.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistsMenu extends JPanel {
    public static PlaylistsMenu instance;
    private JPanel topComponents;
    private JButton openPlayListCreatorMenu;

    private JPanel PlaylistsContainer;
    private List<Playlist> allPlaylists;
    private List<PlaylistItem> allPlaylistItems = new ArrayList<>();

    public void Refresh()
    {
        PlaylistsContainer.removeAll();
        CreatePlaylistItems();
        revalidate();
        repaint();
    }

    public void CreatePlaylistItems()
    {
        try {
            allPlaylists = Database.getAllPlaylists();

            for(Playlist song : allPlaylists)
            {
                PlaylistItem newListItem = new PlaylistItem(song);
                allPlaylistItems.add((PlaylistItem) newListItem);
                PlaylistsContainer.add(newListItem);
                newListItem.setOpaque(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void OpenPlaylistCreator() {
        JDialog dialog = PlaylistAddingMenu.OpenPlaylistCreationMenu(App.player);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true); // BLOCKS until dialog is closed
    }

    public PlaylistsMenu()
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

        openPlayListCreatorMenu = new JButton("New Playlist");
        openPlayListCreatorMenu.setBackground(ColorScheme.PRIMARY_COLOR.brighter());
        openPlayListCreatorMenu.setOpaque(true);
        openPlayListCreatorMenu.setBorderPainted(false);
        openPlayListCreatorMenu.setMaximumSize(new Dimension(20, 10)); //Not doing anything for some reason

        openPlayListCreatorMenu.addActionListener(e -> OpenPlaylistCreator());

        topComponents.add(title, BorderLayout.WEST);
        topComponents.add(openPlayListCreatorMenu, BorderLayout.EAST);

        PlaylistsContainer = new JPanel();
        // PlaylistsContainer.setLayout(new FlowLayout(1,1, 1));
        PlaylistsContainer.setLayout(new GridLayout(0, 3, 10, 10));
        PlaylistsContainer.setBackground(ColorScheme.LIGHT_BG);
        CreatePlaylistItems();

        JScrollPane scrollSideBar = new JScrollPane(PlaylistsContainer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSideBar.setPreferredSize(new Dimension(20, 0));
        scrollSideBar.setBorder(null);
        scrollSideBar.setViewportBorder(null);
        scrollSideBar.getViewport().setBackground(ColorScheme.LIGHT_BG);

        add(topComponents, BorderLayout.NORTH);
        add(scrollSideBar);

        instance = this;
    }
}
