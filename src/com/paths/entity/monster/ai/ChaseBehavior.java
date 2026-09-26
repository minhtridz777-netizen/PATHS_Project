package com.paths.entity.monster.ai;

import com.paths.entity.monster.Monster;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Hành vi RƯỢT ĐUỔI: quái di chuyển thẳng về phía vị trí hiện tại của Player.
 *
 * TODO (Dev 4):
 *  1. Lấy tọa độ Player (cần Monster giữ 1 tham chiếu tới Player, hoặc truyền qua tham số).
 *  2. Tính vector hướng (player.x - monster.x, player.y - monster.y), chuẩn hóa rồi
 *     nhân với speed của monster để ra dx, dy mỗi frame.
 *  3. Nếu khoảng cách tới Player vượt quá losePlayerRange (xa hơn detectionRange 1 chút,
 *     tránh việc dao động liên tục giữa 2 state) thì chuyển lại monster.setAIState(new PatrolBehavior()).
 *  4. Nếu khoảng cách đủ gần (trong tầm đánh), gọi monster.attack().
 */
public class ChaseBehavior implements AIState {

    @Override
    public void execute(Monster monster) {
        // TODO (Dev 4): logic đuổi theo Player + kiểm tra điều kiện quay lại PatrolBehavior
    }
}
