package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Events.Events.Render.RenderHudPostEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.Enemies.EnemiesUtils;
import com.ferra13671.BThack.api.Social.Friends.FriendsUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import net.minecraft.client.util.Window;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.*;

public class Radar extends Module {

    public static NumberSetting opacity;
    public static NumberSetting scale;
    public static NumberSetting range;
    public static BooleanSetting outline;

    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting animals;


    private static final Color mobColor = new Color(255, 255, 0);
    private static final Color animalColor = new Color(0, 255, 0);
    private static final Color friendColor = new Color(0, 225, 255);
    private static final Color enemyColor = new Color(255, 0, 0);

    public Radar() {
        super("Radar",
                "lang.module.Radar",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        opacity = new NumberSetting("Opacity", this, 0.5, 0.05, 1, false);
        scale = new NumberSetting("Scale", this, 100, 100, 150, true);
        range = new NumberSetting("Range", this, 100, 50, 200, true);
        outline = new BooleanSetting("Outline Rect", this, false);

        players = new BooleanSetting("Players", this, true);
        mobs = new BooleanSetting("Mobs", this, true);
        animals = new BooleanSetting("Animals", this, true);

        initSettings(
                opacity,
                scale,
                range,
                outline,
                players,
                mobs,
                animals
        );
    }

    @EventSubscriber
    public void onRender(RenderHudPostEvent e) {
        Window sr = mc.getWindow();
        Color rectColor = new Color(0, 0, 0, (float) opacity.getValue());

        //Very strong math, yeeaah.
        float yaw = (mc.player.yaw / 360);
        yaw = yaw - (float)Math.floor(yaw);
        yaw = yaw * 360;

        float _range = (float) range.getValue();
        float _scale = (float) scale.getValue();

        //Drawing a radar map
        BThackRender.drawRect(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - (int) _scale, sr.getScaledHeight() - (int) _scale, rectColor.hashCode());
        BThackRender.drawTriangle(sr.getScaledWidth() - (_scale / 2), (int)((sr.getScaledHeight() - (_scale / 2)) + 3), 4, -yaw, -1);
        BThackRender.drawCenteredString("X-", sr.getScaledWidth() - 6, sr.getScaledHeight() - ((int) _scale / 2), -1);
        BThackRender.drawString("X+", (int)(sr.getScaledWidth() - _scale) + 2, sr.getScaledHeight() - ((int) _scale / 2), -1);
        BThackRender.drawCenteredString("Z+", sr.getScaledWidth() - (int)(_scale / 2), (sr.getScaledHeight() - (int) _scale) + 1, -1);
        BThackRender.drawCenteredString("Z-", sr.getScaledWidth() - (int)(_scale / 2), sr.getScaledHeight() - 9, -1);
        //////

        if (outline.getValue()) {
            BThackRender.drawOutlineRect(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - (int) _scale, sr.getScaledHeight() - (int) _scale, 1, -1);
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player != mc.player) {
                double posX = player.getX();
                double posZ = player.getZ();
                double radarPosX = mc.player.getX() - posX;
                double radarPosZ = mc.player.getZ() - posZ;

                if (radarPosX < (int) _range && radarPosZ < (int) _range) {

                    radarPosX = ((sr.getScaledWidth() - (sr.getScaledWidth() - _scale)) / 100) * ((radarPosX / _range) * 100);
                    radarPosZ = ((sr.getScaledHeight() - (sr.getScaledHeight() - _scale)) / 100) * ((radarPosZ / _range) * 100);

                    int x = sr.getScaledWidth() - ((int) _scale / 2);
                    int y = sr.getScaledHeight() - ((int) _scale / 2);

                    x = x + (int) radarPosX;
                    y = y + (int) radarPosZ;

                    if (isCurrentCords(x,y, (sr.getScaledWidth() - (int) _scale) + 1, sr.getScaledWidth() - 1, (sr.getScaledHeight() - (int) _scale) + 1, sr.getScaledHeight() - 1)) {
                        if (FriendsUtils.isFriend(player)) {
                            BThackRender.drawSquare(x, y, 1, friendColor.hashCode());
                        } else if (EnemiesUtils.isEnemy(player)) {
                            BThackRender.drawSquare(x, y, 1, enemyColor.hashCode());
                        } else if (ClansUtils.isAlly(player)) {
                            Clan clan = ClansUtils.getFirstClanFromMember(player.getDisplayName().getString());

                            if (clan != null) {
                                BThackRender.drawSquare(x,y,3, -1);
                                Color color = new Color(clan.getR(), clan.getG(), clan.getB());
                                BThackRender.drawSquare(x,y,2, color.hashCode());
                            } else {
                                BThackRender.drawSquare(x, y, 1, -1);
                            }
                        } else if (players.getValue()) {
                            BThackRender.drawSquare(x, y, 1, -1);
                        }
                    }
                }
            }
        }
        for (Entity entity : mc.world.getEntities()) {
            double posX = entity.getX();
            double posZ = entity.getZ();
            double radarPosX = mc.player.getX() - posX;
            double radarPosZ = mc.player.getZ() - posZ;

            if (radarPosX < (int) _range && radarPosZ < (int) _range) {
                radarPosX = ((sr.getScaledWidth() - (sr.getScaledWidth() - _scale)) / 100) * ((radarPosX / _range) * 100);
                radarPosZ = ((sr.getScaledHeight() - (sr.getScaledHeight() - _scale)) / 100) * ((radarPosZ / _range) * 100);

                int x = sr.getScaledWidth() - ((int) _scale / 2);
                int y = sr.getScaledHeight() - ((int) _scale / 2);

                x = x + (int) radarPosX;
                y = y + (int) radarPosZ;

                if (isCurrentCords(x,y, (sr.getScaledWidth() - (int) _scale) + 1, sr.getScaledWidth() - 1, (sr.getScaledHeight() - (int) _scale) + 1, sr.getScaledHeight() - 1)) {
                    if ((entity instanceof MobEntity || entity instanceof GolemEntity) && mobs.getValue()) {
                        BThackRender.drawSquare(x, y, 1, mobColor.hashCode());
                    } else if ((entity instanceof WaterCreatureEntity || entity instanceof PassiveEntity) && animals.getValue()) {
                        BThackRender.drawSquare(x, y, 1, animalColor.hashCode());
                    }
                }
            }
        }
    }

    private boolean isCurrentCords(int checkX, int checkY, int needMinX, int needMaxX, int needMinY, int needMaxY) {
        return checkX > needMinX && checkX < needMaxX && checkY > needMinY && checkY < needMaxY;
    }
}
