package com.ferra13671.BThack.mixins.gui_and_hud.chat;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.CommandSystem.CommandManager;
import com.ferra13671.BThack.api.Events.SendMessageEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.RENDER.BetterChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen implements Mc {

    @Shadow public abstract String normalize(String chatText);

    @Shadow protected TextFieldWidget chatField;

    //---------Chat Animation---------//
    @Unique
    private boolean wasOpenedLastFrame = false;
    @Unique
    private long lastOpenTime = 0;
    @Unique
    private float offsetY = 0;

    @Inject(method = "render", at = @At("HEAD"))
    private void modifyRenderPre(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (ModuleList.betterChat.isEnabled() && BetterChat.chatAnimation.getValue()) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                if (!wasOpenedLastFrame && !client.player.isSleeping()) {
                    wasOpenedLastFrame = true;
                    lastOpenTime = System.currentTimeMillis();
                }
            }


            float screenFactor = (float) client.getWindow().getHeight() / 1080;
            float timeSinceOpen = Math.min((float) (System.currentTimeMillis() - lastOpenTime), (float) BetterChat.fadeTime.getValue());
            float alpha = 1 - (timeSinceOpen / (float) BetterChat.fadeTime.getValue());

            float c1 = 1.70158f;
            float c3 = c1 + 1;
            float modifiedAlpha = c3 * alpha * alpha * alpha - c1 * alpha * alpha;

            offsetY = modifiedAlpha * (float) 8 * screenFactor; // 8 - fade offset

            context.getMatrices().translate(0, offsetY, 0);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void modifyRenderPost(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.getMatrices().translate(0, -offsetY, 0);
    }
    //--------------------------------//



    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    public void modifySendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        String text = normalize(chatText);
        if (text.startsWith(Client.clientInfo.getChatPrefix())) {
            CommandManager.parseCommand(chatText.replace(Client.clientInfo.getChatPrefix(), ""));
            mc.inGameHud.getChatHud().addToMessageHistory(chatText);

            cir.setReturnValue(true);
            return;
        }
        BThack.EVENT_BUS.activate(new SendMessageEvent(text));
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void modifyRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (ModuleList.passwordHider.isEnabled()) {
            if (chatField.getText().startsWith("/l ") || chatField.getText().startsWith("/login ") || chatField.getText().startsWith("/reg ") || chatField.getText().startsWith("/register ")) {
                BThackRender.guiGraphics.getMatrices().push();
                BThackRender.guiGraphics.getMatrices().translate(1, 1, 1000);
                BThackRender.drawRect(
                        7,
                        mc.getWindow().getScaledHeight() - 14,
                        mc.textRenderer.getWidth(chatField.getText()) + 8,
                        mc.getWindow().getScaledHeight() - 2,
                        Color.black.hashCode()
                );
                BThackRender.guiGraphics.getMatrices().pop();
            }
        }
    }
}
