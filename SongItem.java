import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
// import java.awt.GridBagConstraints;
// import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import models.Media;

public class SongItem extends JPanel {
    private JPanel mainSongDetails;
    private JPanel coverContainer;
    private JButton playButton;
    private JLabel songImage;
    private JLabel songTitle;
    private JLabel songAuthor;

    private JPanel endSongDetails;
    private JLabel songLength;
    private JButton songOptions;

    private JPopupMenu contextMenu;
    private JMenuItem editMediaDetails;
    private JMenuItem addToPlaylist;
    private JMenuItem removeFromPlaylist;
    private JMenuItem deleteMedia;


    private void OpenContextMenu(int heldIndex)
    {
        JDialog dialog = MediaAddingMenu.OpenMediaAddingMenu(App.player, heldIndex);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true);
        // System.out.println(heldIndex);
    }

    private void OpenAddingToPlaylistMenu(Media heldMedia) {
        JDialog dialog = AddMediaToPlaylistMenu.OpenMediaAddingMenu(App.player, heldMedia);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true); // BLOCKS until dialog is closed
    }


    public SongItem(Media data) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        mainSongDetails = new JPanel();
        mainSongDetails.setLayout(new FlowLayout(FlowLayout.LEADING, 3, 0));
        mainSongDetails.setBorder(new EmptyBorder(2, 0, 0, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        // System.out.println(data.id);
        
        //Image part
        coverContainer = new JPanel();
        coverContainer.setLayout(new OverlayLayout(coverContainer));
        coverContainer.setPreferredSize(new Dimension(40, 40));
        
        songImage = new JLabel(ImageUtils.getResizedImage("Images/TempSongImage.png", 40)); //used to be new ImageIcon("TempSongImage.png") Just tested and this func doesn't seem to work, lol. I'll look through it again in a sec
        
        new SwingWorker<ImageIcon,Void>() {

            @Override
            protected ImageIcon doInBackground() throws Exception {
                try {
                    byte[] coverBytes = MediaFileHandler.extractCoverArt(data.path);

                    if(coverBytes != null)
                    {
                        BufferedImage image = ImageUtils.bytesToImage(coverBytes);
                        
                        // System.out.println(coverBytes.length);
                        if(image != null) {
                            ImageIcon icon = new ImageIcon(image);
                            return (ImageUtils.resizeImageIcon(icon, 40));
                        }
                    }
                    // var originalIcon = new ImageIcon(ImageUtils.bytesToImage(MediaFileHandler.extractCoverArt(data.path)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null; //nulls get ignored later, so we can chill with this
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if(icon != null) {
                        songImage.setIcon(icon);
                        songImage.revalidate();
                        songImage.repaint();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }.execute();
        

        songImage.setAlignmentX(.5f);
        songImage.setAlignmentY(.5f);

        playButton = new JButton(ImageUtils.getResizedImage("Images/whitePlayButton.png", 40));
        playButton.setAlignmentX(.5f);
        playButton.setAlignmentY(.5f);
        playButton.setMinimumSize(new Dimension(40, 40));
        playButton.setMaximumSize(new Dimension(40, 40));

        playButton.setContentAreaFilled(true);
        playButton.setBorderPainted(false);
        playButton.setBackground(new Color(20, 20, 20, 30));
        playButton.setVisible(false);

        playButton.setFocusable(false);
        playButton.setRolloverEnabled(false);

        coverContainer.add(playButton);
        coverContainer.add(songImage);

        coverContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                playButton.setVisible(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                playButton.setVisible(false);
            }
        });

        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                playButton.setVisible(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                playButton.setVisible(false);
            }
        });

        playButton.addActionListener(e -> plyaSong(data));
        

        songTitle = new JLabel(data.name);
        songTitle.setHorizontalAlignment(SwingConstants.LEADING);
        songTitle.setBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR));

        songAuthor = new JLabel(data.author); //Doesn't make anything appear if is empty...
        songAuthor.setHorizontalAlignment(SwingConstants.LEADING);
        songAuthor.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 0, 1, ColorScheme.PRIMARY_COLOR), new EmptyBorder(0, 5, 0, 0)));


        mainSongDetails.add(coverContainer);
        mainSongDetails.add(songTitle);
        mainSongDetails.add(songAuthor);

        endSongDetails = new JPanel();
        endSongDetails.setLayout(new FlowLayout(FlowLayout.TRAILING, 3, 0));
        endSongDetails.setBorder(new EmptyBorder(2, 0, 0, 10));
        endSongDetails.setPreferredSize(new Dimension(25, 45));

        songLength = new JLabel("00:00");

        songOptions = new JButton(ImageUtils.getResizedImage("Images/TresDots.png",11,15));
        songOptions.setPreferredSize(new Dimension(10, 40));
        songOptions.setContentAreaFilled(true);
        songOptions.setBorderPainted(false);


        endSongDetails.add(songLength);
        endSongDetails.add(songOptions);



        add(mainSongDetails);
        add(endSongDetails);



        contextMenu = new JPopupMenu();
        editMediaDetails = new JMenuItem("Edit Details");
        addToPlaylist = new JMenuItem("Add to Playlist");
        removeFromPlaylist = new JMenuItem("Remove from Playlist");
        deleteMedia = new JMenuItem("Delete Media");

        contextMenu.add(editMediaDetails);
        contextMenu.add(addToPlaylist);
        contextMenu.addSeparator();
        if(SongsMenu.heldPlaylist != null)
            contextMenu.add(removeFromPlaylist);
        contextMenu.add(deleteMedia);

        songOptions.addActionListener(e -> {contextMenu.show(songOptions, songOptions.getWidth() - contextMenu.getWidth(), songOptions.getHeight());});
        editMediaDetails.addActionListener(e -> OpenContextMenu(data.id));
        addToPlaylist.addActionListener(e -> OpenAddingToPlaylistMenu(data));

        if(SongsMenu.heldPlaylist != null)
        { String removeFromPlaylistMessage = "Remove " + data.name + " from " + SongsMenu.heldPlaylist.name + "?"; removeFromPlaylist.addActionListener(e -> OpenConfirmationMenu(removeFromPlaylistMessage, () -> RemoveSongFromPlaylist(data)));}
        String deleteConfirmationMessage = "Delete " + data.name + "?";
        deleteMedia.addActionListener(e -> OpenConfirmationMenu(deleteConfirmationMessage, () -> DeleteSong(data))); //Lambas in Java are weird, lol
    }

    public void OpenConfirmationMenu(String message, Runnable executable)
    {
        JDialog dialog = new ConfirmationPopUp(message, executable);
        dialog.setLocationRelativeTo(App.player);
        dialog.setVisible(true);
    }

    public void RemoveSongFromPlaylist(Media media)
    {
        try{
            Database.removeFromPlaylist(media.id, SongsMenu.heldPlaylist.id);
            SongsMenu.instance.Refresh();
        } catch(Exception error) {
            //Do nothing, lol
        }
    }

    public void DeleteSong(Media media)
    {
        // System.out.println("delete something");

        try{
            Database.deleteMedia(media.id);
        } catch(Exception error) {
            //Do nothing, lol
        }
    }

    public void plyaSong(Media media)
    {
        if(SongsMenu.heldPlaylist != null)
            MusicPlayer.getPlaylist(SongsMenu.allSongs);
        
        MusicPlayer.playTrack(media);
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = getParent();

        if (parent != null) {
            int totalWidth = parent.getWidth();
            return new Dimension(totalWidth, 45);
        }

        return super.getPreferredSize();
    }

    @Override
    public void doLayout()
    {
        super.doLayout();

        int totalWidth = getWidth() - 30;

        songTitle.setPreferredSize(new Dimension((int)(totalWidth * .275), 30));
        songAuthor.setPreferredSize(new Dimension((int)(totalWidth * .175), 30));
    }
}
