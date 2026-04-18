import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import models.Media;

public class MediaAddingMenu {
    public static JDialog mediaAddingDialog;
    public static String tryingThis;

    
    private static JTextField pathField = new JTextField(20);
    private static JTextField nameField = new JTextField(20);
    private static JTextField extField = new JTextField(6);
    private static JTextField authorField = new JTextField(20);
    private static JTextField albumField = new JTextField(20);

    private static Media result = null;

    private static int heldIndex;

    public static JDialog OpenMediaAddingMenu(JFrame parent, int index)
    {
        var mediaAddingDialog = OpenMediaAddingMenu(parent);
        heldIndex = index;
        mediaAddingDialog.setTitle("Edit Media");

        try {
            Media originalData = Database.findMediaById(index);

            pathField.setText(originalData.path);
            nameField.setText(originalData.name);
            extField.setText(originalData.format);
            albumField.setText(originalData.album);
            authorField.setText(originalData.author);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaAddingDialog;
    }

    public static JDialog OpenMediaAddingMenu(JFrame parent) {
        try {Database.initialize();} catch (Exception e) {e.printStackTrace();}

        pathField.setText("");
        nameField.setText("");
        extField.setText("");
        authorField.setText("");
        albumField.setText("");


        mediaAddingDialog = new JDialog(parent, "Add Media", true);

        JPanel formPanel = new JPanel(new GridBagLayout());
        mediaAddingDialog.setLayout(new BorderLayout());



        extField.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row
        formPanel.add(new JLabel("File:"), gbc(c, 0, y));
        formPanel.add(pathField, gbc(c, 1, y));
        JButton browse = new JButton("Browse...");
        browse.addActionListener(e -> openFileSelector());
        formPanel.add(browse, gbc(c, 2, y++));

        // Name
        formPanel.add(new JLabel("Name:"), gbc(c, 0, y));
        formPanel.add(nameField, gbc(c, 1, y++, 2));

        // Extension
        formPanel.add(new JLabel("Extension:"), gbc(c, 0, y));
        formPanel.add(extField, gbc(c, 1, y++, 2));

        // Author
        formPanel.add(new JLabel("Author:"), gbc(c, 0, y));
        formPanel.add(authorField, gbc(c, 1, y++, 2));

        // Album
        formPanel.add(new JLabel("Album:"), gbc(c, 0, y));
        formPanel.add(albumField, gbc(c, 1, y++, 2));

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(e -> submit());
        cancel.addActionListener(e -> mediaAddingDialog.dispose());

        // buttons.add(ok);
        // buttons.add(cancel);

        c.gridwidth = 3;

        mediaAddingDialog.setSize(400, 200);
        mediaAddingDialog.add(formPanel, BorderLayout.CENTER);
        mediaAddingDialog.add(buttons, BorderLayout.SOUTH);

        heldIndex = -1;
        // System.out.println(AudioFileIO.class.getName());

        return mediaAddingDialog;
    }

    private static void submit() {
        Media info = new Media(
            pathField.getText(),
            nameField.getText(),
            extField.getText(),
            authorField.getText(),
            albumField.getText()
        );
        

        // result = info;
        try {
            if(heldIndex == -1)
                Database.insertMedia(info);
            else
                Database.updateMedia(info, heldIndex);
            
            SongsMenu.instance.Refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaAddingDialog.dispose();
    }

    public static File openFileSelector()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Audio & Video Files",
            // Audio
            "mp3", "wav", "flac", "ogg", "aac", "m4a",
            // Video
            "mp4", "mkv", "avi", "mov", "webm"
        );
        
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mediaAddingDialog);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            // System.out.println("You chose to open this file: " +
            //         chooser.getSelectedFile().getName());
                    
            tryingThis = chooser.getSelectedFile().getAbsolutePath();

            pathField.setText(chooser.getSelectedFile().getAbsolutePath());
            nameField.setText(chooser.getSelectedFile().getName());

            String ext = getExtensionOf(chooser.getSelectedFile().getName());
            extField.setText(ext);

            try {
                if(ext != null)
                {
                    if(ext.equals("mp3") || ext.equals("wav"))
                        getMediaDetails(chooser.getSelectedFile().getAbsolutePath());
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return chooser.getSelectedFile();
    }

    public static String getExtensionOf(String fileName) {
        String extension = "";

        int dot = fileName.lastIndexOf('.');
        if(dot > 0 && dot < fileName.length() - 1) {
            extension = fileName.substring(dot + 1).toLowerCase();
        }

        return extension;
    }

    
    public static void getMediaDetails(String path)
    {
        try {
            File file = new File(path);
            AudioFile  audioFile = AudioFileIO.read(file);
            Tag tag = audioFile.getTag();

            if(tag == null)
                return;
            
            String authorName = tag.getFirst("ARTIST");
            String albumName = tag.getFirst("ALBUM");

            if(authorName != null && !authorName.isEmpty())
                authorField.setText(authorName);
            
            if(albumName != null && !albumName.isEmpty())
                albumField.setText(albumName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
