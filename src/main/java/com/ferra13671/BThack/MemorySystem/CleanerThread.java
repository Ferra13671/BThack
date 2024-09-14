package com.ferra13671.BThack.api.MemorySystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.MemoryCleaner;
import net.minecraft.util.Formatting;

public class CleanerThread implements Runnable, Mc {

   public CleanerThread() {
   }

   public void run() {
      BThack.log("Memory cleaner thread started!");
      if (MemoryCleaner.showMessages.getValue() && mc.player != null && mc.world != null) {
         ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Starting memory cleaning, please wait...");
      }

      System.gc();

      try {
         Thread.sleep(1000L);
      } catch (InterruptedException ignored) {}

      System.gc();
      if (MemoryCleaner.showMessages.getValue()) {
         ChatUtils.sendMessage(Formatting.LIGHT_PURPLE + "Memory clearing completed successfully!");
      }

      BThack.log("Memory cleaner thread finished!");

      if (Client.getModuleByName("CleanMemory").isEnabled()) {
         Client.getModuleByName("CleanMemory").setToggled(false);
      }
   }
}
