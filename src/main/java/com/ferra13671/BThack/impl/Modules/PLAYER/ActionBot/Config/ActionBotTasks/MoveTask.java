package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BTbot.api.Utils.Motion.Align.AlignWithXZ;
import com.ferra13671.BTbot.api.Utils.Motion.Align.WhereToAlign;
import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Managers.Build.BuildManager;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.api.Utils.Modules.AimBotUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.FlowerBlock;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class MoveTask extends ActionBotTask {
    private final Type type;
    private final double needX;
    private final double needZ;
    private final boolean scaffold;


    public MoveTask(double needX, double needZ, boolean scaffold, Type type) {
        super("Move");
        this.mode = "Move";

        this.needX = needX;
        this.needZ = needZ;
        this.scaffold = scaffold;
        this.type = type;

        this.taskDescription = Arrays.asList(
                "When the task is activated, the player starts walking to the specified coordinates.",
                "Coordinates should be entered by the ratio of the player's coordinates.",
                "I.e., if you need to write 5 at x coordinates, if you want the",
                "the player has moved 5 blocks along the x-coordinate."
        );
    }
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

        boolean scaffoldActivated = true;

        if (scaffold) {
            scaffoldActivated = ModuleList.scaffold.isEnabled();
            ModuleList.scaffold.setToggled(true);
        }


        maxX = tempX + 0.15;
        minX = tempX - 0.15;
        maxZ = tempZ + 0.15;
        minZ = tempZ - 0.15;

        while (toFarX() || toFarZ()) {
            mc.player.yaw = AimBotUtils.rotations(new Vec3d(tempX, mc.player.getY(), tempZ))[0];
            mc.options.forwardKey.setPressed(!DestroyManager.isDestroying || type != Type.Through_Obstacles);

            if (scaffold) {
                if (!ModuleList.scaffold.isEnabled()) {
                    ModuleList.scaffold.setToggled(true);
                }
            }

            if (mc.player.horizontalCollision) {
                switch (type) {
                    case Default:
                        ChatUtils.sendMessage("[ActionBot: MoveTask] " + Formatting.YELLOW + "The player ran into an obstacle. Skipping a task.");
                        mc.options.forwardKey.setPressed(false);
                        disableScaffold(scaffoldActivated);
                        return;
                    case AutoJump:
                        tryJump();
                        break;
                    case Through_Obstacles:
                        mc.options.forwardKey.setPressed(false);
                        if (!DestroyManager.isDestroying) {
                            double x = mc.player.getX() + AimBotUtils.getCordFactorFromDirection(mc.player)[0];
                            double z = mc.player.getZ() + AimBotUtils.getCordFactorFromDirection(mc.player)[1];
                            double y = mc.player.getY() + 0.5;
                            BlockPos blockPos = new ModifyBlockPos(x, y, z);
                            if (BuildManager.ignoreBlocks.contains(mc.world.getBlockState(blockPos).getBlock()) || mc.world.isAir(blockPos) || mc.world.getBlockState(blockPos).getBlock() instanceof FlowerBlock) {
                                blockPos = new ModifyBlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                            }
                            SimpleDestroyThread destroyThread = new SimpleDestroyThread(blockPos);
                            destroyThread.start();
                        }
                        break;
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

            Thread.yield();
        }
        mc.options.forwardKey.setPressed(false);
        cancel = false;
        disableScaffold(scaffoldActivated);
    }
    private void disableScaffold(boolean scaffoldEnabled) {
        if (scaffold) {
            if (!scaffoldEnabled) {
                ModuleList.scaffold.setToggled(false);
            }
        }
    }

    private boolean toFarX() {
        return mc.player.getX() > maxX || mc.player.getX() < minX;
    }

    private boolean toFarZ() {
        return mc.player.getZ() > maxZ || mc.player.getZ() < minZ;
    }

    private void tryJump() {
        mc.options.forwardKey.setPressed(false);
        double oldPosY = mc.player.getY();
        mc.player.jump();
        mc.options.forwardKey.setPressed(true);

        sleepThread(600);

        mc.options.forwardKey.setPressed(false);
        if (mc.player.getY() < oldPosY + 0.4) {
            mc.player.jump();
            mc.options.forwardKey.setPressed(true);

            sleepThread(600);

            mc.options.forwardKey.setPressed(false);
        }
        if (mc.player.getY() < oldPosY + 0.4) {
            mc.player.jump();
            mc.options.forwardKey.setPressed(true);

            sleepThread(600);

            mc.options.forwardKey.setPressed(false);
        }
        if (mc.player.getY() < oldPosY + 0.4) {
            cancel = true;
        }
    }





    public double getNeedX() {
        return this.needX;
    }

    public double getNeedZ() {
        return this.needZ;
    }

    public boolean isScaffold() {
        return this.scaffold;
    }

    public Type getType() {
        return this.type;
    }

    @Override
    public String getButtonName() {
        return getName() + ":  X: " + getNeedX() + "  Z: " + getNeedZ() + "  Scaffold: " + isScaffold() + "  Type: " + getType().name();
    }

    @Override
    public void save(JsonObject jsonObject) {
        String type = getType().name();

        jsonObject.add("NeedX", new JsonPrimitive(getNeedX()));
        jsonObject.add("NeedZ", new JsonPrimitive(getNeedZ()));
        jsonObject.add("Scaffold", new JsonPrimitive(isScaffold()));
        jsonObject.add("Type", new JsonPrimitive(type));
    }

    @Override
    public void load(JsonObject jsonObject) {
        if (jsonObject.get("NeedX") != null && jsonObject.get("NeedZ") != null && jsonObject.get("Scaffold") != null && jsonObject.get("Type") != null) {
            double needX = jsonObject.get("NeedX").getAsDouble();
            double needZ = jsonObject.get("NeedZ").getAsDouble();
            boolean scaffold = jsonObject.get("Scaffold").getAsBoolean();
            String type = jsonObject.get("Type").getAsString();

            MoveTask.Type moveType = MoveTask.Type.Default;

            for (MoveTask.Type e : MoveTask.Type.values()) {
                if (e.name().equals(type)) {
                    moveType = e;
                    break;
                }
            }

            ActionBotConfig.tasks.add(new MoveTask(needX, needZ, scaffold, moveType));
        }
    }

    public enum Type {
        Default,
        AutoJump,
        Through_Obstacles
    }
}
