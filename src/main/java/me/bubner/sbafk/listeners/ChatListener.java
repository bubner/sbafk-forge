package me.bubner.sbafk.listeners;

import me.bubner.sbafk.actions.CommandAction;
import me.bubner.sbafk.utils.Events;
import me.bubner.sbafk.utils.FlagTrigger;
import me.bubner.sbafk.utils.ModConfig;
import me.bubner.sbafk.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
                    Utils.sendMsg("Cannot notify! Webhook is not set! Set it with /sbafk wh <webhook link>");
                    return;
                }
            }
        }
    }
}
