import java.awt.BorderLayout;
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
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MediaAddingMenu {
    public static JDialog mediaAddingDialog;
    public static String tryingThis;

    
    private static JTextField fileField = new JTextField(20);
    private static JTextField nameField = new JTextField(20);
    private static JTextField extField = new JTextField(6);
    private static JTextField authorField = new JTextField(20);
    private static JTextField albumField = new JTextField(20);


    public static JDialog OpenMediaAddingMenu(JFrame parent) {
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
        formPanel.add(fileField, gbc(c, 1, y));
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
        // JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        // ok.addActionListener(e -> submit());
        // cancel.addActionListener(e -> dispose());

        // buttons.add(ok);
        // buttons.add(cancel);

        c.gridwidth = 3;
        // add(buttons, gbc(c, 0, y));


        // openFileSelector();

        // JLabel label = new JLabel(tryingThis, SwingConstants.CENTER);
        // JButton ok = new JButton("OK");

        // ok.addActionListener(e -> mediaAddingDialog.dispose()); // closes dialog

        // mediaAddingDialog.add(label, BorderLayout.CENTER);
        // mediaAddingDialog.add(ok, BorderLayout.SOUTH);

        mediaAddingDialog.setSize(400, 200);
        mediaAddingDialog.add(formPanel, BorderLayout.CENTER);

        return mediaAddingDialog;
    }

    public static File openFileSelector()
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mediaAddingDialog);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
                    
            tryingThis = chooser.getSelectedFile().getAbsolutePath();
        }


        return chooser.getSelectedFile();
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
