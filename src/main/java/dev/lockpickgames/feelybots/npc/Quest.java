package dev.lockpickgames.feelybots.npc;

import dev.lockpickgames.feelybots.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Quest {

    private final int questId;
    private final String title;
    private final List<String> description;
    private final QuestDifficulty difficulty;
    private QuestStatus status;
    private int progress;
    private final int goal;
    private final List<Material> targets;
    private final Material icon;
    private final List<ItemStack> rewards;

    public Quest(int questId, String title, List<String> description, QuestDifficulty difficulty, int goal, String icon, String target, List<String> rewardList) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.status = QuestStatus.AVAILABLE;
        this.progress = 0;
        this.goal = goal;
        this.icon = Material.valueOf(icon.toUpperCase());
        this.targets = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.setTargets(target);
        this.setRewards(rewardList);
    }

    private void setTargets(String target) {
        if(target.contains("{all}")) {
            for(Material material : Material.values()) {
                if(material == null || material.isAir()) continue;
                if(material.name().toLowerCase().endsWith(target.replace("{all}", ""))) {
                    targets.add(material);
                }
            }
        }else {
            Material material = Material.valueOf(target.toUpperCase());
            targets.add(material);
        }
    }

    private void setRewards(List<String> rewardList) {
        for(String reward : rewardList) {
            String[] datas = reward.split(":");
            if(datas.length == 1) {
                rewards.add(new ItemStack(Material.valueOf(reward.toUpperCase())));
            }else {
                Material material = Material.valueOf(datas[0].toUpperCase());
                int amount = Integer.parseInt(datas[1]);
                rewards.add(new ItemStack(material, amount));
            }
        }
    }

    public List<String> getUILore() {
        List<String> lore = new ArrayList<>(description);
        lore.add("");
        lore.add(StringUtil.translate("&aTarget:"));
        int count = 0;
        for(Material material : getTargets()) {
            if(count > 3) {
                lore.add(StringUtil.translate(" &c- &7and more..."));
                break;
            }
            lore.add(StringUtil.translate(" &c- &7" + StringUtil.format(material)));
            count++;
        }
        lore.add("");
        lore.add(StringUtil.translate("&aProgress: &7" + progress + "&c/&7" + goal));
        lore.add("");
        return lore;
    }

    public void updateProgress() {
        this.updateProgress(1);
    }

    public void updateProgress(int amount) {
        this.progress += amount;
        if(this.progress == this.goal) {
            this.status = QuestStatus.FINISHED;
        }
    }
}
