package dev.lockpickgames.feelybots.listener;

import dev.lockpickgames.feelybots.FeelyBots;
import dev.lockpickgames.feelybots.npc.EmotionTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.CompletableFuture;

public class NPCListener implements Listener {

    public NPCListener(FeelyBots plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onNpcClick(NPCRightClickEvent event) {
        NPC npc = event.getNPC();

        if(npc.getEntity() == null || npc.getTraitNullable(EmotionTrait.class) == null) return;

        var trait = npc.getTraitNullable(EmotionTrait.class);
        trait.startConversation(event);
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {

        //Get all the npcs near the player
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {

            if (npc.getEntity() == null || npc.getTraitNullable(EmotionTrait.class) == null){
                continue;
            }

            var trait = npc.getTraitNullable(EmotionTrait.class);
            var talkingTo = trait.getTalkingTo();

            //See if the NPC is talking to the player
            if (talkingTo == null || !talkingTo.equals(e.getPlayer())){
                continue;
            }

            //If the player is talking to the NPC but is not within 20 blocks, stop the conversation
            if (npc.getEntity().getLocation().distance(e.getPlayer().getLocation()) > 20){
                trait.stopConversation();
            }else{

                CompletableFuture.runAsync(() -> {
                    //Use OpenAI to get a response
                    trait.handleResponse(talkingTo, e.getMessage());
                });

                //Cancel the event so the message doesn't show up in chat
                e.setCancelled(true);
            }

        }

    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent e) {
//        if(e.getClickedInventory() == null) return;
//        if(e.getCurrentItem() == null) return;
//
//        InventoryHolder holder = e.getClickedInventory().getHolder();
//
//        if(holder instanceof BotQuest botQuest) {
//            botQuest.handleClick(e);
//        }
//    }
}
