package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public abstract class AbstractBlockListCommand extends AbstractCommand {

    private final String listName;

    public AbstractBlockListCommand(String listName ,String alias) {
        super("", alias, alias);

        this.listName = listName;
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.BlockList.message"), listName));

        sendAllList();
    }

    @Override
    public String getDescription() {
        return String.format(LanguageSystem.translate("lang.command.BlockList.description"), listName);
    }

    public abstract void sendAllList();
}
