import java.awt.BasicStroke;
import java.awt.BorderLayout;
// import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
// import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicSliderUI;

public class MediaControlBar extends JPanel{
    JButton playButton;
    JButton nextButton;
    JButton prevButton;
    JButton repeatButton;
    JButton shuffleButton;

    JSlider musicProgress;
    JLabel currentSecond;
    JLabel songEndLength;
    
    // ========== HELPER METHOD: CREATE MODERN BUTTON ==========
    // Creates a custom styled button with rounded corners and hover effects
    private JButton createModernButton(String text, String tooltip, Color baseColor) {
        // Create button with custom painting
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Determine button color based on state
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = baseColor.darker();  // Darker when pressed
                } else if (getModel().isRollover()) {
                    buttonColor = baseColor.brighter();  // Brighter on hover
                } else {
                    buttonColor = baseColor;  // Normal state
                }
                
                // Draw rounded rectangle background
                g2.setColor(buttonColor);
                g2.fillRoundRect(0, 0, width - 1, height - 1, 15, 15);
                
                // Draw button text (centered)
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(getText())) / 2;  // Center horizontally
                int y = (height - fm.getHeight()) / 2 + fm.getAscent();  // Center vertically
                g2.drawString(getText(), x, y);
                
                // Draw border
                g2.setColor(baseColor.brighter());  // Slightly brighter border
                g2.setStroke(new BasicStroke(1.5f));  // 1.5 pixel border
                g2.drawRoundRect(1, 1, width - 3, height - 3, 15, 15);
                
                g2.dispose();
            }
        };
        
        // Set button properties
        button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));  // Icon font
        button.setPreferredSize(new Dimension(45, 40));  // Fixed size
        button.setToolTipText(tooltip);  // Tooltip text
        button.setContentAreaFilled(false);  // Don't use default background
        button.setBorderPainted(false);  // Don't paint default border
        button.setFocusPainted(false);  // Don't show focus rectangle
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Hand cursor on hover
        
        return button;
    }
    
    
    public MediaControlBar() {
        // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout(0, 1));
        setPreferredSize(new Dimension(0, 90)); // Was initially 0 before adding slider, want to find a way to get a similar feeling size while having the JSlider section
        setBorder(new MatteBorder(2, 0, 0, 0, new Color(60, 60, 65)));
        setBackground(ColorScheme.DARK_BG);

        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps
        bottomSection.setOpaque(false);
        // bottomSection.setPreferredSize(new Dimension(Integer.MAX_VALUE, 70));

        shuffleButton = createModernButton("🔀", "Shuffle", ColorScheme.LIGHT_BG);
        playButton = createModernButton("▶", "Play", ColorScheme.PRIMARY_COLOR);
        nextButton = createModernButton("⏭", "Next", ColorScheme.LIGHT_BG);
        prevButton = createModernButton("⏮", "Previous", ColorScheme.LIGHT_BG);
        repeatButton = createModernButton("🔁", "Repeat", ColorScheme.LIGHT_BG);

        bottomSection.add(shuffleButton);
        bottomSection.add(prevButton);
        bottomSection.add(playButton);
        bottomSection.add(nextButton);
        bottomSection.add(repeatButton);

        JPanel upperSection = new JPanel();
        upperSection.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps
        upperSection.setOpaque(false);
        // upperSection.setPreferredSize(new Dimension(Integer.MAX_VALUE, 30));

        currentSecond = new JLabel("00:00");
        currentSecond.setForeground(ColorScheme.TEXT_COLOR);
        songEndLength = new JLabel("2:00");
        songEndLength.setForeground(ColorScheme.TEXT_COLOR);

        musicProgress = new JSlider(0, 120); //Apparently you wanna start it from 0 to the amount of secs in a song, but I'll have to figure out how to do that later
        musicProgress.setPaintTicks(false); //Makes it smooth
        musicProgress.setPaintLabels(false); //Makes the line/dot thing follow your mouse better (think the setPaintTicks(false) also helped with this tho)
        musicProgress.setPaintTrack(true); //Basically what it says
        musicProgress.setFocusable(false);
        musicProgress.setOpaque(false);
        musicProgress.setPreferredSize(new Dimension(275, 10));
        musicProgress.putClientProperty("JSlider.isFilled", Boolean.TRUE);
        musicProgress.setDoubleBuffered(true);
        musicProgress.setBackground(ColorScheme.LIGHT_BG);
        musicProgress.setForeground(ColorScheme.PRIMARY_COLOR);

        musicProgress.setUI(new MusicSlider());

        musicProgress.addChangeListener(e -> {
            // if(musicProgress.getValueIsAdjusting())
            //     System.out.println(musicProgress.getValue()); //Yeah, this works

            musicProgress.repaint();
            setProgressCounter();
        });

        musicProgress.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int mouseXpos = e.getX();
                int width = musicProgress.getWidth();

                moveProgress(mouseXpos * musicProgress.getMaximum() / width); // use the value to then set the music point (musicProgress.getValue())
                // musicProgress.setValue(mouseXpos * musicProgress.getMaximum() / width); // use the value to then set the music point (musicProgress.getValue())

                //Some code to say this is being held down (so we can pause the music when you do so)
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // System.out.print("released");
            }
        });
        
        upperSection.add(currentSecond);
        upperSection.add(musicProgress);
        upperSection.add(songEndLength);

        add(bottomSection, BorderLayout.SOUTH);
        add(upperSection, BorderLayout.NORTH);

        playButton.addActionListener(e -> moveProgress(50));
    }

    public void moveProgress(int newSpot) //Use every second when playing music to move the bar
    {
        musicProgress.setValue(newSpot);

        setProgressCounter();
    }

    public void setNewSong(int songLength)
    {
        moveProgress(0);

        int seconds = songLength % 60;
        var secondText = "";

        if(seconds < 10)
            secondText = "0" + String.valueOf(seconds);
        else
            secondText = String.valueOf(seconds);
        
        var minutes = (songLength - seconds) / 60;
        songEndLength.setText(String.valueOf(minutes) + ":" + secondText);
    }

    public void setProgressCounter()
    {
        int newSpot = musicProgress.getValue();
        int seconds = newSpot % 60;
        var secondText = "";

        if(seconds < 10)
            secondText = "0" + String.valueOf(seconds);
        else
            secondText = String.valueOf(seconds);
        
        var minutes = (newSpot - seconds) / 60;
        currentSecond.setText(String.valueOf(minutes) + ":" + secondText);
    }

    public class MusicSlider extends BasicSliderUI
    {
        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int trackHeight = 4;
            int cy = trackRect.y + (trackRect.height / 2) - (trackHeight / 2);

            // FULL track (unplayed)
            g2.setColor(slider.getBackground());
            g2.fillRoundRect(trackRect.x, cy, trackRect.width, trackHeight, 4, 4);

            // PLAYED portion
            int playedWidth = thumbRect.x - trackRect.x;

            g2.setColor(slider.getForeground());
            g2.fillRoundRect(trackRect.x, cy, playedWidth, trackHeight, 4, 4);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 10;

            int x = thumbRect.x + (thumbRect.width - size) / 2; //Shifts it slightly to the left to meet the actual edge
            int y = thumbRect.y + (thumbRect.height - size) / 2; //Shifts it slightly down to meet the center height of the bar

            g2.setColor(Color.WHITE); 
            g2.fillOval(x, y, size, size);
        }


    }
}
