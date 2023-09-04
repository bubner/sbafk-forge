package holo.holoafk.listeners;

import holo.holoafk.actions.AlertAction;
import holo.holoafk.actions.CommandAction;
import holo.holoafk.actions.threads.ReconnectAction;
import holo.holoafk.utils.Events;
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

    private final Configuration config;
    private ServerData lastConnected;

    public ConnectionListener(Configuration config) {
        this.config = config;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.lastConnected = Minecraft.getMinecraft().getCurrentServerData();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (!config.get("settings", "active", false).getBoolean()) {
            return;
        }

        if (lastConnected != null) {
            ReconnectAction reconnectAction = new ReconnectAction(lastConnected, 3);
            reconnectAction.start();
            // Wait until reconnect thread is done
            try {
                reconnectAction.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String webhook = config.get("settings", "webhook", "").getString();
        int maxTries = config.get("settings", "maxtries", 2).getInt();
        if (webhook.isEmpty()) {
            CommandAction.runRecovery(maxTries, Events.RecoveryEvent.KICK_RECOVERY);
            return;
        }
        String discordid = config.get("settings", "discordid", "").getString();
        String identifier = config.get("settings", "identifier", "").getString();

        if (Minecraft.getMinecraft().getCurrentServerData() == null) {
            AlertAction.sendAlert("<account disconnect detection>", Events.getKickEventContent(Events.KickEvent.UNSUCCESSFUL_DISCONNECT_RECOVERY), identifier, discordid, webhook, AlertAction.AlertPriority.HIGH);
            return;
        }

        CommandAction.runRecovery(maxTries, Events.RecoveryEvent.KICK_RECOVERY);
    }
}
