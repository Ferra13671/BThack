package com.ferra13671.BThack.api.Utils;

import com.ferra13671.BTbot.api.Utils.Generate.GenerateNumber;
import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Discord.DiscordEventHandlers;
import com.ferra13671.BThack.api.Discord.DiscordRPC;
import com.ferra13671.BThack.api.Discord.DiscordRichPresence;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.SoundSystem.TinySound;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

public final class DiscordUtils implements Mc {
    private static DiscordRPC discordRPC;
    private static DiscordRichPresence discordRichPresence;

    private static String details;
    private static String state;

    private static boolean inited = false;

    public static void init() {
        if (!TinySound.isInitialized()) {
            return;
        }

        try {
            discordRPC = DiscordRPC.INSTANCE;
            discordRichPresence = new DiscordRichPresence();
            inited = true;
        } catch (Exception ignored) {
            inited = false;
        }
    }

    public static void startup() {
        if (!inited) return;
        BThack.log("Discord RPC is starting up!");

        DiscordEventHandlers handlers = new DiscordEventHandlers();

        discordRPC.Discord_Initialize(BThack.APP_ID, handlers, true, "");

        String imageKey = "bthack_icon";

        if (com.ferra13671.BThack.impl.Modules.CLIENT.DiscordRPC.secret.getValue()) {
            int percent = GenerateNumber.generateInt(1, 100);
            if (percent > 0 && percent <= 10) {
                imageKey = "hentai_face1";
            } else if (percent > 10 && percent <= 20) {
                imageKey = "hentai_face2";
            } else if (percent > 20 && percent <= 30) {
                imageKey = "hentai_face3";
            }
        }

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.largeImageKey = imageKey;
        discordRichPresence.largeImageText = "BThack client";

        discordRPC.Discord_UpdatePresence(discordRichPresence);

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    details = "Version " + BThack.VERSION;
                    state = "Main Menu";

                    if (mc.isIntegratedServerRunning()) {
                        state = "In singleplayer";
                    } else if (mc.currentScreen instanceof MultiplayerScreen) {
                        state = "Multiplayer Menu";
                    } else if (mc.currentScreen instanceof SelectWorldScreen) {
                        state = "Singleplayer Menu";
                    } else if (mc.getCurrentServerEntry() != null) {
                        state = "Server | " + mc.getCurrentServerEntry().address.toLowerCase();
                    }

                    discordRichPresence.details = details;
                    discordRichPresence.state = state;

                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                } catch (Exception exception) {
                    exception.printStackTrace();
                } try {
                    Thread.sleep(5000L);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }, "RPC-Callback-Handler").start();
    }

    public static void shutdown() {
        if (!inited) return;
        BThack.log("Discord RPC is shutting down!");

        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
