package holo.holoafk.listeners;

import holo.holoafk.actions.SendAlert;
import holo.holoafk.actions.CommandAction;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
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

    private final ModConfig config;
    public ChatListener(ModConfig config) {
        this.config = config;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onServerChat(ClientChatReceivedEvent event) {
        if (!config.isActive()) {
            return;
        }
        String message = event.message.getUnformattedText().toLowerCase();
        for (String key : Events.FLAGGED_MSGS.keySet()) {
            if (message.contains(key)) {
                FlagTrigger trigger = new FlagTrigger(message, key);
                CommandAction.runRecovery(config, trigger, Events.FLAGGED_MSGS.get(key), false);
                if (config.getWebhook().isEmpty()) {
                    Utils.sendMsg("Cannot notify! Webhook is not set! Set it with /holoafk wh <webhook link>");
                    return;
                }
            }
        }
    }
}
