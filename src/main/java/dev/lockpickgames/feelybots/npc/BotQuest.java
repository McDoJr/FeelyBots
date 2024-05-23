package dev.lockpickgames.feelybots.npc;

import dev.lockpickgames.feelybots.utils.ItemBuilder;
import dev.lockpickgames.feelybots.utils.StringUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class BotQuest implements InventoryHolder {

    private final List<Quest> quests;
    private Inventory inventory;
    private final String title;

    public BotQuest(String title) {
        this.title = title;
        this.quests = new ArrayList<>();
    }

    public void addQuest(Quest...quests) {
        this.quests.addAll(Arrays.asList(quests));
    }

    private void setupUI() {
        for(Quest quest : getQuests()) {
            inventory.addItem(new ItemBuilder(quest.getIcon())
                    .name(quest.getTitle())
                    .lore(quest.getUILore())
                    .addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build());
        }
    }

    public void handleClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }

    public void openUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 36, StringUtil.translate(title + "'s Quests"));
        this.setupUI();
        player.openInventory(getInventory());
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }
}
