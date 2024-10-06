package com.ferra13671.BThack.impl.Commands;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Social.Enemies.EnemyList;
import com.ferra13671.BThack.api.Social.Friends.FriendList;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class RefreshCommand extends AbstractCommand {

    public RefreshCommand() {
        super("lang.command.Refresh.description", "refresh [friends/enemies/clans]", "refresh"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || !args[0].equals("friends") && !args[0].equals("enemies") && !args[0].equals("clans")) {
            invalidArgumentError();
        } else {
            switch (args[0]) {
                case "friends":
                    FriendList.loadFriends();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.friendRefreshed"));
                    break;
                case "enemies":
                    EnemyList.loadEnemies();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.enemyRefreshed"));
                    break;
                case "clans":
                    ClansUtils.reloadClans();
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Refresh.clanRefreshed"));
                    break;
            }
        }
    }
}
