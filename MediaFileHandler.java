import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;

import models.Media;

public class MediaFileHandler {
    public static void readFile(String path)
    {
        //do your stuff here, might also be able to pull some other tricks tho if we decide to switch this to grabbing media objects cuz it also stores the extension type
    }

    public static void getAudioCover(Media song) throws Exception
    {
        File file = new File(song.path);
        AudioFile  audioFile = AudioFileIO.read(file);
        Tag tag = audioFile.getTag();

        
    }
}
