package me.bubner.sbafk;

import me.bubner.sbafk.commands.SbAFKSettings;
import me.bubner.sbafk.listeners.ChatListener;
import me.bubner.sbafk.listeners.ConnectionListener;
import me.bubner.sbafk.utils.ModConfig;
import me.bubner.sbafk.utils.Utils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * SbAFK
 */
@Mod(modid = SbAFK.MODID, version = SbAFK.VERSION)
public class SbAFK {
    public static final String MODID = "sbafk";
    public static final String VERSION = "1.0";

    private final Configuration config = Utils.getConfig();

    @Mod.EventHandler
    public void preInit(FMLInitializationEvent event) {
        config.get("settings", "active", true);
        config.get("settings", "webhook", "");
        config.get("settings", "discordid", "");
        config.get("settings", "identifier", "");
        config.get("settings", "maxtries", 2);
        if (config.hasChanged()) {
            config.save();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Lachlan Paul CSS");
        // Register commands and the two primary disconnect listeners
        ModConfig modConfig = new ModConfig(this.config);
        ClientCommandHandler.instance.registerCommand(new SbAFKSettings(config));
        MinecraftForge.EVENT_BUS.register(new ConnectionListener(modConfig));
        MinecraftForge.EVENT_BUS.register(new ChatListener(modConfig));
    }
}
