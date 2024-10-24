package com.ferra13671.BThack.api.Utils.List;

import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Commands.AbstractBlockListCommand;
import com.ferra13671.BThack.impl.Commands.EditListCommand;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.ArrayList;

public class BlockList {

    public final String txtName;

    public final EditListCommand editListCommand;
    public final AbstractBlockListCommand listCommand;

    public final ArrayList<Block> blocks = new ArrayList<>();
    public final ArrayList<String> blockNames = new ArrayList<>();

    public Block block = null;

    public BlockList(String descName, String alias, String txtName) {
        editListCommand = new EditListCommand(descName, alias, this);
        listCommand = new AbstractBlockListCommand(descName, alias + "list") {
            @Override
            public void sendAllList() {
                BlockList.this.sendAllList();
            }
        };
        this.txtName = txtName;
        try {
            FileSystem.registerFolder(descName, "");
            FileSystem.registerFile(txtName, descName, "txt");
        } catch (Exception ignored) {}
    }

    public void saveInFile() throws IOException {
        ConfigUtils.saveInTxt(txtName, editListCommand.descName, writer -> {
            for (String blockName : blockNames) {
                try {
                    writer.write(blockName + System.lineSeparator());
                } catch (IOException ignored) {}
            }
        });
    }

    public void loadFromFile() throws IOException {
        ConfigUtils.loadFromTxt(txtName, editListCommand.descName, line -> {
            Block block = BlockUtils.getBlockFromNameOrID(line);
            if (block != null) {
                blocks.add(block);
                blockNames.add(line);
            }
        });
    }

    public void preAction(String arg) {
        block = BlockUtils.getBlockFromNameOrID(arg);
        if (block == null) {
            ChatUtils.sendMessage(Formatting.RED + LanguageSystem.translate("lang.command.List.invalidName"));
        }
    }

    public void addToList(String arg) {
        if (!blocks.contains(block)) {
            blocks.add(block);
            blockNames.add(arg);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockAdded"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyAdded"));
        }
    }

    public void removeFromList(String arg) {
        if (blocks.contains(block)) {
            blocks.remove(block);
            blockNames.remove(arg);
            try {
                saveInFile();
            } catch (IOException ignored) {}
            ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockRemoved"));
        } else {
            ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyRemoved"));
        }
    }

    public void clearList() {
        blocks.clear();
        blockNames.clear();
        try {
            saveInFile();
        } catch (IOException ignored) {}
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), editListCommand.descName));
    }

    public void postAction() {

    }

    public void sendAllList() {
        for (String blockName : blockNames) {
            ChatUtils.sendMessage(blockName);
        }
    }
}
