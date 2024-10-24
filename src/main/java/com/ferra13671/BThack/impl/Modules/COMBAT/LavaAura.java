package com.ferra13671.BThack.impl.Modules.COMBAT;

import com.ferra13671.BThack.Events.TickEvent;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.ModeSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.*;
import com.ferra13671.BThack.api.Utils.Grim.GrimUtils;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.api.Utils.Modules.KillAuraUtils;
import com.ferra13671.MegaEvents.Base.EventSubscriber;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
//Not working
public class LavaAura extends Module {

    public static BooleanSetting allowInventory;
    public static ModeSetting swap;

    public static BooleanSetting pickUpAfter;
    public static NumberSetting pickUpDelay;

    public static BooleanSetting players;
    public static BooleanSetting friends;
    public static BooleanSetting teammates;
    public static BooleanSetting clanManager;
    public static ModeSetting clanMode;
    public static ModeSetting targetClan;

    public static BooleanSetting mobs;

    public LavaAura() {
        super("LavaAura",
                "",
                KeyboardUtils.RELEASE,
                Category.COMBAT,
                false
        );

        allowInventory = new BooleanSetting("Allow Inventory", this, true);
        swap = new ModeSetting("Swap Mode", this, Arrays.asList("Client", "Packet"));

        pickUpAfter = new BooleanSetting("Pick Up After", this, true);
        pickUpDelay = new NumberSetting("PickUpDelay", this, 3, 1, 7, false, pickUpAfter::getValue);

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

                pickUpAfter,
                pickUpDelay,

                players,
                friends,
                teammates,
                clanManager,
                clanMode,
                targetClan,

                mobs
        );
    }

    public List<BlockPos> lavas = new ArrayList<>();
    Ticker ticker = new Ticker();

    @Override
    public void onEnable() {
        super.onEnable();
        ticker.reset();
    }

    @EventSubscriber
    public void onTick(TickEvent.ClientTickEvent e) {
        if (nullCheck()) return;

        int slot = InventoryUtils.findItem(Items.LAVA_BUCKET, allowInventory.getValue() ? 36 : 9);
        if (slot == -1) return;

        PlayerEntity player = KillAuraUtils.filterPlayers(
                4,
                friends.getValue(),
                teammates.getValue(),
                clanManager.getValue(),
                clanMode.getValue(),
                targetClan.getValue(),
                (entity) -> entity.onGround &&
                        !BuildManager.lavas.contains(mc.world.getBlockState(new ModifyBlockPos(entity.getX(), entity.getY(), entity.getZ())).getBlock()) &&
                        BlockUtils.hasLineOfSight((new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).add(0, mc.player.getStandingEyeHeight(), 0)), new Vec3d(entity.getX(), entity.getY(), entity.getZ()))
                );
        Entity entity = KillAuraUtils.filterEntity(
                4,
                (entity1) -> entity1.onGround &&
                        entity1 instanceof LivingEntity &&
                        !BuildManager.lavas.contains(mc.world.getBlockState(new ModifyBlockPos(entity1.getX(), entity1.getY(), entity1.getZ())).getBlock()) &&
                        BlockUtils.hasLineOfSight((new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ()).add(0, mc.player.getStandingEyeHeight(), 0)), new Vec3d(entity1.getX(), entity1.getY(), entity1.getZ()))
                );

        preAction();

        if (players.getValue() && player != null) {
            action(slot, player);
        } else if (mobs.getValue() && entity != null) {
            action(slot, entity);
        }
    }

    public void preAction() {
        if (!pickUpAfter.getValue() && ticker.passed(pickUpDelay.getValue() * 1000)) {
            lavas.removeIf(pos -> MathUtils.getDistance(mc.player.getPos(), new Vec3d(pos.getX(), pos.getY(), pos.getZ())) > 4);

            for (BlockPos pos : lavas) {
                if (mc.world.getBlockState(pos).getBlock() != Blocks.LAVA) return;
                int slot = InventoryUtils.findItem(Items.BUCKET, allowInventory.getValue() ? 36 : 9);
                if (slot == -1) return;
                int oldSlot = mc.player.getInventory().selectedSlot;

                swapAction(oldSlot, slot, false);
                Vec3d vec3d = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                vec3d.y = pos.getY() - 0.4;
                float[] rots = AimBotUtils.rotations(vec3d);
                GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
                ItemsUtils.useItem(Hand.MAIN_HAND, false);
                GrimUtils.sendPostActionGrimPackets();
                //ItemsUtils.useItemOnBlock(BuildManager.getHitResult(pos, false, Direction.UP));

                //swapAction(oldSlot, slot, true);
            }
        }
    }

    public void action(int slot, Entity entity) {
        int oldSlot = mc.player.getInventory().selectedSlot;

        swapAction(oldSlot, slot, false);

        BlockPos pos = new ModifyBlockPos(entity.getX(), entity.getY(), entity.getZ());
        Vec3d vec3d = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        vec3d.y = pos.getY() - 0.4;
        float[] rots = AimBotUtils.rotations(vec3d);
        GrimUtils.sendPreActionGrimPackets(rots[0], rots[1]);
        ItemsUtils.useItem(Hand.MAIN_HAND, false);
        GrimUtils.sendPostActionGrimPackets();
        //ItemsUtils.useItemOnBlock(BuildManager.getHitResult(pos.add(0, -1, 0), false, Direction.UP));
        lavas.add(pos);

        //swapAction(oldSlot, slot, true);
    }

    public void swapAction(int oldSlot, int slot, boolean post) {
        if (!post && oldSlot == slot) return;

        switch (swap.getValue()) {
            case "Client" -> {
                if (slot < 9) {
                    InventoryUtils.swapItem((post ? oldSlot : slot));
                } else {
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
                }
            }
            case "Packet" -> {
                if (slot < 9)
                    InventoryUtils.packetSwapItem((post ? oldSlot : slot));
                else
                    InventoryUtils.swapItemOnInventory(oldSlot, slot);
            }
        }
    }
}
