package com.paths.entity.monster.ai;

import java.util.Random;

import com.paths.core.GamePanel;
import com.paths.entity.monster.Monster;

/**
 * Dev 4. Tuần tra: đi tới một điểm ngẫu nhiên quanh nhà, nghỉ, rồi chọn điểm khác.
 * Mỗi con quái giữ một object riêng nên các biến bên dưới không lẫn giữa các con.
 */
public class PatrolBehavior implements AIState {

    private static final Random RANDOM = new Random();
    private static final double PATROL_RADIUS = GamePanel.TILE_SIZE * 2.5;

    private double targetX, targetY;
    private boolean hasTarget = false;
    private int waitTimer = RANDOM.nextInt(60);
    private int stuckTimer = 0;
    private double lastX, lastY;

    @Override
    public void execute(Monster monster) {
        // Giai đoạn 2: thêm đoạn phát hiện Player ở đây

        if (waitTimer > 0) {
            waitTimer--;
            return;
        }
        if (!hasTarget) {
            pickNewTarget(monster);
        }

        monster.moveToward(targetX, targetY, 0.6); // Tuần tra thì đi thong thả (60% tốc độ)

        // Kiểm tra bị kẹt: vị trí gần như không đổi trong 20 frame liên tiếp
        boolean barelyMoved = Math.abs(monster.getX() - lastX) + Math.abs(monster.getY() - lastY) < 0.3;
        stuckTimer = barelyMoved ? stuckTimer + 1 : 0;
        lastX = monster.getX();
        lastY = monster.getY();

        double distToTarget = Math.hypot(targetX - monster.getCenterX(), targetY - monster.getCenterY());
        if (distToTarget < 3 || stuckTimer > 20) {
            hasTarget = false;
            stuckTimer = 0;
            waitTimer = 30 + RANDOM.nextInt(90);
        }
    }

    private void pickNewTarget(Monster monster) {
        double angle = RANDOM.nextDouble() * Math.PI * 2;
        double radius = RANDOM.nextDouble() * PATROL_RADIUS;
        targetX = monster.getHomeX() + Math.cos(angle) * radius;
        targetY = monster.getHomeY() + Math.sin(angle) * radius;
        hasTarget = true;
    }
}