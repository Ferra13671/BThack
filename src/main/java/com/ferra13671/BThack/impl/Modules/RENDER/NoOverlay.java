package com.ferra13671.BThack.impl.Modules.RENDER;

import com.ferra13671.BThack.Events.Render.RenderHudElementEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;

public class NoOverlay extends Module {

    public static BooleanSetting hurtCam;
    public static BooleanSetting pumpkin;
    public static BooleanSetting portal;
    public static BooleanSetting crosshair;
    public static BooleanSetting hotbar;
    public static BooleanSetting experiense;
    public static BooleanSetting jumpBar;
    public static BooleanSetting vignette;
    public static BooleanSetting effects;
    public static BooleanSetting scoreBoard;

    public NoOverlay() {
        super("NoOverlay",
                "lang.module.NoOverlay",
                KeyboardUtils.RELEASE,
                Category.RENDER,
                false
        );

        hurtCam = new BooleanSetting("Hurt Camera", this, true);
        pumpkin = new BooleanSetting("Pumpkin", this, true);
        portal = new BooleanSetting("Portal", this, true);
        crosshair = new BooleanSetting("Crosshair", this, false);
        hotbar = new BooleanSetting("Hotbar", this, false);
        experiense = new BooleanSetting("Experience", this, false);
        jumpBar = new BooleanSetting("JumpBar", this, false);
        vignette = new BooleanSetting("Vignette", this, true);
        effects = new BooleanSetting("Effects", this, true);
        scoreBoard = new BooleanSetting("ScoreBoard", this, false);

        initSettings(
                hurtCam,
                pumpkin,
                portal,
                crosshair,
                hotbar,
                experiense,
                jumpBar,
                vignette,
                effects,
                scoreBoard
        );
    }

    @EventSubscriber
    @SuppressWarnings("unused")
    public void onOverlayElements(RenderHudElementEvent e) {
        cancelEvent(pumpkin, RenderHudElementEvent.ElementType.PUMPKIN, e);
        cancelEvent(portal, RenderHudElementEvent.ElementType.PORTAL, e);
        cancelEvent(crosshair, RenderHudElementEvent.ElementType.CROSSHAIRS, e);
        cancelEvent(hotbar, RenderHudElementEvent.ElementType.HOTBAR, e);
        cancelEvent(experiense, RenderHudElementEvent.ElementType.EXPERIENCE, e);
        cancelEvent(jumpBar, RenderHudElementEvent.ElementType.JUMPBAR, e);
        cancelEvent(vignette, RenderHudElementEvent.ElementType.VIGNETTE, e);
        cancelEvent(effects, RenderHudElementEvent.ElementType.STATUS_EFFECTS, e);
        cancelEvent(scoreBoard, RenderHudElementEvent.ElementType.SCOREBOARD_SIDEBAR, e);
    }

    private void cancelEvent(BooleanSetting setting, RenderHudElementEvent.ElementType elementType, RenderHudElementEvent e) {
        if (setting.getValue() && e.elementType == elementType)
            e.setCancelled(true);
    }
}
