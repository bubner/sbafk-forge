package holo.holoafk;

import holo.holoafk.commands.HoloAFKSettings;
import holo.holoafk.listeners.ChatListener;
import holo.holoafk.listeners.ConnectionListener;
import holo.holoafk.utils.ModConfig;
import holo.holoafk.utils.Utils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * HoloAFK
 */
@Mod(modid = HoloAFK.MODID, version = HoloAFK.VERSION)
public class HoloAFK {
    public static final String MODID = "holoafk";
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
        ClientCommandHandler.instance.registerCommand(new HoloAFKSettings(config));
        MinecraftForge.EVENT_BUS.register(new ConnectionListener(modConfig));
        MinecraftForge.EVENT_BUS.register(new ChatListener(modConfig));
    }
}
