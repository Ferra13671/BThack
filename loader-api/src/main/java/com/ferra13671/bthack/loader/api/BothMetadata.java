package com.ferra13671.bthack.loader.api;

import com.google.gson.JsonObject;

public record BothMetadata(String id, String entrypointPath, String mixinsPath, JarCandidate candidate) {

    public static BothMetadata fromJson(JsonObject jsonObject, JarCandidate jarCandidate) {
        return new BothMetadata(
                jsonObject.get("id").getAsString(),
                jsonObject.get("entrypoint").getAsString(),
                jsonObject.get("mixins").getAsString(),
                jarCandidate
        );
    }
}
