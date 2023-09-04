package holo.holoafk.listeners;

import holo.holoafk.actions.AlertAction;
import holo.holoafk.actions.CommandAction;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

/**
 * Chat detection for kick events.
 */
public class ChatListener {

    private final Configuration config;

    public ChatListener(Configuration config) {
        this.config = config;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerChat(ClientChatReceivedEvent event) {
        if (!config.get("settings", "active", false).getBoolean()) {
            return;
        }
        if (config.get("settings", "webhook", "").getString().isEmpty()) {
            Utils.sendMsg("Cannot notify! Webhook is not set! Set it with /holoafk wh <stripped webhook link>");
        }

        String message = event.message.getUnformattedText().toLowerCase();
        int maxTries = config.get("settings", "maxtries", 2).getInt();
        for (String key : Events.FLAGGED_MSGS.keySet()) {
            if (message.contains(key)) {
                CommandAction.runRecovery(maxTries, Events.FLAGGED_MSGS.get(key));
                String webhook = config.get("settings", "webhook", "").getString();
                String discordid = config.get("settings", "discordid", "").getString();
                String identifier = config.get("settings", "identifier", "").getString();
                if (Utils.isInSkyblock() && Utils.isOnPrivateIsland()) {
                    AlertAction.sendAlert(message, Events.getKickEventContent(Objects.requireNonNull(Events.getKickEvent(true, Events.FLAGGED_MSGS.get(key)))), identifier, discordid, webhook, AlertAction.AlertPriority.LOW);
                } else {
                    AlertAction.sendAlert(message, Events.getKickEventContent(Objects.requireNonNull(Events.getKickEvent(false, Events.FLAGGED_MSGS.get(key)))), identifier, discordid, webhook, AlertAction.AlertPriority.HIGH);
                }
            }
        }
    }
}
