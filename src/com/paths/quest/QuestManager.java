package com.paths.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Quản lý danh sách toàn bộ quest đang hoạt động (active) của người chơi.
 * PlayState (Dev 1) sẽ gọi checkProgress() mỗi frame (hoặc mỗi khi có sự kiện liên
 * quan như giết quái/nhặt đồ) để cập nhật tiến độ quest tương ứng.
 *
 * TODO (Dev 5):
 *  1. addQuest(Quest): thêm quest mới vào activeQuests khi Player nhận quest từ NPC.
 *  2. Thêm cơ chế lắng nghe sự kiện: vd khi Monster (Dev 4) chết, gọi
 *     questManager.onMonsterKilled(monsterType) để cộng dồn tiến độ các quest liên quan
 *     đến việc giết loại quái đó.
 */
public class QuestManager {

    private final List<Quest> activeQuests = new ArrayList<>();
    private final List<Quest> completedQuests = new ArrayList<>();

    public void addQuest(Quest quest) {
        activeQuests.add(quest);
    }

    public void checkProgress() {
        // TODO (Dev 5): duyệt qua activeQuests, nếu quest.isCompleted() thì
        // chuyển từ activeQuests sang completedQuests.
        activeQuests.removeIf(q -> {
            if (q.isCompleted()) {
                completedQuests.add(q);
                return true;
            }
            return false;
        });
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }
}
