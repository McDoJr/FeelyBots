package dev.lockpickgames.feelybots.manager;

import dev.lockpickgames.feelybots.npc.FeelyBot;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class BotsManager {

    public Map<UUID, FeelyBot> bots = new ConcurrentHashMap<>();

    public void createBot(FeelyBot feelyBot) {
        this.bots.putIfAbsent(feelyBot.getUUID(), feelyBot);
    }

    public void removeBot(UUID uuid) {
        this.bots.remove(uuid);
    }

    public Optional<FeelyBot> getBot(UUID uuid) {
        return bots.containsKey(uuid) ? Optional.of(bots.get(uuid)) : Optional.empty();
    }
}
