package me.bubner.sbafk.utils;

import net.minecraftforge.common.config.Configuration;

/**
 * Utility getter class for mod configuration.
 */
public class ModConfig {
    private Configuration config;
    public ModConfig(Configuration config) {
        this.config = config;
    }

    public boolean isActive() {
        return config.get("settings", "active", false).getBoolean();
    }

    public String getWebhook() {
        return config.get("settings", "webhook", "").getString();
    }

    public int getMaxTries() {
        return config.get("settings", "maxtries", 2).getInt();
    }

    public String getDiscordId() {
        return config.get("settings", "discordid", "").getString();
    }

    public String getIdentifier() {
        return config.get("settings", "identifier", "").getString();
    }
}
