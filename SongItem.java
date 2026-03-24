import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SongItem extends JPanel {
    private JLabel songTitle = new JLabel();
    private JLabel songAuthor = new JLabel();

    public SongItem() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(songTitle);
        add(songAuthor);
    }
}
