package dev.lockpickgames.feelybots.npc;

import lombok.Getter;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;


@Getter
@TraitName("emotiontrait")
public class EmotionTrait extends Trait {

    private Player talkingTo;

    public EmotionTrait() {
        super("emotiontrait");
    }

    public void startConversation(NPCRightClickEvent event) {
        if(event.getNPC() != npc) return;
        Player player = event.getClicker();

        if(talkingTo == null) {
            startConversation(player);
        }else {
            if(talkingTo != player) {

                //See if the person the NPC is talking to is within 20 blocks
                if (npc.getEntity().getLocation().distance(this.talkingTo.getLocation()) > 20){
                    this.talkingTo.sendMessage("The " + this.getName() + " NPC stopped talking to you because you moved too far away.");
                    startConversation(player);
                }

                player.sendMessage("I am talking to someone else right now!");

            }else {
                player.sendMessage("I'm already talking to you!");
            }
        }
    }

    private void startConversation(Player player) {
        this.talkingTo = player;
        CompletableFuture.runAsync(() -> handleResponse(talkingTo, null));
    }

    public void stopConversation() {
        this.talkingTo.sendMessage("You are no longer talking to the " + this.getName() + " NPC.");
        this.talkingTo = null;
    }

    public void handleResponse(Player player, String message) {

        if(message == null) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Start talking in chat."));
            return;
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("Thinking..."));

        EmotionAnalysis.analyzeEmotion(message, response -> {
            player.sendMessage(EmotionAnalysis.getEmotionResult(response));
        });
    }
}
