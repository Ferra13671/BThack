package com.ferra13671.BThack.impl.Commands.List;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.RENDER.Search;
import net.minecraft.util.Formatting;

public class SearchListCommand extends AbstractCommand {

    public SearchListCommand() {
        super("lang.command.SearchList.description", "searchlist", "searchlist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.SearchList.message"));

        for (String blockName : Search.searchBlockNames) {
            ChatUtils.sendMessage(blockName);
        }
    }
}
