package com.paths.quest;

/** Dev 5. Một nhiệm vụ. targetKey là loại sự kiện được tính: KILL_SLIME, KILL_SKELETON, KILL_BOSS, OPEN_CHEST. */
public class Quest {

    protected final String id;
    protected final String title;
    protected final String description;
    protected final String targetKey;
    protected final int targetAmount;
    protected int progress = 0;

    public Quest(String id, String title, String description, String targetKey, int targetAmount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetKey = targetKey;
        this.targetAmount = targetAmount;
    }

    public void addProgress(int amount) {
    }

    public boolean isCompleted() {
        return false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getTargetKey() { return targetKey; }
    public int getTargetAmount() { return targetAmount; }
    public int getProgress() { return progress; }
}
