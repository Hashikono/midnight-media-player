// import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import models.Media;

public class MediaFileHandler {
    private static final String FFMPEG_PATH = "ffmpeg/ffmpeg.exe";

    public static void readFile(String path)
    {
        //do your stuff here, might also be able to pull some other tricks tho if we decide to switch this to grabbing media objects cuz it also stores the extension type
    }

    public static byte[] extractCoverArt(String mediaPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                FFMPEG_PATH,
                "-i", mediaPath,
                "-an",
                "-vcodec", "mjpeg",
                "-f", "image2pipe",
                "pipe:1"
            );

            pb.redirectErrorStream(true); // merge stderr (ffmpeg logs)

            Process process = pb.start();

            InputStream is = process.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            byte[] temp = new byte[4096];
            int read;
            while ((read = is.read(temp)) != -1) {
                buffer.write(temp, 0, read);
            }

            process.waitFor();

            byte[] imageBytes = buffer.toByteArray();

            // No cover art case
            if (imageBytes.length == 0) {
                return null;
            }

            return imageBytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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