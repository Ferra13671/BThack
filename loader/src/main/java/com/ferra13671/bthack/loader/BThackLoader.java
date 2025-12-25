package com.ferra13671.bthack.loader;

import com.ferra13671.bthack.loader.api.BothMetadata;
import com.ferra13671.bthack.loader.api.ClientEntrypoint;
import com.ferra13671.bthack.loader.api.ClientLoader;
import com.ferra13671.bthack.loader.api.JarCandidate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class BThackLoader extends ClientLoader {
    private static final String CLIENT_METADATA_FILE_NAME = "client-metadata.json";
    private static final String IDE_CLIENT_CLASS_NAME = "com.ferra13671.bthack.BThackClient";

    private final File clientFolder = Paths.get("BThack").toFile();
    private final Logger logger = LoggerFactory.getLogger("BThack Loader");
    private boolean developmentEnvironment;
    private BothMetadata clientMetadata;
    private ClientEntrypoint entrypoint;
    private long initTime = 0L;

    public void load() {
        logger.info("Start loading BThack...");
        long startLoadTime = System.currentTimeMillis();

        checkIsDevelopmentEnvironment();
        loadEntrypoint();

        this.initTime = System.currentTimeMillis();
        this.logger.info(String.format("BThack was loaded successfully in %s ms.", this.initTime - startLoadTime));
    }

    @SneakyThrows
    private void loadEntrypoint() {
        if (isDevelopmentEnvironment()) {
            this.clientMetadata = BothMetadata.fromJson(
                    JsonParser.parseReader(new InputStreamReader(BThackLoader.class.getClassLoader().getResourceAsStream(CLIENT_METADATA_FILE_NAME))).getAsJsonObject(),
                    null
            );
            this.entrypoint = (ClientEntrypoint) BThackLoader.class.getClassLoader().loadClass(IDE_CLIENT_CLASS_NAME).getDeclaredConstructor().newInstance();
        } else {
            BothMetadata metadata = loadClientMetadata(findCandidates(getClientFolder())).orElseThrow();
            this.clientMetadata = metadata;
            Knot.getLauncher().addToClassPath(metadata.candidate().file().toPath());
            this.entrypoint = (ClientEntrypoint) getClass().getClassLoader().loadClass(metadata.entrypointPath()).getDeclaredConstructor().newInstance();
        }

        applyMixins(List.of(this.clientMetadata.mixinsPath()));
    }

    @SneakyThrows
    private void applyMixins(List<String> mixinPaths) {
        for (String mixin : mixinPaths)
            Mixins.addConfiguration(mixin);

        ClassLoader classLoader = getClass().getClassLoader();
        Field delegateField = classLoader.getClass().getDeclaredField("delegate");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(classLoader);

        Field mixinTransformerField = delegate.getClass().getDeclaredField("mixinTransformer");
        mixinTransformerField.setAccessible(true);

        Object mixinTransformer = mixinTransformerField.get(delegate);
        if (mixinTransformer.getClass().getName().equals("spongepowered")) {
            Field processorField = mixinTransformer.getClass().getDeclaredField("processor");
            processorField.setAccessible(true);

            Object processor = processorField.get(mixinTransformer);

            Field transformedCountField = processor.getClass().getDeclaredField("transformedCount");
            transformedCountField.setAccessible(true);
            transformedCountField.setInt(processor, 0);
        }
    }

    private Optional<BothMetadata> loadClientMetadata(List<JarCandidate> candidates) {
        for (JarCandidate candidate : candidates) {
            InputStream inputStream = null;

            try {
                inputStream = candidate.jarFile().getInputStream(new ZipEntry(CLIENT_METADATA_FILE_NAME));
            } catch (Exception ignored) {
            } finally {
                if (inputStream == null)
                    this.logger.debug(String.format("Jar file '%s' does not contain '%s'.", candidate.file().getName(), CLIENT_METADATA_FILE_NAME));
            }

            if (inputStream != null) {
                this.logger.info(String.format("Found '%s' in jar '%s', starting to parse.", CLIENT_METADATA_FILE_NAME, candidate.file().getName()));
                try {
                    JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
                    return Optional.of(BothMetadata.fromJson(jsonObject, candidate));
                } catch (Exception e) {
                    this.logger.error(String.format("Error throw when parsing '%s' from file '%s'.", CLIENT_METADATA_FILE_NAME, candidate.file().getName()));
                }
            }
        }
        return Optional.empty();
    }

    private List<JarCandidate> findCandidates(File folder) {
        List<JarCandidate> candidates = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files == null) {
            candidates.add(downloadJar(folder));
        } else {
            for (File file : files) {
                if (isJar(file))
                    try {
                        candidates.add(new JarCandidate(new JarFile(file), file));
                    } catch (Exception e) {
                        this.logger.error(String.format("Exception throw while analyzing '%s' file.", file.getName()));
                    }
            }
        }

        return candidates;
    }

    //TODO downloading jar
    private JarCandidate downloadJar(File folder) {
        throw new RuntimeException(new NoSuchFileException("Cannot find BThack jar."));
    }

    private boolean isJar(File file) {
        return file.isFile() && file.getName().endsWith(".jar");
    }

    private void checkIsDevelopmentEnvironment() {
        Class<?> clazz = null;
        try {
            clazz = getClass().getClassLoader().loadClass(IDE_CLIENT_CLASS_NAME);
        } catch (Exception ignored) {}

        this.developmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment() && clazz != null;
    }

    @Override
    public long getInitTime() {
        return this.initTime;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return this.developmentEnvironment;
    }

    @Override
    public BothMetadata getClientMetadata() {
        return this.clientMetadata;
    }

    @Override
    public ClientEntrypoint getClientEntrypoint() {
        return this.entrypoint;
    }

    @Override
    public File getClientFolder() {
        return this.clientFolder;
    }
}
