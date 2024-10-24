package com.ferra13671.BThack.Core.FileSystem.ConfigSystem;

import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Utils.PathMode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigUtils {

    public static OutputStreamWriter createWriter(Path path) throws IOException {
        return new OutputStreamWriter(Files.newOutputStream(path), StandardCharsets.UTF_8);
    }

    public static String jsonToString(JsonObject object) {
        return ConfigSystem.gson.toJson(JsonParser.parseString(object.toString()));
    }

    public static BufferedReader createBufferedReader(InputStream stream, Charset charset) {
        CharsetDecoder decoder = charset.newDecoder();
        Reader reader = new InputStreamReader(stream, decoder);
        return new BufferedReader(reader);
    }

    public static InputStream newInputStream(String path, PathMode pathMode) {
        if (pathMode == PathMode.INSIDEJAR) {
            return ConfigUtils.class.getClassLoader().getResourceAsStream(path);
        } else {
            try {
                return Files.newInputStream(Paths.get(path));
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static void loadFromTxt(String fileName, String path, RunnableWithString runnable) throws IOException {
        Path savePath = Paths.get("BThack/" + path + "/" + fileName + ".txt");

        if (!Files.exists(savePath)) {
            FileSystem.registerFile(fileName, path, "txt");
            return;
        }

        BufferedReader reader = Files.newBufferedReader(savePath, StandardCharsets.UTF_8);
        String line = reader.readLine();
        while (line != null) {
            runnable.run(line);
            line = reader.readLine();
        }
        reader.close();
    }

    public static void saveInTxt(String fileName, String path, RunnableWithWriter runnable) throws IOException {
        Path savePath = Paths.get("BThack/" + path + "/" + fileName + ".txt");

        if (Files.exists(savePath)) {
            File file = savePath.toFile();
            file.delete();
            FileSystem.registerFile(fileName, path, "txt");
        }

        BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8);
        runnable.run(writer);
        writer.close();
    }

    public static interface RunnableWithString {
        void run(String str);
    }

    public static interface RunnableWithWriter {
        void run(BufferedWriter writer);
    }
}
