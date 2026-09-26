package com.paths.quest;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Đại diện cho 1 nhiệm vụ (quest) trong game, vd "Tiêu diệt 3 con quái Slime"
 * hoặc "Tìm được chiếc rương kho báu ở hang động phía Bắc".
 *
 * TODO (Dev 5):
 *  1. Thêm field targetAmount + currentAmount (vd cần giết 3 quái, hiện đã giết 1).
 *  2. Thêm method updateProgress(int amount) để cộng dồn tiến độ, tự set isCompleted = true
 *     khi currentAmount >= targetAmount.
 */
public class Quest {

    private final String id;
    private final String title;
    private final String description;
    private boolean completed = false;

    public Quest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public void complete() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
