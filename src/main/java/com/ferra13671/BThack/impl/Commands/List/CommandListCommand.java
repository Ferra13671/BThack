package com.ferra13671.BThack.impl.Commands.List;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.CommandSystem.command.ICommand;
import com.ferra13671.BThack.api.CommandSystem.manager.CommandManager;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class CommandListCommand extends AbstractCommand {

    public CommandListCommand() {
        super("lang.command.CommandList.description", "commandlist", "commandlist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.CommandList.message"));

        for (ICommand command : CommandManager.commands) {
            ChatUtils.sendMessage(Formatting.GRAY + "$" + command.getUsage() + Formatting.WHITE + " - " + Formatting.YELLOW + command.getDescription() + ";");
        }
    }
}
