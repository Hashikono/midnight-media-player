import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class NavBar extends JPanel{

    public NavBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(60, 0));
        setBorder(new MatteBorder(0, 0, 0, 2, new Color(60, 60, 65)));
        setBackground(ColorScheme.DARK_BG);
    }
}
