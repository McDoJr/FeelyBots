package dev.lockpickgames.feelybots.npc;

import lombok.Getter;

@Getter
public enum QuestStatus {

    UNAVAILABLE("Unavailable"),
    AVAILABLE("Available"),
    IN_PROGRESS("In-Progress"),
    FINISHED("Finished");

    private final String name;

    QuestStatus(String name) {
        this.name = name;
    }
}
