package com.paths.quest;

import java.util.ArrayList;
import java.util.List;
import com.paths.core.World;

/** Dev 5. Nhiệm vụ đang làm và đã xong. World báo sự kiện (quái chết, mở rương) để cộng tiến độ. */
public class QuestManager {

    protected final World world;
    protected final List<Quest> activeQuests = new ArrayList<>();
    protected final List<Quest> completedQuests = new ArrayList<>();

    public QuestManager(World world) {
        this.world = world;
    }

    public void addQuest(Quest quest) {
        activeQuests.add(quest);
    }

    public void onMonsterKilled(String kind) {
    }

    public void onChestOpened() {
    }

    public void update() {
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }

    public boolean isAllCompleted() {
        return false;
    }
}
