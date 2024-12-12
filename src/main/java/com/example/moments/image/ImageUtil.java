package com.example.moments.image;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {

    public static byte[] compressImage(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty");
        }

        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to compress image", e);
        }
    }

    public static byte[] decompressImage(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be null or empty");
        }

        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] tmp = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to decompress image", e);
        }
    }
}
