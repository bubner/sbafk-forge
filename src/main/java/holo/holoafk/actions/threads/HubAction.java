package holo.holoafk.actions.threads;

import holo.holoafk.actions.SendAlert;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
import holo.holoafk.utils.Utils;
import net.minecraft.client.Minecraft;

/**
 * Run commands to recover from being kicked to the SkyBlock hub.
 */
public class HubAction extends Thread implements Runnable {

    private final ModConfig config;
    private final FlagTrigger trigger;

    public HubAction(ModConfig config, FlagTrigger trigger) {
        this.config = config;
        this.trigger = trigger;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int tries = 0;
        while (tries < config.getMaxTries()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/is");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Utils.isOnPrivateIsland()) {
                trigger.setSuccess(true);
                new SendAlert(config, trigger, Events.AlertPriority.LOW).start();
                break;
            }
            tries++;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!Utils.isOnPrivateIsland()) {
            Utils.sendMsg("Recovery has failed. Sending alert.");
            trigger.setSuccess(false);
            new SendAlert(config, trigger, Events.AlertPriority.HIGH).start();
        } else {
            Utils.sendMsg("Recovery completed. Sending notice.");
            trigger.setSuccess(true);
            new SendAlert(config, trigger, Events.AlertPriority.LOW).start();
        }
    }
}
