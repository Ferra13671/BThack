package com.ferra13671.BThack.impl.Commands.List;

import com.ferra13671.BThack.api.CommandSystem.command.AbstractCommand;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.util.Formatting;

public class XrayListCommand extends AbstractCommand {

    public XrayListCommand() {
        super("lang.command.XrayList.description", "xraylist", "xraylist"
        );
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.XrayList.message"));

        for (String blockName : Xray.xrayBlockNames) {
            ChatUtils.sendMessage(blockName);
        }
    }
}
