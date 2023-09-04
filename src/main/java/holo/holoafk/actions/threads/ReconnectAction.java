package holo.holoafk.actions.threads;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Asynchronously reconnects to the last connected server until it connects or runs out of tries.
 */
public class ReconnectAction extends Thread implements Runnable {

    private final ServerData lastConnected;
    private int runs;

    public ReconnectAction(ServerData lastConnected, int runs) {
        this.lastConnected = lastConnected;
        this.runs = runs;
    }

    @Override
    public void run() {
        Minecraft.getMinecraft().addScheduledTask(() -> FMLClientHandler.instance().connectToServer(Minecraft.getMinecraft().currentScreen, lastConnected));
        while (Minecraft.getMinecraft().getCurrentServerData() == null && runs > 0) {
            Minecraft.getMinecraft().addScheduledTask(() -> FMLClientHandler.instance().connectToServer(Minecraft.getMinecraft().currentScreen, lastConnected));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Minecraft.getMinecraft().getCurrentServerData() == null) {
                this.runs--;
            }
        }
    }
}
