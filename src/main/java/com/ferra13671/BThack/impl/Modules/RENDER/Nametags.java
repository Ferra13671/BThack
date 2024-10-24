package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.BThackRenderUtils;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Enemies.EnemiesUtils;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4d;

import java.awt.*;

public class Nametags extends Module {

    public static BooleanSetting players;
    public static BooleanSetting items;

    public Nametags() {
        super("Nametags",
                "lang.module.Nametags",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        players = new BooleanSetting("Players", this, true);
        items = new BooleanSetting("Items", this, true);

        initSettings(
                players,
                items
        );
    }

    @EventSubscriber
    public void onRenderHud(RenderHudPostEvent e) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity player && players.getValue() && player != mc.player) {
                renderPlayerNametag(player);
                continue;
            }
            if (entity instanceof ItemEntity itemEntity && items.getValue()) {
                renderItemNametag(itemEntity);
                continue;
            }
        }
    }

    public void renderItemNametag(ItemEntity itemEntity) {
        float[] cords = getNametagPos(itemEntity, 0);
        if (cords == null) return;

        BThackRender.drawItem(BThackRender.guiGraphics, itemEntity.getStack(), (int) cords[0] - 8, (int) cords[1] - 18, null, true);
        BThackRender.drawCenteredString(itemEntity.getName().getString(), cords[0], cords[1], -1, 0.8f);
    }

    public void renderPlayerNametag(PlayerEntity player) {
        float[] cords = getNametagPos(player, player.getHeight() + 0.3f);
        if (cords == null) return;

        float leftX = cords[0] - 100;
        float upY = cords[1] - 50;
        float rightX = cords[0] + 100;
        float downY = cords[1];

        BThackRender.guiGraphics.getMatrices().push();
        BThackRender.guiGraphics.getMatrices().translate(1,1,600);


        BThackRender.drawRect(leftX, upY, rightX, downY, new Color(0,0,0,150).hashCode());
        BThackRender.drawOutlineRect(leftX, upY, rightX, downY, 2, ColourUtils.rainbow(100));

        BThackRender.drawString(player.getDisplayName().getString(), leftX + 5, downY - mc.textRenderer.fontHeight - 3, -1, false, 1.1f);

        //---------Render Armor---------//
        float moveF = leftX + 5;
        for (int i = 3; i > -1; i--) {
            ItemStack stack = player.getInventory().getArmorStack(i);
            if (stack != null) {
                BThackRender.drawItem(BThackRender.guiGraphics, stack, (int) moveF, (int) upY + 5, null, true);
            }
            moveF += 20;
        }
        //------------------------------//

        moveF += 10;

        //---------Render hands(Main and Off)---------//
        BThackRender.drawString("L: ", moveF, upY + 10, -1, false);
        moveF += 12;
        if (player.getOffHandStack() != null) {
            BThackRender.drawItem(BThackRender.guiGraphics, player.getOffHandStack(), (int) moveF, (int) upY + 5, null, true);
        }

        moveF += 30;

        BThackRender.drawString("R: ", moveF, upY + 10, -1, false);
        moveF += 12;
        if (player.getMainHandStack() != null) {
            BThackRender.drawItem(BThackRender.guiGraphics, player.getMainHandStack(), (int) moveF, (int) upY + 5, null, false);
        }
        //--------------------------------------------//

        //---------Render Social Info---------//
        String socialText = (FriendsUtils.isFriend(player) ? Formatting.GREEN + "Friend" : "") + Formatting.RESET + (EnemiesUtils.isEnemy(player) ? Formatting.RED + (FriendsUtils.isFriend(player) ? "   " : "") + "Enemy" : "");
        BThackRender.drawString(socialText, leftX + 5, upY + 23, -1, false);
        //------------------------------------//

        //---------Render Hp---------//
        float hpLength = rightX - cords[0] - 20;
        double healthLength = rightX - hpLength - 5 + ((player.getHealth() / player.getMaxHealth()) * hpLength);
        BThackRender.drawString("HP: " + (int) player.getHealth(), rightX - hpLength - 20, upY + 23, -1);

        BThackRender.drawHorizontalGradientRect(rightX - hpLength - 5, upY + 23, rightX - 5, upY + 30, Color.red.hashCode(), Color.green.hashCode());
        BThackRender.drawRect((float) healthLength, upY + 23, rightX - 5, upY + 30, Color.black.hashCode());
        //---------------------------//

        BThackRender.guiGraphics.getMatrices().pop();
    }

    public void renderMobNametag(LivingEntity livingEntity) {

    }

    public float[] getNametagPos(Entity entity, float yPlus) {
        double x = entity.prevX + (entity.getX() - entity.prevX) * mc.getTickDelta();
        double y = entity.prevY + (entity.getY() - entity.prevY) * mc.getTickDelta();
        double z = entity.prevZ + (entity.getZ() - entity.prevZ) * mc.getTickDelta();
        Vec3d vector = new Vec3d(x, y + yPlus, z);

        Vector4d position;

        vector = BThackRenderUtils.worldCordsToScreenCords(vector);

        position = new Vector4d(vector.x, vector.y, vector.z, 0);
        position.x = Math.min(vector.x, position.x);
        position.y = Math.min(vector.y, position.y);
        position.z = Math.max(vector.x, position.z);


        if (vector.z > 0 && vector.z < 1) {
            position = new Vector4d(vector.x, vector.y, vector.z, 0);
            position.x = Math.min(vector.x, position.x);
            position.y = Math.min(vector.y, position.y);
            position.z = Math.max(vector.x, position.z);
        } else {
            return null;
        }

        //                      X                  Y
        return new float[]{(float) position.x, (float) position.y};
    }
}
