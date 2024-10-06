package com.ferra13671.BThack.api.SoundSystem;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

/**
 * Sound manager needed to work with sound files.
 * The manager uses openAL to play sounds, so initialization of the manager takes place during window creation.
 *
 * @note Taken and modified from Ferra2DEngine
 */

public class SoundManager {

    /**
     * Converts and prepares the .ogg file for further playback.
     *
     * @param path   File path.
     * @param loop   Whether the sound is looped or not.
     * @return   bufferId and sourceId needed for further work with sound.
     */
    public static int[] getOggMusic(String path, boolean loop) {
        int[] musicData = {0,0}; // bufferId , sourceId

        // Allocate space to store the return information from stb
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            stackPop();
            stackPop();
            return null;
        }

        // Retrieve the extra information that was stored in the buffers by stb
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        // Free
        stackPop();
        stackPop();

        // Find the correct openAL format
        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        musicData[0] = alGenBuffers();
        alBufferData(musicData[0], format, rawAudioBuffer, sampleRate);

        // Generate the source
        musicData[1] = alGenSources();
        alSourcei(musicData[1], AL_BUFFER, musicData[0]);
        alSourcei(musicData[1], AL_LOOPING, loop ? 1 : 0);
        alSourcei(musicData[1], AL_POSITION, 0);
        // Sound Volume
        alSourcef(musicData[1], AL_GAIN, 1f);

        // Free stb raw audio buffer
        free(rawAudioBuffer);

        return musicData;
    }
}
