package holo.holoafk.commands;

import holo.holoafk.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.config.Configuration;

import java.util.Objects;

/**
 * Commands for HoloAFK
 * /holoafk <active | wh | discordid | identifier | maxtries> <value | clear>
 */
public class HoloAFKSettings extends CommandBase {

    private static final String USE_COMMAND = "/holoafk <active | wh | discordid | identifier | maxtries> <value | clear>\n\n" +
            "<active>: whether to activate the mod (true/false)\n" +
            "<wh>: discord webhook for notifications, paste all the text after `https://discord.com/api/webhooks/` in the URL\n" +
            "<discordid>: id of person to ping, optionally supports @everyone and @here\n" +
            "<identifier>: configure name: holoafk notifier for <identifier>\n" +
            "<maxtries>: max recovery attempts before sending a fail message";
    private final Configuration config;

    public HoloAFKSettings(Configuration config) {
        this.config = config;
    }

    @Override
    public String getCommandName() {
        return "holoafk";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return USE_COMMAND;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException(USE_COMMAND);
        }
        if (Objects.equals(args[0], "debug")) {
            Utils.sendMsg("isOnPrivateIsland: " + Utils.isOnPrivateIsland());
            Utils.sendMsg("isInSkyblock: " + Utils.isInSkyblock());
            return;
        }
        if (args.length == 2 && Objects.equals(args[1], "clear")) {
            switch (args[0]) {
                case "active":
                    config.get("settings", "active", true).set(true);
                    break;
                case "wh":
                    config.get("settings", "webhook", "").set("");
                    break;
                case "discordid":
                case "identifier":
                    config.get("settings", args[0], "").set("");
                    break;
                case "maxtries":
                    config.get("settings", "maxtries", 2).set(2);
                    break;
                default:
                    throw new CommandException(USE_COMMAND);
            }
            Utils.sendMsg("Reset " + args[0] + "!");
        } else if (args.length == 2) {
            switch (args[0]) {
                case "active":
                    config.get("settings", "active", true).set(Boolean.parseBoolean(args[1]));
                    break;
                case "wh":
                    // Crappy hack as Minecraft chat has a character limit, would implement a GUI if I needed one
                    config.get("settings", "webhook", "").set(args[1].startsWith("https://discord.com/api/webhooks/") ? "" : "https://discord.com/api/webhooks/" + args[1]);
                    break;
                case "discordid":
                case "identifier":
                    config.get("settings", args[0], "").set(args[1]);
                    break;
                case "maxtries":
                    config.get("settings", "maxtries", 2).set(Integer.parseInt(args[1]));
                    break;
                default:
                    throw new CommandException(USE_COMMAND);
            }
            Utils.sendMsg("Set " + args[0] + " to " + args[1] + "!");
        } else {
            switch (args[0]) {
                case "active":
                    Utils.sendMsg("active: " + config.get("settings", "active", true).getBoolean());
                    break;
                case "wh":
                    Utils.sendMsg("webhook: " + config.get("settings", "webhook", "").getString());
                    break;
                case "discordid":
                case "identifier":
                    Utils.sendMsg(args[0] + ": " + config.get("settings", args[0], "").getString());
                    break;
                case "maxtries":
                    Utils.sendMsg("maxtries: " + config.get("settings", "maxtries", 2).getInt(2));
                    break;
                default:
                    throw new CommandException(USE_COMMAND);
            }
        }
        config.save();
    }
}
