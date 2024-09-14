package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Events.Entity.UseEntityEvent;
import com.ferra13671.BThack.Events.Events.GuiOpenEvent;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.Event;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Thread.ThreadManager;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.impl.Modules.CLIENT.FPSReducer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements Mc {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Final public Mouse mouse;

    @Shadow @Final private Window window;

    @Shadow public boolean skipGameRender;

    @Shadow @Final private SoundManager soundManager;

    @Shadow public abstract void updateWindowTitle();

    @Shadow private boolean disconnecting;

    @Shadow @Nullable public ClientWorld world;

    @Inject(method = "doItemUse",
            at = @At(value = "INVOKE", target = "net/minecraft/client/network/ClientPlayerInteractionManager.interactEntityAtLocation(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/hit/EntityHitResult;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void injectUseEntityCallback(CallbackInfo ci, Hand[] hands, int i1, int i2, Hand hand, ItemStack stack, EntityHitResult hitResult, Entity entity) {
        Event event = new UseEntityEvent(player, player.getEntityWorld(), hand, entity);
        BThack.EVENT_BUS.activate(event);

        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void modifyMinecraftInit(RunArgs args, CallbackInfo ci) {
        BThack.initLog("Starting to upload color themes....");
        try {
            ConfigInit.loadColourThemes();
            BThack.initLog("Color themes has loaded!");
        } catch (IOException e) {
            BThack.initErr("There was a error when loading color themes!");
            throw new RuntimeException(e);
        }

        BThack.initLog("Starting client initialization...");
        Client.startup();
        if (Client.inited) {
            BThack.initLog("Client initialized!");
        } else {
            BThack.initErr("There was an error during client initialization! Further work is impossible!");
            throw new RuntimeException();
        }

        BThack.instance.clickGui = new ClickGuiScreen(Text.of("ClickGui"));
        BThack.instance.mainMenu = new BThackMainMenuScreen();

        BThack.initLog("Starting loading the config...");
        try {
            ConfigInit.loadConfig();
            BThack.initLog("Config successfully uploaded!");
        } catch (Exception e) {
            BThack.initErr("There was an error when loading the config. Further work may occur with failures.");
            e.printStackTrace();
        }

        ThreadManager.startNewThread(thread -> {
            do {
                if (BThack.instance.clickGui != null) {
                    if (mc.currentScreen == BThack.instance.clickGui) {
                        BThack.instance.clickGui.clickGuiTick();
                    }
                }
                try {
                    thread.sleep(10);
                } catch (InterruptedException ignored) {
                }
            } while (true);
        });

        BThackRender.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Client.getModuleByName("Timer").setToggled(false);
            ConfigInit.saveConfig();
            try {
                ConfigInit.saveHudComponents();
            } catch (IOException ignored) {}
            BThack.log("Config Saved!");
        }));

        PluginSystem.getLoadedPlugins().forEach(Plugin::postInit);


        BThack.initLog("BThack is fully initialized and ready for further work. Enjoy your game!");
    }

    @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
    public void modifyGetFramerateLimit(CallbackInfoReturnable<Integer> cir) {
        if (Client.getModuleByName("FPSReducer").isEnabled()) {
            if (!mc.isWindowFocused()) {
                if (FPSReducer.lastFocusTicks <= 0)
                    cir.setReturnValue((int) FPSReducer.fpsLimit.getValue());
            }
        }
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    public void modifySetScreen(Screen screen, CallbackInfo ci) {
        GuiOpenEvent event = new GuiOpenEvent(screen);
        BThack.EVENT_BUS.activate(event);
        if (event.isCancelled()) {
            ci.cancel();
            return;
        }
        screen = event.getScreen();

        if (this.currentScreen != null) {
            this.currentScreen.removed();
        }

        if (screen == null && this.disconnecting) {
            throw new IllegalStateException("Trying to return to in-game GUI during disconnection");
        } else {
            if (screen == null && this.world == null) {
                screen = new TitleScreen();
            } else if (screen == null && this.player.isDead()) {
                if (this.player.showsDeathScreen()) {
                    screen = new DeathScreen(null, this.world.getLevelProperties().isHardcore());
                } else {
                    this.player.requestRespawn();
                }
            }

            this.currentScreen = screen;
            if (this.currentScreen != null) {
                this.currentScreen.onDisplayed();
            }

            BufferRenderer.reset();
            if (screen != null) {
                this.mouse.unlockCursor();
                KeyBinding.unpressAll();
                screen.init(mc, this.window.getScaledWidth(), this.window.getScaledHeight());
                this.skipGameRender = false;
            } else {
                this.soundManager.resumeAll();
                this.mouse.lockCursor();
            }

            this.updateWindowTitle();
        }

        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void onStartTick(CallbackInfo info) {
        BThack.EVENT_BUS.activate(new TickEvent.ClientTickEvent(TickEvent.Phase.START));
    }
    @Inject(at = @At("RETURN"), method = "tick")
    private void onEndTick(CallbackInfo info) {
        BThack.EVENT_BUS.activate(new TickEvent.ClientTickEvent(TickEvent.Phase.END));
    }
}
