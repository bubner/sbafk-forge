package holo.holoafk.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class Utils {
    /**
     * Read the config file.
     */
    public static Configuration getConfig() {
        File configFile = new File(Loader.instance().getConfigDir(), "HoloAFK.cfg");
        Configuration config = new Configuration(configFile);
        config.load();
        return config;
    }

    /**
     * Send a formatted message to the player.
     */
    public static void sendMsg(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§4[§choloafk§4]§7 " + message));
    }

    /**
     * Check if the user is in SkyBlock by analysing the scoreboard.
     */
    public static boolean isInSkyblock() {
        try {
            return Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null && Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().contains("SKYBLOCK");
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Check if the user is on their Private Island by checking for Your Isla and 🍭 in the scoreboard.
     * Hypixel includes emojis in their scoreboard island location depending on the island type.
     */
    public static boolean isOnPrivateIsland() {
        try {
            Scoreboard sc = Minecraft.getMinecraft().theWorld.getScoreboard();
            String[] lines = sc.getObjectiveInDisplaySlot(1).getDisplayName().split("\n");
            for (String line : lines) {
                if (line.contains("Your Isla") || line.contains("🍭")) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
}
