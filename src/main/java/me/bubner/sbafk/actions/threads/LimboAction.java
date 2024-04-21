package me.bubner.sbafk.actions.threads;

import me.bubner.sbafk.utils.Events;
import me.bubner.sbafk.utils.FlagTrigger;
import me.bubner.sbafk.utils.ModConfig;
import net.minecraft.client.Minecraft;

import static me.bubner.sbafk.actions.CommandAction.runRecovery;

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
