package holo.holoafk.actions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import holo.holoafk.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AlertAction {
    /**
     * Send a message to the configured Discord webhook.
     *
     * @param message    Message that triggered the alert
     * @param content    Content of the alert
     * @param identifier Distinguishing identifier for the webhook
     * @param id         Discord ID to ping, supports @everyone and @here
     * @param webhook    Discord webhook URL
     * @param priority   Priority of the alert
     */
    public static void sendAlert(String message, String content, String identifier, String id, String webhook, AlertPriority priority) {
        long currentTime = System.currentTimeMillis() / 1000L;
        String fullmsg = content + "\n\nTriggered by message:\n" + message + "\n<t:" + currentTime + ":R>";

        String pingmsg = "";
        if (Objects.equals(id, "@everyone") || Objects.equals(id, "@here")) {
            pingmsg = id;
        } else {
            try {
                Long.parseLong(id);
                pingmsg = "<@" + id + ">";
            } catch (NumberFormatException e) {
                // Continue
            }
        }

        JsonObject request = getJsonObject(identifier, priority, pingmsg, fullmsg);
        String requestString = request.toString().replaceAll("\\\\(?!n)", "");

        try {
            URL url = new URL(webhook);
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
            sendAlert(message, content, identifier, id, webhook, priority);
        }
    }

    /**
     * Constructs the JSON object for the webhook request.
     */
    private static JsonObject getJsonObject(String notifier, AlertPriority priority, String pingmsg, String fullmsg) {
        JsonObject body = new JsonObject();
        body.addProperty("username", !Objects.equals(notifier, "") ? "holoafk notifier for " + notifier : "holoafk notifier");
        body.addProperty("avatar_url", "https://cdn.discordapp.com/attachments/792907086555643904/1061517437708816444/holov2simple.png");
        body.addProperty("content", priority == AlertPriority.HIGH ? pingmsg : "");

        JsonArray embedsArray = getJsonEmbed(priority, fullmsg);

        body.add("embeds", embedsArray);
        return body;
    }

    /**
     * Constructs the Discord embed for the webhook request.
     */
    private static JsonArray getJsonEmbed(AlertPriority priority, String fullmsg) {
        JsonObject embed = new JsonObject();
        embed.addProperty("title", priority == AlertPriority.HIGH ? "HIGH PRIORITY MESSAGE" : "Low Priority Message");
        embed.addProperty("description", fullmsg);
        embed.addProperty("color", priority == AlertPriority.HIGH ? 0xed1c24 : 0x00b020);

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "https://github.com/hololb/holoafk-forge");

        JsonArray fields = new JsonArray();

        embed.add("fields", fields);
        embed.add("footer", footer);

        JsonArray embedsArray = new JsonArray();
        embedsArray.add(embed);
        return embedsArray;
    }

    /**
     * Priorities of alert messages, HIGH will be pinged and displayed in red.
     */
    public enum AlertPriority {
        LOW,
        HIGH
    }
}
