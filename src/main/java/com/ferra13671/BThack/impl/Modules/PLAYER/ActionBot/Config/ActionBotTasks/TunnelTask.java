package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Build.BuildThread3D;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.InventoryUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


// TODO : Add more checks
public class TunnelTask extends ActionBotTask {


    public TunnelTask(Direction direction, double length) {
        super("Make Tunnel");
        this.mode = "Tunnel";

        this.direction = direction;
        this.length = length;

        this.needX = this.length * this.direction.getX();
        this.needZ = this.length * this.direction.getZ();

        this.taskDescription = Arrays.asList(
                "The more advanced version of MoveTask is equipped with special checks necessary for more safe tunnel digging.",
                "Unlike its ancestor, the direction and final length of the tunnel are used instead of coordinates. "
        );
    }

    private final double length;
    private final Direction direction;

    private final double needX;
    private final double needZ;

    public double maxX;
    public double minX;
    public double maxZ;
    public double minZ;
    protected boolean cancel;

    @Override
    public void play() {
        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);
        alignWithXZ.alignWithXZ();

        do {
            sleepThread(50);
        } while (alignWithXZ.isMoving());


        double tempX = mc.player.getX() + needX;
        double tempZ = mc.player.getZ() + needZ;

        boolean scaffoldActivated;

        scaffoldActivated = Client.getModuleByName("Scaffold").isEnabled();
        Client.getModuleByName("Scaffold").setToggled(true);


        maxX = tempX + 0.15;
        minX = tempX - 0.15;
        maxZ = tempZ + 0.15;
        minZ = tempZ - 0.15;


