package me.bubner.sbafk.actions.threads;

import me.bubner.sbafk.actions.SendAlert;
import me.bubner.sbafk.utils.Events;
import me.bubner.sbafk.utils.FlagTrigger;
import me.bubner.sbafk.utils.ModConfig;
import me.bubner.sbafk.utils.Utils;
import net.minecraft.client.Minecraft;

import static me.bubner.sbafk.actions.CommandAction.runRecovery;

/**
 * Run commands to recover from being completely kicked from SkyBlock.
 */
public class KickAction extends Thread implements Runnable {

    private final ModConfig config;
    private final FlagTrigger trigger;

    public KickAction(ModConfig config, FlagTrigger trigger) {
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
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play sb");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Utils.isInSkyblock()) {
                Utils.sendMsg("SkyBlock connection reestablished.");
                runRecovery(config, trigger, Events.RecoveryEvent.HUB_RECOVERY);
                return;
            }
            tries++;
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!Utils.isInSkyblock()) {
            Utils.sendMsg("Failed to reconnect. Sending alert.");
            trigger.setSuccess(false);
            new SendAlert(config, trigger, Events.AlertPriority.HIGH).start();
            return;
        }
        runRecovery(config, trigger, Events.RecoveryEvent.HUB_RECOVERY);
    }
}
