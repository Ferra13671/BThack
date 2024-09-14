package com.ferra13671.BThack.Core.FileSystem.ConfigInit;

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
        return ConfigInit.gson.toJson(JsonParser.parseString(object.toString()));
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
}
