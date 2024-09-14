package com.ferra13671.BThack.impl.Commands.List;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Social.Friends.FriendList;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class FriendListCommand extends AbstractCommand {

    public FriendListCommand() {
        super("lang.command.FriendList.description", "friendlist", "friendlist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.FriendList.message"));

        for (String name : FriendList.friends) {
            ChatUtils.sendMessage(name);
        }
    }
}
