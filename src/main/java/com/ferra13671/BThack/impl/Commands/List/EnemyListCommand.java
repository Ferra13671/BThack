package com.ferra13671.BThack.impl.Commands.List;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Social.Enemies.EnemyList;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class EnemyListCommand extends AbstractCommand {

    public EnemyListCommand() {
        super("lang.command.EnemyList.description", "enemylist", "enemylist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.EnemyList.message"));

        for (String name : EnemyList.enemies) {
            ChatUtils.sendMessage(name);
        }
    }
}
