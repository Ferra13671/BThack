package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.RENDER.Search;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;

public class SearchCommand extends AbstractCommand {

    public SearchCommand() {
        super("lang.command.Xray.description", "search [clear] / [[add/remove] [block_name]]", "search"
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

                            if (!Client.blockSearchManager.getSearchBlocks().contains(block)) {
                                Client.blockSearchManager.addBlockToSearch(block);
                                Search.searchBlockNames.add(args[1]);
                                try {
                                    ConfigInit.saveSearchBlocks();
                                } catch (IOException ignored) {
                                }
                                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Xray.blockAdded"));
                            } else {
                                ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Xray.blockAlreadyAdded"));
                            }
                            break;
                        case "remove":

                            if (Client.blockSearchManager.getSearchBlocks().contains(block)) {
                                Client.blockSearchManager.removeBlockToSearch(block);
                                Search.searchBlockNames.remove(args[1]);
                                try {
                                    ConfigInit.saveSearchBlocks();
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
                Client.blockSearchManager.clearSearchBlocks();
                Search.searchBlockNames.clear();
                try {
                    ConfigInit.saveSearchBlocks();
                } catch (IOException ignored) {
                }
                ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Search.listCleared"));
            }
        }
    }
}
