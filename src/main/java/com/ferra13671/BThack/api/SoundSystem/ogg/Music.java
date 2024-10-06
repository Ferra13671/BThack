package com.ferra13671.BThack.api.SoundSystem.ogg;

import com.ferra13671.BThack.api.SoundSystem.MusicState;
import com.ferra13671.BThack.api.SoundSystem.SoundManager;

import java.io.File;

import static org.lwjgl.openal.AL10.*;

/**
 * A class for loading .ogg music and further work with it.
 *
 * @note Taken and modified from Ferra2DEngine
 */

public class Music {
    private int bufferId;
    private int sourceId;
    private final String path;

    private MusicState state = MusicState.STOPPED;

    public Music(File file, boolean loop) {
        this.path = file.getAbsolutePath();

        int[] data = SoundManager.getOggMusic(this.path, loop);

        if (data != null) {
            this.bufferId = data[0];
            this.sourceId = data[1];
        }
    }

    /**
     * Plays current music.
     * <p>
     * If the music has been paused, playback will start from the beginning,
     * and if the music has been paused, playback will start at the pause point.
     */
    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            this.state = MusicState.STOPPED;
            alSourcei(sourceId, AL_POSITION, 0);
        }
        if (state == AL_PAUSED) {
            this.state = MusicState.PAUSED;
        }

        if (this.state == MusicState.STOPPED || this.state == MusicState.PAUSED) {
            alSourcePlay(sourceId);
            this.state = MusicState.PLAYING;
        }
    }

    /**
     * Stops the current music.
     * The next playback will start from the beginning.
     */
    public void stop() {
        if (this.state == MusicState.PLAYING) {
            alSourceStop(sourceId);
            this.state = MusicState.STOPPED;
        }
    }

    /**
     * Pauses the current music.
     * The next playback will start from the pause point.
     */
    public void pause() {
        if (this.state == MusicState.PLAYING) {
            alSourcePause(sourceId);
            this.state = MusicState.PAUSED;
        }
    }

    /**
     * Sets the volume of the current music.
     *
     * @param volume   Value from 0 to 1.
     */
    public void setVolume(float volume) {
        alSourcef(sourceId, AL_GAIN, volume);
    }

    /**
     * Deletes the current music.
     * Use this function when you no longer need the music.
     *
     * @return   null.
     */
    public Music delete() {
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

    /**
     * Gets the current state of the music, and if the music is playing, returns true, otherwise false.
     *
     * @return   Whether music is played, or not.
     */
    public boolean isPlaying() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);

        if (state == AL_STOPPED) {
            this.state = MusicState.STOPPED;
        } else if (state == AL_PAUSED) {
            this.state = MusicState.PAUSED;
        }
        return this.state == MusicState.PLAYING;
    }

    public int getBufferId() {
        return this.bufferId;
    }

    public int getSourceId() {
        return this.sourceId;
    }
}