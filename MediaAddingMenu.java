import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MediaAddingMenu {
    public static JDialog OpenMediaAddingMenu(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Modal Dialog", true);

        dialog.setSize(400, 600);
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("You must respond before continuing", SwingConstants.CENTER);
        JButton ok = new JButton("OK");

        ok.addActionListener(e -> dialog.dispose()); // closes dialog

        dialog.add(label, BorderLayout.CENTER);
        dialog.add(ok, BorderLayout.SOUTH);

        return dialog;
    } 
}
