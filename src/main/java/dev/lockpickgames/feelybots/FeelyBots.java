package dev.lockpickgames.feelybots;

import dev.lockpickgames.feelybots.commands.BotCommand;
import dev.lockpickgames.feelybots.listener.NPCListener;
import dev.lockpickgames.feelybots.manager.BotsManager;
import dev.lockpickgames.feelybots.manager.QuestManager;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeelyBots extends JavaPlugin {

    @Getter
    private static FeelyBots instance;

    @Getter
    private NPCRegistry npcRegistry;

    @Getter
    private BotsManager botsManager;
    @Getter
    private QuestManager questManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.botsManager = new BotsManager();
        this.questManager = new QuestManager();
        setupCitizens();
        BotCommand.register();
        new NPCListener(this);
        getLogger().info("FeelyBots has been loaded!");
    }

    @Override
    public void onDisable() {
        if(npcRegistry != null) {
            botsManager.getBots().forEach((uuid, feelyBot) -> {
                feelyBot.getNpc().destroy();
            });
        }
    }

    private void setupCitizens() {
        // Creating an npcRegistry to store all created npc's in this project
        this.npcRegistry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
    }
}
