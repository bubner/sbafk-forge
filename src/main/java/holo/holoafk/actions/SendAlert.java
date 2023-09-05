package holo.holoafk.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import holo.holoafk.utils.Events;
import holo.holoafk.utils.FlagTrigger;
import holo.holoafk.utils.ModConfig;
import holo.holoafk.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SendAlert extends Thread implements Runnable {

    private final FlagTrigger trigger;
    private final ModConfig config;
    private final Events.AlertPriority priority;
    private final int runs;

    /**
     * @param config     Mod configuration
     * @param trigger    Flag trigger
     * @param priority   Priority of the alert
     */
    public SendAlert(ModConfig config, FlagTrigger trigger, Events.AlertPriority priority) {
        this.trigger = trigger;
        this.config = config;
        this.runs = config.getMaxTries();
        this.priority = priority;
    }

    public SendAlert(ModConfig config, FlagTrigger trigger, Events.AlertPriority priority, int runs) {
        this.trigger = trigger;
        this.config = config;
        this.runs = runs;
        this.priority = priority;
    }

    /**
     * Send a message to the configured Discord webhook.
     */
    @Override
    public void run() {
        long currentTime = System.currentTimeMillis() / 1000L;
        String fullmsg = trigger.getContent() + "\n\nTriggered by message:\n" + trigger.getMessage() + "\n<t:" + currentTime + ":R>";

        String pingmsg = "";
        if (Objects.equals(config.getDiscordId(), "@everyone") || Objects.equals(config.getDiscordId(), "@here")) {
            pingmsg = config.getDiscordId();
        } else {
            try {
                Long.parseLong(config.getDiscordId());
                pingmsg = "<@" + config.getDiscordId() + ">";
            } catch (NumberFormatException e) {
                // Continue
            }
        }

        JsonObject request = getJsonObject(config.getIdentifier(), priority, pingmsg, fullmsg);
        String requestString = request.toString().replaceAll("\\\\(?!n)", "");

        try {
            URL url = new URL(config.getWebhook());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            con.setDoOutput(true);
            byte[] input = requestString.getBytes(StandardCharsets.UTF_8);
            con.getOutputStream().write(input, 0, input.length);

            con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.sendMsg("Error sending message to your webhook! Retrying...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            if (runs > 0) {
                new SendAlert(config, trigger, priority, runs - 1).start();
            } else {
                Utils.sendMsg("Failed to send message to your webhook!");
            }
        }
    }

    /**
     * Constructs the JSON object for the webhook request.
     */
    private static JsonObject getJsonObject(String notifier, Events.AlertPriority priority, String pingmsg, String fullmsg) {
        JsonObject body = new JsonObject();
        body.addProperty("username", !Objects.equals(notifier, "") ? "holoafk notifier for " + notifier : "holoafk notifier");
        body.addProperty("avatar_url", "https://cdn.discordapp.com/attachments/792907086555643904/1061517437708816444/holov2simple.png");
        body.addProperty("content", priority == Events.AlertPriority.HIGH ? pingmsg : "");

        JsonArray embedsArray = getJsonEmbed(priority, fullmsg);

        body.add("embeds", embedsArray);
        return body;
    }

    /**
     * Constructs the Discord embed for the webhook request.
     */
    private static JsonArray getJsonEmbed(Events.AlertPriority priority, String fullmsg) {
        JsonObject embed = new JsonObject();
        embed.addProperty("title", priority == Events.AlertPriority.HIGH ? "HIGH PRIORITY MESSAGE" : "Low Priority Message");
        embed.addProperty("description", fullmsg);
        embed.addProperty("color", priority == Events.AlertPriority.HIGH ? 0xed1c24 : 0x00b020);

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "https://github.com/hololb/holoafk-forge");

        JsonArray fields = new JsonArray();

        embed.add("fields", fields);
        embed.add("footer", footer);

        JsonArray embedsArray = new JsonArray();
        embedsArray.add(embed);
        return embedsArray;
    }
}
