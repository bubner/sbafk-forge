package holo.holoafk.actions.threads;

import holo.holoafk.utils.Utils;
import net.minecraft.client.Minecraft;

/**
 * Run commands to recover from being kicked to the SkyBlock hub.
 */
public class HubAction extends Thread implements Runnable {

    private final int maxTries;

    public HubAction(int maxTries) {
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
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/is");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Utils.isOnPrivateIsland()) {
                break;
            }
            tries++;
        }
    }
}
