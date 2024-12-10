package com.ferra13671.BThack.mixins.gui_and_hud.chat;

import com.ferra13671.BThack.Core.Client.Client;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.impl.Modules.MISC.MoreChatHistory;
import com.ferra13671.BThack.impl.Modules.RENDER.BetterChat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud {


    @Shadow public abstract int getWidth();

    @Shadow public abstract double getChatScale();

    @Shadow protected abstract boolean isChatFocused();

    @Shadow private int scrolledLines;

    @Shadow private boolean hasUnreadNewMessages;

    @Shadow public abstract void scroll(int scroll);

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @Shadow @Final private List<ChatHudLine> messages;

    @Shadow @Final private MinecraftClient client;

    //---------Chat Animation---------//
    @Shadow private int getLineHeight() {
        return 0;
    }

    @ModifyArg(method = "render", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0))
    private float applyYOffset(float y) {
        if (ModuleList.betterChat.isEnabled() && BetterChat.chatAnimation.getValue()) {
            // Apply the offset
            ModuleList.betterChat.calculateYOffset(getLineHeight(), scrolledLines);

            // Raised mod compatibility
            if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
                // for Raised 1.2.0+
                y -= distance;
            } else if (FabricLoader.getInstance().getObjectShare().get("raised:distance") instanceof Integer distance) {
                y -= distance;
            }

            return y + BetterChat.chatDisplacementY;
        } else return y;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("TAIL"))
    private void addMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        BetterChat.messageTimestamps.add(0, System.currentTimeMillis());
        while (BetterChat.messageTimestamps.size() > visibleMessages.size()) {
            BetterChat.messageTimestamps.remove(BetterChat.messageTimestamps.size() - 1);
        }
    }
    //--------------------------------//


    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At("HEAD"), cancellable = true)
    public void modifyAddMessage(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        ci.cancel();

        int i = MathHelper.floor((double) getWidth() / getChatScale());
        if (indicator != null && indicator.icon() != null) {
            i -= indicator.icon().width + 4 + 2;
        }

        List<OrderedText> list = ChatMessages.breakRenderedChatMessageLines(message, i, client.textRenderer);
        boolean bl = isChatFocused();

        for(int j = 0; j < list.size(); ++j) {
            OrderedText orderedText = list.get(j);
            if (bl && scrolledLines > 0) {
                hasUnreadNewMessages = true;
                scroll(1);
            }

            boolean bl2 = j == list.size() - 1;
            visibleMessages.add(0, new ChatHudLine.Visible(ticks, orderedText, indicator, bl2));
        }

        while(visibleMessages.size() > getMaxChatSize()) {
            visibleMessages.remove(visibleMessages.size() - 1);
        }

        if (!refresh) {
            messages.add(0, new ChatHudLine(ticks, message, signature, indicator));

            while(messages.size() > getMaxChatSize()) {
                messages.remove(messages.size() - 1);
            }
        }
    }

    @Unique
    public int getMaxChatSize() {
        if (Client.inited && ModuleList.moreChatHistory.isEnabled()) return (int) MoreChatHistory.size.getValue();
        else return 100;
    }
}
