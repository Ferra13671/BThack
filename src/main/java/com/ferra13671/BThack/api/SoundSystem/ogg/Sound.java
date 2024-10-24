package com.ferra13671.BThack.api.SoundSystem.ogg;


import com.ferra13671.BThack.api.SoundSystem.SoundManager;

import java.io.File;

import static org.lwjgl.openal.AL10.*;

/**
 * A simplified {@link Music} class, more suitable for various short sounds or sound effects. The sound is only playable.
 *
 * @note Taken and modified from Ferra2DEngine
 */

public class Sound {
    private int bufferId;
    private int sourceId;
    private final String path;

    public Sound(File file) {
        this.path = file.getAbsolutePath();

        int[] data = SoundManager.getOggMusic(this.path, false);

        if (data != null) {
            this.bufferId = data[0];
            this.sourceId = data[1];
        }
    }

    /**
     * Plays back the current sound at the standard volume.
     */
    public void play() {
        this.play(1f);
    }

    /**
     * Plays back the current sound at the specified volume.
     *
     * @param volume   Value from 0 to 1.
     */
    public void play(float volume) {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_PLAYING) {
            alSourceStop(sourceId);
            alSourcei(sourceId, AL_POSITION, 0);
        }

        alSourcef(sourceId, AL_GAIN, volume);
        alSourcePlay(sourceId);
    }

    /**
     * Deletes the current sound.
     * Use this function when you no longer need the sound.
     *
     * @return   null.
     */
    public Sound delete() {
        alDeleteSources(sourceId);
        alDeleteBuffers(bufferId);

        return null;
    }

    /**
     * @return   The path to the .ogg file.
     */
    public String getPath() {
        return this.path;
    }

    public int getBufferId() {
        return this.bufferId;
    }

    public int getSourceId() {
        return this.sourceId;
    }
}
