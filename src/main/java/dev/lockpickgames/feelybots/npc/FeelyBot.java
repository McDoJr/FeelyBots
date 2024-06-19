package dev.lockpickgames.feelybots.npc;

import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.manager.QuestManager;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

@Getter
public class FeelyBot extends BotQuest implements BotInfo {

    private final String name;
    private final String role;
    private NPC npc;

    public FeelyBot(String name) {
        this(name, name);
    }

    public FeelyBot(String name, String role) {
        super(name);
        this.name = name;
        this.role = role;
        this.init();
    }

    private void init() {
        FeelyBots plugin = FeelyBots.getInstance();
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        this.npc.addTrait(new EmotionTrait());
        this.npc.setProtected(true);
        LookClose trait = this.npc.getOrAddTrait(LookClose.class);
        trait.lookClose(true);
        QuestManager questManager = plugin.getQuestManager();
        this.addQuest(questManager.getQuest(1), questManager.getQuest(2));
    }

    public void spawn(Location location) {
        this.npc.spawn(location, SpawnReason.CREATE);
    }

    @Override
    public int getId() {
        return this.npc.getId();
    }

    @Override
    public UUID getUUID() {
        return this.npc.getUniqueId();
    }
}
