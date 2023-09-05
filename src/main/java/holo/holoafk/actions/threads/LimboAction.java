package holo.holoafk.actions.threads;

import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
import net.minecraft.client.Minecraft;

import static holo.holoafk.actions.CommandAction.runRecovery;

/**
 * Run commands to recover from being kicked to limbo.
 */
public class LimboAction extends Thread implements Runnable {

    private final ModConfig config;
    private final FlagTrigger trigger;

    public LimboAction(ModConfig config, FlagTrigger trigger) {
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
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/l");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Not bothering with checks if we're out of limbo
        runRecovery(config, trigger, Events.RecoveryEvent.KICK_RECOVERY);
    }
}
