package dev.lockpickgames.feelybots.manager;

import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.npc.Quest;
import dev.lockpickgames.feelybots.npc.QuestDifficulty;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class QuestManager {

    private final Map<Integer, Quest> quests = new HashMap<>();

    public QuestManager() {
        this.loadQuests();
    }

    public void loadQuests() {
        FileConfiguration config = FeelyBots.getInstance().getConfig();
        for(String id : config.getConfigurationSection("quests").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("quests." + id);
            int questId = Integer.parseInt(id);
            assert section != null;
            String title = section.getString("title");
            List<String> description = section.getStringList("description");
            List<String> rewards = section.getStringList("rewards");
            int goal = section.getInt("goal");
            String icon = section.getString("icon");
            String target = section.getString("target");
            QuestDifficulty difficulty = QuestDifficulty.valueOf(section.getString("difficulty").toUpperCase());
            icon = icon == null ? "dirt" : icon;
            Quest quest = new Quest(questId, title, description, difficulty, goal, icon, target, rewards);
            this.quests.put(questId, quest);
        }
    }

    public Quest getQuest(int questId) {
        return this.quests.get(questId);
    }
}
