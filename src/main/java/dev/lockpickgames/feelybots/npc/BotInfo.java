package dev.lockpickgames.feelybots.npc;


import java.util.UUID;

public interface BotInfo {

    int getId();

    UUID getUUID();

    String getName();

    String getRole();

}
