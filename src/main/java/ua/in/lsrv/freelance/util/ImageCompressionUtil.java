package ua.in.lsrv.freelance.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageCompressionUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ImageCompressionUtil.class);

    public static byte[] compressImage(byte[] originalImage) {
        Deflater deflater = new Deflater();
        deflater.setInput(originalImage);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(originalImage.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error("Cannot compress image: " + e.getMessage());
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] image) {
        Inflater inflater = new Inflater();
        inflater.setInput(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(image.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOGGER.error("Cannot decompress image: " + e.getMessage());
        }
        return outputStream.toByteArray();
    }
}
