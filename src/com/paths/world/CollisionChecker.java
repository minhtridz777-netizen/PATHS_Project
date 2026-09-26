package com.paths.world;

import java.awt.Rectangle;
import com.paths.core.GamePanel;
import com.paths.entity.Entity;
import com.paths.entity.GameObject;

/**
 * PHỤ TRÁCH: Dev 3
 *
 * Chịu trách nhiệm kiểm tra va chạm ở 2 dạng:
 *  1. Va chạm với Tilemap (Entity có đi được vào 1 ô tile hay không - vd tường, nước).
 *  2. Va chạm giữa 2 GameObject bất kỳ (Entity vs Entity, dùng Rectangle.intersects()) -
 *     phục vụ Dev 2 (tấn công), Dev 4 (quái đụng player gây damage, nhặt item...).
 *
 * TODO (Dev 3):
 *  1. Hoàn thiện canMoveToTile(): tính ra 4 góc của hitbox entity SAU KHI di chuyển
 *     (dx, dy), đổi từng góc sang tọa độ (cột, hàng) tile bằng cách chia cho TILE_SIZE,
 *     rồi gọi tileManager.isTileBlocking(col, row) cho cả 4 góc.
 *  2. checkEntityCollision() dùng chung cho mọi cặp GameObject (Player-Monster,
 *     Player-Item, Player-Chest...) chỉ cần so 2 hitbox bằng Rectangle.intersects().
 */
public class CollisionChecker {

    private final TileManager tileManager;

    public CollisionChecker(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    /**
     * Kiểm tra 1 Entity có thể di chuyển thêm (dx, dy) mà không đâm vào tile chặn đường không.
     * TODO (Dev 3): implement đầy đủ logic tính 4 góc hitbox như mô tả ở trên.
     */
    public boolean canMoveToTile(Entity entity, double dx, double dy) {
        Rectangle futureBounds = entity.getBounds();
        futureBounds.translate((int) dx, (int) dy);

        int leftCol = futureBounds.x / GamePanel.TILE_SIZE;
        int rightCol = (futureBounds.x + futureBounds.width) / GamePanel.TILE_SIZE;
        int topRow = futureBounds.y / GamePanel.TILE_SIZE;
        int bottomRow = (futureBounds.y + futureBounds.height) / GamePanel.TILE_SIZE;

        return !tileManager.isTileBlocking(leftCol, topRow)
                && !tileManager.isTileBlocking(rightCol, topRow)
                && !tileManager.isTileBlocking(leftCol, bottomRow)
                && !tileManager.isTileBlocking(rightCol, bottomRow);
    }

    /** Kiểm tra va chạm giữa 2 GameObject bất kỳ (dùng chung cho toàn team) */
    public boolean checkObjectCollision(GameObject a, GameObject b) {
        return a.getBounds().intersects(b.getBounds());
    }
}
