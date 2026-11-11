package com.paths.world;

import java.awt.Rectangle;
import com.paths.entity.Entity;
import com.paths.entity.GameObject;

/**
 * Dev 3. Va chạm: canMove() kiểm tra hitbox ở vị trí mới có đè lên ô chặn đường không,
 * intersects() kiểm tra hai vật thể chạm nhau.
 */
public class CollisionChecker {

    protected final TileManager tileManager;

    public CollisionChecker(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    /** Bản khung: luôn cho đi (chưa có va chạm tường) */
    public boolean canMove(Entity entity, double dx, double dy) {
        return true;
    }

    public boolean intersects(GameObject a, GameObject b) {
        return a.getBounds().intersects(b.getBounds());
    }

    public boolean intersects(Rectangle area, GameObject obj) {
        return area.intersects(obj.getBounds());
    }
}
