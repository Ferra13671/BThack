package com.ferra13671.bthack.loader.api;

import java.io.File;
import java.io.InputStream;

public abstract class ClientLoader {
    private static ClientLoader instance = null;

    public abstract long getInitTime();

    public abstract boolean isDevelopmentEnvironment();

    public abstract BothMetadata getClientMetadata();

    public abstract ClientEntrypoint getClientEntrypoint();

    public abstract File getClientFolder();

    public static void setInstance(ClientLoader instance) {
        ClientLoader.instance = instance;
    }

    public static ClientLoader getInstance() {
        return instance;
    }

    public static InputStream getResource(ResourcePath path) {
        return ClientLoader.class.getClassLoader().getResourceAsStream("assets/".concat(path.namespace()).concat("/").concat(path.path()));
    }
}
