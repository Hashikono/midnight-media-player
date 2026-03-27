import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SongsMenu extends JPanel {
    public SongsMenu()
    {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setBackground(ColorScheme.DARK_BG);
        
        var title = new JLabel("Songs");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 25));
        title.setHorizontalAlignment(SwingConstants.LEADING);

        add(title, BorderLayout.NORTH);
    }
}
