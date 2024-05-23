package dev.lockpickgames.feelybots.listener;

import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.npc.BotQuest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class NPCListener implements Listener {

    public NPCListener(FeelyBots plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onNpcClick(NPCRightClickEvent e) {
        UUID uuid = e.getNPC().getUniqueId();
        FeelyBots.getInstance().getBotsManager().getBot(uuid).ifPresent(feelyBot -> {
            feelyBot.openUI(e.getClicker());
        });
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getCurrentItem() == null) return;

        InventoryHolder holder = e.getClickedInventory().getHolder();

        if(holder instanceof BotQuest botQuest) {
            botQuest.handleClick(e);
        }
    }
}
