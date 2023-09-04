package holo.holoafk.actions.threads;

import holo.holoafk.utils.Events;
import net.minecraft.client.Minecraft;

import static holo.holoafk.actions.CommandAction.runRecovery;

/**
 * Run commands to recover from being kicked to limbo.
 */
public class LimboAction extends Thread implements Runnable {

    private final int maxTries;

    public LimboAction(int maxTries) {
        this.maxTries = maxTries;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/l");
        // Not bothering with checks if we're out of limbo
        runRecovery(maxTries, Events.RecoveryEvent.KICK_RECOVERY);
    }
}
