package dev.lockpickgames.feelybots;

import dev.asurasoftware.asuraplugin.AsuraPlugin;
import dev.lockpickgames.feelybots.commands.BotCommand;
import dev.lockpickgames.feelybots.listener.NPCListener;
import dev.lockpickgames.feelybots.manager.BotsManager;
import dev.lockpickgames.feelybots.manager.QuestManager;
import dev.lockpickgames.feelybots.npc.EmotionTrait;
import lombok.Getter;
import lombok.NonNull;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;

public final class FeelyBots extends AsuraPlugin {

    private static final String SECRET_KEY = "h6Y1InfKmZHJ4xnvJGZV7mn7YYNfAQm3hm7A1sxz2oQFvmaK4IWOl8kroJZr8aCM";

    @Getter
    private static FeelyBots instance;

    @Getter
    private BotsManager botsManager;
    @Getter
    private QuestManager questManager;

    private BukkitAudiences adventure;

    @Override
    protected void enable() {
        // Plugin startup logic
        instance = this;
        this.adventure = BukkitAudiences.create(this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.botsManager = new BotsManager();
        this.questManager = new QuestManager();
        BotCommand.register();
        setupCitizens();
        new NPCListener(this);
    }

    @Override
    protected void disable() {
    }

    private void setupCitizens() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(EmotionTrait.class));
    }

    public @NonNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
