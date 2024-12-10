package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.api.Events.ClientTickEvent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ItemUtils;
import com.ferra13671.BThack.api.Utils.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class WebAura extends Module {

    public static BooleanSetting allowInventory;
    public static ModeSetting swap;
    public static BooleanSetting ignoreWalls;

    public static BooleanSetting players;
    public static BooleanSetting friends;
    public static BooleanSetting teammates;
    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting targetClan;

    public static BooleanSetting mobs;

    public WebAura() {
        super("WebAura",
                "lang.module.WebAura",
                KeyboardUtils.RELEASE,
                MCategory.COMBAT,
                false
        );

        allowInventory = new BooleanSetting("Allow Inventory", this, true);
        swap = new ModeSetting("Swap Mode", this, Arrays.asList("Client", "Packet"));
        ignoreWalls = new BooleanSetting("Ignore Walls", this, true);

        players = new BooleanSetting("Players", this, true);
        friends = new BooleanSetting("Friends", this, false);
        teammates = new BooleanSetting("Teammates", this, false);
        clanManager = ClansUtils.getClanManagerSetting(this);
        clanMode = ClansUtils.getClanModeSetting(this, clanManager);
        targetClan = ClansUtils.getClanTargetSetting(this, clanManager, clanMode);

        mobs = new BooleanSetting("Mobs", this, false);

        initSettings(
                allowInventory,
                swap,
                ignoreWalls,

                players,
                friends,
                teammates,
                clanManager,
                clanMode,
                targetClan,

                mobs
        );
    }

    @EventSubscriber
    public void onTick(ClientTickEvent e) {
        if (nullCheck()) return;

        int slot = InventoryUtils.findItem(Items.COBWEB, allowInventory.getValue() ? 36 : 9);
        if (slot == -1) return;

        PlayerEntity player = KillAuraUtils.filterPlayers(4,
                friends.getValue(),
                teammates.getValue(),
                clanManager.getValue(),
                clanMode.getValue(),
                targetClan.getValue(),
                (entity) -> entity.onGround &&
                        KillAuraUtils.canBeSeeTarget(ignoreWalls, entity) &&
                        mc.world.isAir(new ModifyBlockPos(entity.getX(), entity.getY(), entity.getZ()))
        );

        Entity entity = KillAuraUtils.filterEntity(
                4,
                (entity1) -> entity1.onGround &&
                        entity1 instanceof LivingEntity &&
                        KillAuraUtils.canBeSeeTarget(ignoreWalls, entity1) &&
                        mc.world.isAir(new ModifyBlockPos(entity1.getX(), entity1.getY(), entity1.getZ()))
        );

        if (players.getValue() && player != null) {
            placeAction(slot, player);
        } else if (mobs.getValue() && entity != null) {
            placeAction(slot, entity);
        }
    }

    public void placeAction(int slot, Entity entity) {
        int oldSlot = mc.player.getInventory().selectedSlot;

        InventoryUtils.swapAction(oldSlot, slot, false, swap.getValue());

        BlockPos pos = new ModifyBlockPos(entity.getX(), entity.getY(), entity.getZ());
        ItemUtils.useItemOnBlock(pos);

        InventoryUtils.swapAction(oldSlot, slot, true, swap.getValue());
    }
}
