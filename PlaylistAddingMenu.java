import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import models.Playlist;

public class PlaylistAddingMenu {
    public static JDialog playlistMakingDialog;
    public static String tryingThis;

    private static JLabel imageDisplay = new JLabel();
    private static JTextField nameField = new JTextField(20);
    private static byte[] imageBlob;

    private static int heldIndex;

    public static JDialog OpenPlaylistCreationMenu(JFrame parent, int index)
    {
        var dialog = OpenPlaylistCreationMenu(parent);
        heldIndex = index;
        playlistMakingDialog.setTitle("Edit Playlist");

        try {
            Playlist originalData = Database.findPlaylistById(index);

            nameField.setText(originalData.name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dialog;
    }


    public static JDialog OpenPlaylistCreationMenu(JFrame parent) {
        try {Database.initialize();} catch (Exception e) {e.printStackTrace();}

        nameField.setText("");

        imageDisplay.setVisible(false);


        playlistMakingDialog = new JDialog(parent, "Create New Playlist", true);
        playlistMakingDialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());

        // DialogButtons
        JPanel imageFunctions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectImage = new JButton("Select Image");
        selectImage.addActionListener(e -> openFileSelector());
        JButton removeImage = new JButton("Remove Image");

        imageFunctions.add(selectImage);
        imageFunctions.add(removeImage);

        // extField.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row

        formPanel.add(imageDisplay, gbc(c, 1, y++, 2));
        formPanel.add(imageFunctions, gbc(c, 1, y++, 2));
        formPanel.add(nameField, gbc(c, 1, y++, 2));

        // DialogButtons
        JPanel dialogButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        dialogButtons.add(ok);
        dialogButtons.add(cancel);

        ok.addActionListener(e -> submit());
        cancel.addActionListener(e -> playlistMakingDialog.dispose());

        c.gridwidth = 3;

        playlistMakingDialog.setSize(400, 200);
        playlistMakingDialog.add(formPanel, BorderLayout.CENTER);
        playlistMakingDialog.add(dialogButtons, BorderLayout.SOUTH);

        heldIndex = -1;

        return playlistMakingDialog;
    }

    public static File openFileSelector()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files",
            "png", "img", "jpg", "heic", "webp"
        );
        
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(playlistMakingDialog);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            tryingThis = chooser.getSelectedFile().getAbsolutePath();
            
            try {
                imageBlob = ImageUtils.getBytesFromFile(chooser.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // pathField.setText(chooser.getSelectedFile().getAbsolutePath());
        // nameField.setText(chooser.getSelectedFile().getName());
        // extField.setText(getExtensionOf(chooser.getSelectedFile().getName()));
        return chooser.getSelectedFile();
    }

    private static void submit() {
        Playlist info = new Playlist(
            nameField.getText()
        );
        

        // result = info;
        try {
            if(heldIndex == -1)
                Database.createPlaylist(info);
            else
                Database.updatePlaylistDetails(info, heldIndex);
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
