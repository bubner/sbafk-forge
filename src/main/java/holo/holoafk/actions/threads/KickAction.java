package holo.holoafk.actions.threads;

import holo.holoafk.utils.Events;
import holo.holoafk.utils.Utils;
import net.minecraft.client.Minecraft;

import static holo.holoafk.actions.CommandAction.runRecovery;

/**
 * Run commands to recover from being completely kicked from SkyBlock.
 */
public class KickAction extends Thread implements Runnable {

    private final int maxTries;

    public KickAction(int maxTries) {
        this.maxTries = maxTries;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int tries = 0;
        while (tries < maxTries) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play sb");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Utils.isInSkyblock()) {
                break;
            }
            tries++;
        }
        runRecovery(maxTries, Events.RecoveryEvent.HUB_RECOVERY);
    }
}
