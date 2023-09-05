package holo.holoafk.actions.threads;

import holo.holoafk.actions.CommandAction;
import holo.holoafk.actions.SendAlert;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
import holo.holoafk.utils.Utils;
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
