package com.paths.entity.monster.ai;

import com.paths.entity.monster.Monster;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Interface đại diện cho 1 "hành vi AI" của quái (Tuần tra, Rượt đuổi, Đứng yên...).
 * Đây là áp dụng STRATEGY PATTERN: thay vì viết if/else khổng lồ trong Monster.update()
 * để xử lý mọi trạng thái AI, ta tách mỗi hành vi thành 1 class riêng (PatrolBehavior,
 * ChaseBehavior...) rồi Monster chỉ giữ 1 tham chiếu currentState và gọi:
 *
 *      currentState.execute(this);
 *
 * TÍNH ĐA HÌNH: cùng 1 lời gọi execute(monster) nhưng PatrolBehavior sẽ cho quái đi
 * qua lại giữa 2 điểm, còn ChaseBehavior sẽ cho quái lao thẳng về phía Player.
 * Muốn thêm hành vi mới (vd "bỏ chạy khi yếu máu") chỉ cần thêm 1 class mới implements
 * AIState, KHÔNG cần sửa code trong Monster.java (tuân thủ nguyên lý Open/Closed).
 */
public interface AIState {
    void execute(Monster monster);
}
