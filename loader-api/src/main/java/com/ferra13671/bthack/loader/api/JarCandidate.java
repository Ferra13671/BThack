package com.ferra13671.bthack.loader.api;

import java.io.File;
import java.util.jar.JarFile;

public record JarCandidate(JarFile jarFile, File file) {
}
