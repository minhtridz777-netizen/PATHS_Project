package com.paths.entity.monster.ai;

import com.paths.entity.monster.Monster;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Hành vi TUẦN TRA: quái tự động đi lại giữa 1 vài điểm mốc (waypoint) hoặc
 * đi 1 hướng ngẫu nhiên rồi đổi hướng sau vài giây, khi CHƯA phát hiện Player.
 *
 * TODO (Dev 4):
 *  1. Lưu 1 hướng di chuyển hiện tại + bộ đếm thời gian (đổi hướng ngẫu nhiên mỗi 1-2 giây).
 *  2. Di chuyển monster theo hướng đó (nhớ phối hợp CollisionChecker của Dev 3 để
 *     không đi xuyên tường).
 *  3. Mỗi frame kiểm tra khoảng cách tới Player: nếu nhỏ hơn detectionRange thì
 *     gọi monster.setAIState(new ChaseBehavior()) để chuyển sang trạng thái rượt đuổi.
 */
public class PatrolBehavior implements AIState {

    @Override
    public void execute(Monster monster) {
        // TODO (Dev 4): logic tuần tra + kiểm tra điều kiện chuyển sang ChaseBehavior
    }
}
