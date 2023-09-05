package holo.holoafk.listeners;

import holo.holoafk.actions.SendAlert;
import holo.holoafk.actions.CommandAction;
import holo.holoafk.actions.threads.ReconnectAction;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * Server disconnect detection for kick events.
 */
public class ConnectionListener {

    private final ModConfig config;
    private ServerData lastConnected;

    public ConnectionListener(ModConfig config) {
        this.config = config;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.lastConnected = Minecraft.getMinecraft().getCurrentServerData();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (!config.isActive()) {
            return;
        }

        if (lastConnected != null) {
            ReconnectAction reconnectAction = new ReconnectAction(lastConnected, config);
            reconnectAction.start();
        }
    }
}
