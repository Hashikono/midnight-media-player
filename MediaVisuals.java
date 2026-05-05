import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;

public class MediaVisuals extends JPanel {

    public static EmbeddedMediaListPlayerComponent visualizer;

    public MediaVisuals()
    {
        setSize(1000, 10);
        setLayout(new BorderLayout(10, 7));
        setBorder(new EmptyBorder(12, 12, 12, 20));
        setBackground(ColorScheme.DARK_BG);

        add(visualizer);

        // MusicPlayer.syncVisuals();
    }
}
