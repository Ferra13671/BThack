package com.ferra13671.BThack.api.Storage;


import com.ferra13671.BThack.api.SoundSystem.Music;
import com.ferra13671.BThack.api.SoundSystem.Sound;
import com.ferra13671.BThack.api.SoundSystem.TinySound;

public class MusicStorage {
    public static Sound buttonClicked;







    public static void init() {
        buttonClicked = TinySound.loadSound("assets/bthack/Sounds/buttonClicked.wav");
    }

    public static void playSound(Sound sound, double volume) {
        if (!TinySound.isInitialized()) return;

        sound.play(volume);
    }

    public static void playMusic(Music music, double volume, boolean loop) {
        if (!TinySound.isInitialized()) return;

        music.play(loop, volume);
    }
}