        while (toFarX() || toFarZ()) {

            mc.player.yaw = AimBotUtils.rotations(new Vec3d(tempX, mc.player.getY(), tempZ))[0];
            mc.options.forwardKey.setPressed(!DestroyManager.isDestroying);

            if (!Client.getModuleByName("Scaffold").isEnabled()) {
                Client.getModuleByName("Scaffold").setToggled(true);
            }

            BlockPos blockPos1;
            BlockPos blockPos2;
            double x;
            double y = mc.player.getY() + 0.5;
            double z;

            if (checkLava()) {
                ChatUtils.sendMessage("Lava Detected!");
                mc.options.forwardKey.setPressed(false);
                emergencyTrap();
                cancel = false;
                disableScaffold(scaffoldActivated);
                return;
            }

            if (!mc.player.horizontalCollision) {
                x = mc.player.getX() + (AimBotUtils.getCordFactorFromDirection(mc.player)[0]);
                z = mc.player.getZ() + (AimBotUtils.getCordFactorFromDirection(mc.player)[1]);
            } else {
                x = mc.player.getX() + (AimBotUtils.getCordFactorFromDirection(mc.player)[0]);
                z = mc.player.getZ() + (AimBotUtils.getCordFactorFromDirection(mc.player)[1]);
            }

            blockPos1 = new ModifyBlockPos(x, y, z);
            blockPos2 = new BlockPos(blockPos1.getX(), blockPos1.getY() + 1, blockPos1.getZ());
            Block block1 = mc.world.getBlockState(blockPos1).getBlock();
            Block block2 = mc.world.getBlockState(blockPos2).getBlock();

            SimpleDestroyThread destroyThread;

            if (!mc.world.isAir(blockPos1)) {
                if (!BuildManager.ignoreBlocks.contains(block1) && !(block1 instanceof FlowerBlock)) {
                    mc.options.forwardKey.setPressed(false);
                    if (!DestroyManager.isDestroying) {
                        destroyThread = new SimpleDestroyThread(blockPos1);
                        destroyThread.start();
                    }
                }
            } else if (!mc.world.isAir(blockPos2)) {
                if (!BuildManager.ignoreBlocks.contains(block2) && !(block2 instanceof FlowerBlock)) {
                    mc.options.forwardKey.setPressed(false);
                    if (!DestroyManager.isDestroying) {
                        destroyThread = new SimpleDestroyThread(blockPos2);
                        destroyThread.start();
                    }
                }
            }

            if (!toFarX() && !toFarZ()) {
                mc.options.forwardKey.setPressed(false);
                cancel = false;
                disableScaffold(scaffoldActivated);
                return;
            }

            if (cancel) {
                cancel = false;
                mc.options.forwardKey.setPressed(false);
                disableScaffold(scaffoldActivated);
                return;
            }
        }
        mc.options.forwardKey.setPressed(false);
        cancel = false;
        disableScaffold(scaffoldActivated);
    }
    private void disableScaffold(boolean scaffoldEnabled) {
        if (!scaffoldEnabled) {
            Client.getModuleByName("Scaffold").setToggled(false);
        }
    }

    private boolean toFarX() {
        return mc.player.getX() > maxX || mc.player.getX() < minX;
    }

    private boolean toFarZ() {
        return mc.player.getZ() > maxZ || mc.player.getZ() < minZ;
    }

    private boolean checkLava() {
        double x = mc.player.getX() + (AimBotUtils.getCordFactorFromDirection(mc.player)[0] * 2);
        double y = mc.player.getY() + 0.5;
        double z = mc.player.getZ() + (AimBotUtils.getCordFactorFromDirection(mc.player)[1] * 2);

        ArrayList<BlockPos> blockPosData = new ArrayList<>(Arrays.asList(
                new ModifyBlockPos(x, y, z),
                new ModifyBlockPos(x - 1, y, z),
                new ModifyBlockPos(x + 1, y, z),
                new ModifyBlockPos(x, y, z + 1),
                new ModifyBlockPos(x, y, z - 1)
        ));

        for (BlockPos pos : blockPosData) {
            if (BuildManager.lavas.contains(mc.world.getBlockState(pos).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new ModifyBlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new ModifyBlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getBlock())) {
                return true;
            } else if (BuildManager.lavas.contains(mc.world.getBlockState(new ModifyBlockPos(pos.getX(), pos.getY() + 2, pos.getZ())).getBlock())) {
                return true;
            }
        }
        return false;
    }

    private void emergencyTrap() {
        ArrayList<Vec3d> trapSchematic = new ArrayList<>(Arrays.asList(
                new Vec3d(1,1,0),
                new Vec3d(-1,1,0),
                new Vec3d(0,1,1),
                new Vec3d(0,1,-1),
                new Vec3d(1,2,0),
                new Vec3d(-1,2,0),
                new Vec3d(0,2,1),
                new Vec3d(0,2,-1),
                new Vec3d(0,3,0)
        ));

        WhereToAlign whereToAlign = new WhereToAlign();
        AlignWithXZ alignWithXZ = new AlignWithXZ(whereToAlign);

        alignWithXZ.alignWithXZ();

        for (int needSlot = 0; needSlot < 36; needSlot++) {
            Item item = mc.player.getInventory().getStack(needSlot).getItem();
            if (item instanceof BlockItem) {
                if (needSlot < 9) {
                    InventoryUtils.swapItem(needSlot);

                } else {
                    InventoryUtils.swapItemOnInventory(mc.player.getInventory().selectedSlot, needSlot);
                    mc.interactionManager.tick();
                }
                break;
            }
        }

        BlockPos startPos = new ModifyBlockPos(mc.player.getX(), mc.player.getY() - 0.1, mc.player.getZ());

        BuildThread3D buildThread3D = new BuildThread3D();
        buildThread3D.set3DSchematic(1, trapSchematic, startPos);
        buildThread3D.start();
    }






    public Direction getDirection() {
        return this.direction;
    }

    public double getLength() {
        return this.length;
    }


    public enum Direction {
        X_PLUS(1,0),
        X_MINUS(-1,0),
        Z_PLUS(0,1),
        Z_MINUS(0,-1);


        private final int x;
        private final int z;

        Direction(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public int getX() {
            return this.x;
        }

        public int getZ() {
            return this.z;
        }
    }

    @Override
    public String getButtonName() {
        return getName() + ":  Direction: " + getDirection().name() + "  Length: " + getLength();
    }

    @Override
    public void save(JsonObject jsonObject) {
        String direction = getDirection().name();

        jsonObject.add("Direction", new JsonPrimitive(direction));
        jsonObject.add("Length", new JsonPrimitive(getLength()));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (jsonObject.get("Direction") != null && jsonObject.get("Length") != null) {
            String directionString = jsonObject.get("Direction").getAsString();
            double length = jsonObject.get("Length").getAsDouble();

            TunnelTask.Direction direction = TunnelTask.Direction.X_PLUS;

            for (TunnelTask.Direction e : TunnelTask.Direction.values()) {
                if (e.name().equals(directionString)) {
                    direction = e;
                    break;
                }
            }


            ActionBotConfig.tasks.add(new TunnelTask(direction, length));
        }
    }
}
