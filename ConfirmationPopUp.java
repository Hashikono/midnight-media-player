import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
// import java.util.function.Function;

import javax.swing.JButton;
// import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmationPopUp extends JDialog {
    
    public ConfirmationPopUp(JDialog parent, String message, Runnable exectuable)
    {
        super(javax.swing.SwingUtilities.getWindowAncestor(parent));

        setModal(true);
        JPanel formPanel = new JPanel(new GridBagLayout());
        setSize(175, 120);

        
        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row
        JLabel label = new JLabel(message);
        formPanel.add(label, gbc(c, 1, y++, 2));

        // DialogButtons
        JPanel dialogButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("Yes");
        JButton cancel = new JButton("No");

        dialogButtons.add(ok);
        dialogButtons.add(cancel);

        ok.addActionListener(e -> {exectuable.run();});
        ok.addActionListener(e -> dispose());
        cancel.addActionListener(e -> dispose());

        c.gridwidth = 3;


        add(formPanel, BorderLayout.CENTER);
        add(dialogButtons, BorderLayout.SOUTH);
        setAlwaysOnTop(true);
    }

    public ConfirmationPopUp(String message, Runnable exectuable)
    {
        JPanel formPanel = new JPanel(new GridBagLayout());
        setSize(175, 120);

        
        GridBagConstraints c = new GridBagConstraints();
        // c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // File row
        JLabel label = new JLabel(message);
        formPanel.add(label, gbc(c, 1, y++, 2));

        // DialogButtons
        JPanel dialogButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("Yes");
        JButton cancel = new JButton("No");

        dialogButtons.add(ok);
        dialogButtons.add(cancel);

        ok.addActionListener(e -> {exectuable.run();});
        ok.addActionListener(e -> dispose());
        cancel.addActionListener(e -> dispose());

        c.gridwidth = 3;


        add(formPanel, BorderLayout.CENTER);
        add(dialogButtons, BorderLayout.SOUTH);
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
