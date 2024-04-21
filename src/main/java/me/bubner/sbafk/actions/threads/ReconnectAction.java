package me.bubner.sbafk.actions.threads;

import me.bubner.sbafk.actions.CommandAction;
import me.bubner.sbafk.actions.SendAlert;
import me.bubner.sbafk.utils.Events;
import me.bubner.sbafk.utils.FlagTrigger;
import me.bubner.sbafk.utils.ModConfig;
import me.bubner.sbafk.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Asynchronously reconnects to the last connected server until it connects or runs out of tries.
 */
public class ReconnectAction extends Thread implements Runnable {

    private final ServerData lastConnected;
    private final ModConfig config;

    public ReconnectAction(ServerData lastConnected, ModConfig config) {
        this.lastConnected = lastConnected;
        this.config = config;
    }

    @Override
    public void run() {
        Minecraft.getMinecraft().addScheduledTask(() -> FMLClientHandler.instance().connectToServer(Minecraft.getMinecraft().currentScreen, lastConnected));
        int runs = config.getMaxTries();
        while (Minecraft.getMinecraft().getCurrentServerData() == null && runs > 0) {
            Minecraft.getMinecraft().addScheduledTask(() -> FMLClientHandler.instance().connectToServer(Minecraft.getMinecraft().currentScreen, lastConnected));
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Minecraft.getMinecraft().getCurrentServerData() == null) {
                runs--;
            }
        }

        FlagTrigger trigger = new FlagTrigger("<account disconnect detection>", null);
        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            trigger.setSuccess(false);
            new SendAlert(config, trigger, Events.AlertPriority.HIGH).start();
            return;
        }

        Utils.sendMsg("Connection reestablished. Running recovery commands.");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CommandAction.runRecovery(config, trigger, Events.RecoveryEvent.DISCONNECT_RECOVERY);
    }
}
