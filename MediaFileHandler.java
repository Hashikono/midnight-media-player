// import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import models.Media;

public class MediaFileHandler {
    private static final String FFMPEG_PATH = "ffmpeg/ffmpeg.exe";

    public static void readFile(String path)
    {
        //do your stuff here, might also be able to pull some other tricks tho if we decide to switch this to grabbing media objects cuz it also stores the extension type
    }

    public static byte[] extractCoverArt(String mediaPath) { //I don't fully understand this because it was mostly done by chatGPT, but it's an edit it made of something I'd already made some work on
        try {
            ProcessBuilder pb = new ProcessBuilder(
            FFMPEG_PATH,
            "-i", mediaPath,
            "-map", "0:v:0",
            "-c:v", "mjpeg",
            "-f", "image2pipe",
            "pipe:1"
        );

        // DO NOT merge streams
        // pb.redirectErrorStream(true); ← REMOVE

        Process process = pb.start();

        //consume stderr separately so ffmpeg doesn't hang
        new Thread(() -> {
            try (InputStream err = process.getErrorStream()) {
                err.transferTo(OutputStream.nullOutputStream());
            } catch (Exception ignored) {}
        }).start();

        //Only read actual image data from stdout
        InputStream is = process.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] temp = new byte[4096];
        int read;
        while ((read = is.read(temp)) != -1) {
            buffer.write(temp, 0, read);
        }

        process.waitFor();

        byte[] imageBytes = buffer.toByteArray();
        return imageBytes.length > 0 ? imageBytes : null;

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