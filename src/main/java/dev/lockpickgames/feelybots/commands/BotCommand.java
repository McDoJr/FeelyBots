package dev.lockpickgames.feelybots.commands;

import dev.asurasoftware.asuraplugin.commands.AsuraCommand;
import dev.asurasoftware.asuraplugin.commands.AsuraSubcommand;
import dev.asurasoftware.asuraplugin.commands.CommandArguments;
import dev.asurasoftware.asuraplugin.commands.StringArgument;
import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.manager.BotsManager;
import dev.lockpickgames.feelybots.npc.FeelyBot;
import dev.lockpickgames.feelybots.utils.StringUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.UUID;

public class BotCommand {

    private static final BotsManager manager = FeelyBots.getInstance().getBotsManager();

    public static void register() {
        new AsuraCommand("feelybot")
                .withAliases("bot")
                .withSubcommand(new AsuraSubcommand("create")
                        .withPermission("feelybot.create")
                        .withArgument(new StringArgument("name"))
                        .withArgument(new StringArgument("role"))
                        .executesPlayer(BotCommand::create))
//                .withSubcommand(new AsuraSubcommand("remove")
//                        .withPermission("feelybot.remove")
//                        .executesPlayer(BotCommand::remove))
                .withSubcommand(new AsuraSubcommand("reload")
                        .withPermission("feelybot.reload")
                        .executesPlayer(BotCommand::reload))
                .register();
    }

    private static void create(Player player, CommandArguments arguments) {
        Location location = player.getLocation().clone();
        String name = arguments.getAsString(0);
        String role = arguments.getAsString(1);
        FeelyBot feelyBot = new FeelyBot(name, role);
        manager.createBot(feelyBot);
        feelyBot.spawn(location);
    }

    private static void remove(Player player, CommandArguments arguments) {
        getBot(player).ifPresent(feelyBot -> {
            feelyBot.getNpc().destroy();
            manager.removeBot(feelyBot.getUUID());
            player.sendMessage(StringUtil.translate(feelyBot.getName() + " &cBot has been removed!"));
        });
    }

    private static void reload(CommandSender sender, CommandArguments arguments) {
        FeelyBots.getInstance().reloadConfig();
        sender.sendMessage(StringUtil.translate("&aConfigurations has been reloaded!"));
    }

    private static Optional<FeelyBot> getBot(Player player) {
        for(double i = 0.2; i <= 3; i += 0.2) {
            Location location = player.getEyeLocation().clone();
            Vector vector = location.getDirection().multiply(i);
            location.add(vector);
            for(Entity entity : location.getWorld().getNearbyEntities(location, 0.1, 0, 0.1)) {
                UUID uuid = entity.getUniqueId();
                if(manager.getBots().containsKey(uuid)) {
                    return manager.getBot(uuid);
                }
            }
        }
        return Optional.empty();
    }
}
