package dev.lockpickgames.feelybots.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.manager.BotsManager;
import dev.lockpickgames.feelybots.npc.FeelyBot;
import dev.lockpickgames.feelybots.utils.StringUtil;
import net.citizensnpcs.api.npc.NPC;
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
        new CommandAPICommand("feelybot")
                .withAliases("bot")
                .withSubcommand(new CommandAPICommand("create")
                        .withPermission("feelybot.create")
                        .withArguments(new StringArgument("name"))
                        .withOptionalArguments(new StringArgument("nickname"))
                        .executesPlayer(BotCommand::create))
                .withSubcommand(new CommandAPICommand("remove")
                        .withPermission("feelybot.remove")
                        .executesPlayer(BotCommand::remove))
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("feelybot.reload")
                        .executesPlayer(BotCommand::reload))
                .register();
    }

    private static void create(Player player, CommandArguments arguments) {
        Location location = player.getLocation().clone().add(0.5, 0, 0.5);
        String name = (String) arguments.get(0);
        String nickname = (String) arguments.getOrDefault(1, name);
        FeelyBot feelyBot = new FeelyBot(name, nickname);
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
