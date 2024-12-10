package com.ferra13671.BThack.api.Gui.ActionBot;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.Utils.ColorUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;

public class ActionBotConfigGui extends BThackScreen {
    public static double widthFactor = 0;
    public static double heightFactor = 0;

    public static final int menuColor = new Color(85, 85, 85).hashCode();


    public static final double offsetFactor = 1.5;

    public static Window scaledResolution;


    public static final ArrayList<TaskButton> taskButtons = new ArrayList<>();


    protected static short page = 0;

    protected static TaskButton selectedTask = null;

    public ActionBotConfigGui() {
        super(Text.of("ActionBot Config"));
    }

    @Override
    public void init() {
        buttons.clear();
        selectedTask = null;

        scaledResolution = mc.getWindow();

        widthFactor = scaledResolution.getScaledWidth() / 37D;
        heightFactor = scaledResolution.getScaledHeight() / 30D;

        buttons.add(Button.of(1, (int) (scaledResolution.getScaledWidth() - (widthFactor * 4)), (int) (scaledResolution.getScaledHeight() - (heightFactor + heightFactor)), (int) widthFactor * 3, (int) heightFactor, "Quit"));
        buttons.add(Button.of(3, (int) (scaledResolution.getScaledWidth() - (widthFactor * 4)), (int) (scaledResolution.getScaledHeight() - (heightFactor * 8)), (int) widthFactor * 3, (int) heightFactor, "Move"));
        buttons.add(Button.of(4, (int) (scaledResolution.getScaledWidth() - (widthFactor * 4)), (int) (scaledResolution.getScaledHeight() - (heightFactor * 11)), (int) widthFactor * 3, (int) heightFactor, "Edit"));
        buttons.add(Button.of(5, (int) (scaledResolution.getScaledWidth() - (widthFactor * 4)), (int) (scaledResolution.getScaledHeight() - (heightFactor * 14)), (int) widthFactor * 3, (int) heightFactor, "Delete"));
        buttons.add(Button.of(6, (int) (scaledResolution.getScaledWidth() - (widthFactor * 4)), (int) (scaledResolution.getScaledHeight() - (heightFactor * 17)), (int) widthFactor * 3, (int) heightFactor, "Add"));

        //Buttons for scrolling menu
        buttons.add(Button.of(-1, (int) (widthFactor * 1.75), (int) (heightFactor * 7.75), (int) (widthFactor * 0.75), (int) (heightFactor * 0.75), "Up"));
        buttons.add(Button.of(-2, (int) (widthFactor + widthFactor), (int) (scaledResolution.getScaledHeight() - (heightFactor * 4.75)), (int) widthFactor, (int) (heightFactor * 0.75), "Down"));
        //////

        updateTaskList();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawBackGround();

        //Menu
        BThackRender.drawOutlineRect((int) widthFactor - 1, (int) (heightFactor * 7) - 1, (int) (widthFactor * 27) + 1, (int) (scaledResolution.getScaledHeight() - (heightFactor * 4)) + 1, 1, Color.white.hashCode());
        BThackRender.drawRect((int) widthFactor, (int) (heightFactor * 7), (int) (widthFactor * 27), (int) (scaledResolution.getScaledHeight() - (heightFactor * 4)), menuColor);

        int offset = 0;

        //Text with current page
        BThackRender.drawString("Page: " + (page + 1), (int) widthFactor * 3, (int) (heightFactor * 7.2), Color.white.hashCode());


        //Draws all tasks that are currently visible
        for (int i = 0; i < 12; i++) {
            if (i + (page * 12) >= ActionBotConfig.tasks.size()) {
                break;
            }
            ActionBotTask task = ActionBotConfig.tasks.get(i + (page * 12));
            if (i < 11 && !task.getName().equals("End"))
                BThackRender.drawRect((int) ((widthFactor * 9) - 1), (int) (heightFactor * 8  + ((heightFactor * offsetFactor) * offset)), (int) ((widthFactor * 9) + 1), (int) (((heightFactor * 8) + ((heightFactor * offsetFactor) * offset)) + ((heightFactor * offsetFactor))), Color.white.hashCode());


            TaskButton taskButton = taskButtons.get(i + (page * 12));
            taskButton.setOffset(offset);
            if (!task.isStartOrEndTask())
                taskButton.updateButton(mouseX, mouseY);
            taskButton.renderButton();
            offset++;
        }
        //////


        //Description
        BThackRender.drawOutlineRect((int) widthFactor - 1, (int) (scaledResolution.getScaledHeight() - (heightFactor * 3)) - 1, (int) (scaledResolution.getScaledWidth() - (widthFactor * 10)) + 1, (int) (scaledResolution.getScaledHeight() - heightFactor) + 1, 1, ColorUtils.rainbow(100));
        BThackRender.drawRect((int) widthFactor, (int) (scaledResolution.getScaledHeight() - (heightFactor * 3)), (int) (scaledResolution.getScaledWidth() - (widthFactor * 10)), (int) (scaledResolution.getScaledHeight() - heightFactor), Color.black.hashCode());

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (activeButton.getId() == 1) {
            mc.setScreen(null);
        }
        if (activeButton.getId() == 3) {
            if (selectedTask != null) {
                mc.setScreen(new MoveTaskGui(selectedTask));
            }
        }
        if (activeButton.getId() == 4) {
            if (ActionBotConfig.tasks.size() > 2) {
                if (selectedTask != null) {
                    for (ActionBotTaskData ac : ActionBotConfig.getFullActionBotTasks()) {
                        if (selectedTask != null) {
                            if (ac.getTask().mode.equals(selectedTask.task.mode)) {
                                mc.setScreen(ac.getTaskScreen(selectedTask, true));
                            }
                        }
                    }
                }
            }
        }
        if (activeButton.getId() == 5) {
            if (ActionBotConfig.tasks.size() > 2) {
                if (selectedTask == null) {
                    ActionBotConfig.removeActionTaskFromList();
                    mc.setScreen(new ActionBotConfigGui());
                } else {
                    removeTaskFromList(
                            selectedTask.getId()
                    );
                }
            }
        }
        if (activeButton.getId() == 6) {
            if (selectedTask != null)
                AddingTaskGui.substituteTask = selectedTask;
            else
                AddingTaskGui.substituteTask = null;
            mc.setScreen(new AddingTaskGui());
        }





        if (activeButton.getId() == -2) {
            page++;
        }
        if (activeButton.getId() == -1) {
            if (page > 0)
                page--;
        }







        int offset = 0;

        selectedTask = null;

        for (int i = 0; i < 12; i++) {
            if (i + (page * 12) >= ActionBotConfig.tasks.size()) {
                break;
            }
            ActionBotTask task = ActionBotConfig.tasks.get(i + (page * 12));

            TaskButton taskButton = taskButtons.get(i + (page * 12));
            taskButton.setOffset(offset);

            if (!task.isStartOrEndTask()) {
                taskButton.mouseClicked((int) mouseX, (int) mouseY, mouseButton);

                if (taskButton.isSelected()) {
                    selectedTask = taskButton;
                }
            }


            offset++;
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }



    public static void updateTaskList() {
        taskButtons.clear();

        for (int i = 0; i < ActionBotConfig.tasks.size(); i++) {
            ActionBotTask task = ActionBotConfig.tasks.get(i);

            if (task.isStartOrEndTask()) {
                taskButtons.add(new TaskButton(i, task.getButtonName(), 0, task));
            } else {
                taskButtons.add(new TaskButton(i, i + ". " + task.getButtonName(), 0, task));
            }
        }
    }

    public static void removeTaskFromList(int index) {
        MinecraftClient mc = MinecraftClient.getInstance();

        ActionBotConfig.tasks.remove(index);

        mc.setScreen(new ActionBotConfigGui());
    }
}
