package com.ferra13671.BThack.impl.Commands;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class XrayCommand extends AbstractCommand {

    public XrayCommand() {
        super("lang.command.Xray.description", "xray [clear] / [[add/remove] [block_name]]", "xray"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || !args[0].equals("add") && !args[0].equals("remove") && !args[0].equals("clear")) {
            invalidArgumentError();
        } else {
            if (args[0].equals("add") || args[0].equals("remove")) {
                if (args.length > 1) {
                    Block block = BlockUtils.getBlockFromNameOrID(args[1]);
                    if (block == null) {
                        ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.command.Xray.invalidName"));
                        return;
                    }

                    switch (args[0]) {
                        case "add":

                            if (!Xray.xrayBlocks.contains(block)) {
                                Xray.xrayBlocks.add(block);
                                Xray.xrayBlockNames.add(args[1]);
                                if (Client.getModuleByName("Xray").isEnabled())
                                    mc.worldRenderer.reload();
                                try {
                                    ConfigInit.saveXrayBlocks();
                                } catch (IOException ignored) {
                                }
                                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Xray.blockAdded"));
                            } else {
                                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Xray.blockAlreadyAdded"));
                            }
                            break;
                        case "remove":

                            if (Xray.xrayBlocks.contains(block)) {
                                Xray.xrayBlocks.remove(block);
                                Xray.xrayBlockNames.remove(args[1]);
                                if (Client.getModuleByName("Xray").isEnabled())
                                    mc.worldRenderer.reload();
                                try {
                                    ConfigInit.saveXrayBlocks();
                                } catch (IOException ignored) {
                                }
                                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Xray.blockRemoved"));
                            } else {
                                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Xray.blockAlreadyRemoved"));
                            }
                            break;
                    }
                } else invalidArgumentError();
            } else {
                Xray.xrayBlocks.clear();
                Xray.xrayBlockNames.clear();
                if (Client.getModuleByName("Xray").isEnabled())
                    mc.worldRenderer.reload();
                try {
                    ConfigInit.saveXrayBlocks();
                } catch (IOException ignored) {
                }
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Xray.listCleared"));
            }
        }
    }
}
