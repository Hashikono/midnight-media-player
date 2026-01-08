import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.Playlist;

public class PlaylistAddingMenu {
    public static JDialog playlistMakingDialog;
    public static String tryingThis;

    
    private static JTextField nameField = new JTextField(20);


    public static JDialog OpenPlaylistCreationMenu(JFrame parent) {
        try {Database.initialize();} catch (Exception e) {e.printStackTrace();}

        nameField.setText("");


        playlistMakingDialog = new JDialog(parent, "Create New Playlist", true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        playlistMakingDialog.setLayout(new BorderLayout());



        // extField.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row

        formPanel.add(nameField, gbc(c, 1, y++, 2));

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(e -> submit());
        cancel.addActionListener(e -> playlistMakingDialog.dispose());

        // buttons.add(ok);
        // buttons.add(cancel);

        c.gridwidth = 3;

        playlistMakingDialog.setSize(400, 200);
        playlistMakingDialog.add(formPanel, BorderLayout.CENTER);
        playlistMakingDialog.add(buttons, BorderLayout.SOUTH);

        return playlistMakingDialog;
    }

    private static void submit() {
        Playlist info = new Playlist(
            nameField.getText()
        );
        

        // result = info;
        try {
            Database.createPlaylist(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        playlistMakingDialog.dispose();
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
