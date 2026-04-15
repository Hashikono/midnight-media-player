import models.Media;
import models.Playlist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class AddMediaToPlaylistMenu {
    public static JDialog AddToPlaylistDialog;
    private static JComboBox<Playlist> dropDown = new JComboBox<Playlist>();
    private static List<Playlist> allPlaylists;

    public static JDialog OpenMediaAddingMenu(JFrame parent, Media media)
    {
        AddToPlaylistDialog = new JDialog(parent, "Add Media", true);
        AddToPlaylistDialog.setSize(400, 200);

        dropDown.removeAll();
        dropDown.addItem(null);

        try {
            allPlaylists = Database.getAllPlaylists();
            System.out.println(allPlaylists.size());

            for(Playlist listing : allPlaylists)
            {
                dropDown.addItem(listing);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel formPanel = new JPanel(new GridBagLayout());

        
        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row

        formPanel.add(dropDown, gbc(c, 1, y++, 2));

        // DialogButtons
        JPanel dialogButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        dialogButtons.add(ok);
        dialogButtons.add(cancel);

        ok.addActionListener(e -> submit(media));
        cancel.addActionListener(e -> AddToPlaylistDialog.dispose());

        c.gridwidth = 3;


        AddToPlaylistDialog.add(formPanel, BorderLayout.CENTER);
        AddToPlaylistDialog.add(dialogButtons, BorderLayout.SOUTH);

        return AddToPlaylistDialog;
    }

    
    private static void submit(Media media) {
        Playlist selected = (Playlist)dropDown.getSelectedItem();


        // // result = info;
        if(selected != null)
        {
            try {
                Database.insertToPlaylist(selected.id, media.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

        AddToPlaylistDialog.dispose();
    }
    
    // Utility for GridBagConstraints
    private static GridBagConstraints gbc(GridBagConstraints c, int x, int y) {
        return gbc(c, x, y, 1);
    }

    private static GridBagConstraints gbc(GridBagConstraints c, int x, int y, int w) {
        GridBagConstraints copy = (GridBagConstraints) c.clone();
        copy.gridx = x;
        copy.gridy = y;
        copy.gridwidth = w;
        return copy;
    }

}
