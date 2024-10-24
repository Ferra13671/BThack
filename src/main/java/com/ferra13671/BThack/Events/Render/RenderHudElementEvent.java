package com.ferra13671.BThack.Events.Render;

import com.ferra13671.MegaEvents.Base.Event;
import net.minecraft.client.gui.hud.InGameHud;

public class RenderHudElementEvent extends Event {
    public final ElementType elementType;
    public final InGameHud inGameHud;

    public RenderHudElementEvent(InGameHud inGameHud ,ElementType elementType) {
        this.inGameHud = inGameHud;
        this.elementType = elementType;
    }

    public enum ElementType {
        //NONE,
        PUMPKIN,
        PORTAL,
        CROSSHAIRS,
        //BOSSHEALTH,
        //BOSSINFO,
        //ARMOR,
        HEALTH,
        //FOOD,
        //AIR,
        HOTBAR,
        EXPERIENCE,
        //TEXT,
        //HEALTHMOUNT,
        JUMPBAR,
        //CHAT,
        //PLAYER_LIST,
        //DEBUG,
        //POTION_ICONS,
        //SUBTITLES,
        SPYGLASS,
        //FPS_GRAPH,
        VIGNETTE,
        //POWDER_SNOW,
        ITEM,
        STATUS_EFFECTS,
        SCOREBOARD_SIDEBAR
    }
}
