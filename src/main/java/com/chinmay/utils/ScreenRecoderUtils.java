package com.chinmay.utils;

import org.monte.media.Format;
import org.monte.media.FormatKeys;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.chinmay.constants.FrameworkConstants.getVideoRecordingFilePathFilePath;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * ScreenRecoderUtils class extends ScreenRecorder to provide utility methods to record the screen.
 */
public class ScreenRecoderUtils extends ScreenRecorder {
    private final Object lock = new Object();
    private String fileName;
    private File currentFile;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

    /**
     * Constructs a new ScreenRecoderUtils object with the default screen device and configuration.
     *
     * @throws IOException  If an I/O error occurs.
     * @throws AWTException If an AWT error occurs.
     */
    public ScreenRecoderUtils() throws IOException, AWTException {
        super(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(),
                new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height),
                new Format(MediaTypeKey, FormatKeys.MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, FormatKeys.MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
                        Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                null,
                new File("./" + getVideoRecordingFilePathFilePath() + "/"));
    }

    /**
     * Returns a new file object with a unique name based on the provided filename. If a file with the same name already
     * <p>
     * exists, a counter is appended to the filename until a unique name is found.
     *
     * @param fileName The base filename to use for the new file object.
     * @return A new file object with a unique name.
     */
    private File getFileWithUniqueName(String fileName) {
        String extension = "";
        String name = "";

        int idxOfDot = fileName.lastIndexOf('.'); // Get the last index of . to separate extension
        extension = fileName.substring(idxOfDot + 1);
        name = fileName.substring(0, idxOfDot);

        Path path = Paths.get(fileName);
        int counter = 1;
        while (Files.exists(path)) {
            fileName = name + "-" + counter + "." + extension;
            path = Paths.get(fileName);
            counter++;
        }
        return new File(fileName);
    }

    /**
     * Creates a new movie file with a unique name in the movieFolder directory.
     *
     * @param fileFormat the format of the movie file
     * @return the created movie file
     * @throws IOException if there was an error creating the file
     */
    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        synchronized (lock) {
            if (!movieFolder.exists()) {
                movieFolder.mkdirs();
            } else if (!movieFolder.isDirectory()) {
                throw new IOException("" + movieFolder + "is not a directory.");
            }
            currentFile = getFileWithUniqueName(movieFolder.getAbsolutePath() + File.separator + fileName + "_" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
            return currentFile;
        }
    }

    /**
     * Starts recording the screen to a file with the given file name.
     *
     * @param fileName the name of the file to record to
     */
    public void startRecording(String fileName) {
        synchronized (lock) {
            this.fileName = fileName;
            try {
                start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Stops recording the screen and optionally deletes the recorded file.
     *
     * @param keepFile if true, the recorded file will be kept; if false, the recorded file will be deleted
     */
    public void stopRecording(boolean keepFile) {
        synchronized (lock) {
            try {
                stop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!keepFile) {
                deleteRecording();
            }
        }
    }

    /**
     * Deletes the current screen recording file.
     */
    public void deleteRecording() {
        synchronized (lock) {
            boolean deleted = false;
            try {
                if (currentFile.exists()) {
                    deleted = currentFile.delete();
                }
            } catch (Exception e) {
                // Prints the stack trace of the exception
                e.printStackTrace();
            }
            if (deleted) {
                currentFile = null;
            } else {
                System.out.println("Could not delete the screen record!");
            }
        }
    }
}
