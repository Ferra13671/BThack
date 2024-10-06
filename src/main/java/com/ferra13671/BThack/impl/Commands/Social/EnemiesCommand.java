package com.ferra13671.BThack.impl.Commands.Social;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Social.Enemies.EnemiesUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import net.minecraft.util.Formatting;

public class EnemiesCommand extends AbstractCommand {

    public EnemiesCommand() {
        super("lang.command.Enemies.description", "enemies [add/remove] [enemyName]", "enemies"
        );
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 0 && args.length != 1 && (args[0].equals("add") || args[0].equals("remove"))) {
            if (args[0].equals("add")) {
                if (EnemiesUtils.addEnemy(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyAdded"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Enemies.enemyExists"));
                }
            }

            if (args[0].equals("remove")) {
                if (EnemiesUtils.removeEnemy(args[1])) {
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.Enemies.enemyRemoved"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.Enemies.enemyDoesn'tExists"));
                }
            }

        } else {
            invalidArgumentError();
        }
    }
}
