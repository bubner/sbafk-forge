package me.bubner.sbafk.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.Sys;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    /**
     * Read the config file.
     */
    public static Configuration getConfig() {
        File configFile = new File(Loader.instance().getConfigDir(), "SbAFK.cfg");
        Configuration config = new Configuration(configFile);
        config.load();
        return config;
    }

    /**
     * Send a formatted message to the player.
     */
    public static void sendMsg(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§4[§csbafk§4]§7 " + message));
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
     * Check if the user is on their Private Island by checking for 'Your Isla' in the scoreboard.
     * Hypixel includes emojis in their scoreboard for formatting purposes, so we can only check for a partial match.
     */
    public static boolean isOnPrivateIsland() {
        try {
            Scoreboard sc = Minecraft.getMinecraft().theWorld.getScoreboard();
            ScoreObjective sidebar = sc.getObjectiveInDisplaySlot(1);
            Collection<Score> scores = sc.getSortedScores(sidebar);
            for (Score line : scores) {
                ScorePlayerTeam team = sc.getPlayersTeam(line.getPlayerName());
                if (ScorePlayerTeam.formatPlayerName(team, line.getPlayerName()).trim().contains("Your Isla")) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }
}
