import java.io.File;
import models.Media;

public class MediaFileHandler {
    public static void readFile(String path)
    {
        //do your stuff here, might also be able to pull some other tricks tho if we decide to switch this to grabbing media objects cuz it also stores the extension type
    }

    public static void getAudioCover(Media song)
    {

    }

    public static String getMetadataJson(String filePath) throws Exception {
        String ffprobePath = "ffmpeg/ffprobe.exe";

        ProcessBuilder pb = new ProcessBuilder(
            ffprobePath,
            "-v", "quiet",
            "-print_format", "json",
            "-show_format",
            "-show_streams",
            filePath
        );

        pb.redirectErrorStream(true);

        Process process = pb.start();

        String output = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        return output;
    }
}