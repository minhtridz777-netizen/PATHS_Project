package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.Direction;

/** Dev 4. Skeleton: 50 máu, nhanh gấp đôi Slime, chạm vào mất 12 máu. */
public class Skeleton extends Monster {

    private static final Color BONE = new Color(232, 228, 214);
    private static final Color BONE_DARK = new Color(170, 164, 150);
    private static final Color HOLE = new Color(40, 30, 40);
    private static final Color EYE_GLOW = new Color(255, 120, 80);

    public Skeleton(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 50, 2, 12);
    }

    @Override
    public String getKind() {
        return "SKELETON";
    }

    @Override

    protected void drawBody(Graphics2D g2, int sx, int sy) {
        int step = walking ? ((animCounter / 8) % 2 == 0 ? 1 : -1) : 0;

        // Chân
        fill(g2, sx, sy, BONE_DARK, 6, 12 + Math.max(0, step), 1, 3 - Math.max(0, step));
        fill(g2, sx, sy, BONE_DARK, 9, 12 + Math.max(0, -step), 1, 3 - Math.max(0, -step));
        // Xương sườn
        fill(g2, sx, sy, BONE, 5, 7, 6, 5);
        fill(g2, sx, sy, HOLE, 6, 8, 4, 1);
        fill(g2, sx, sy, HOLE, 6, 10, 4, 1);
        fill(g2, sx, sy, BONE, 7, 7, 2, 5);                    // xương sống
        // Tay
        fill(g2, sx, sy, BONE_DARK, 4, 7 - step, 1, 4);
        fill(g2, sx, sy, BONE_DARK, 11, 7 + step, 1, 4);
        // Sọ
        fill(g2, sx, sy, BONE, 5, 1, 6, 6);
        fill(g2, sx, sy, BONE_DARK, 5, 6, 6, 1);
        if (direction != Direction.UP) {
            int look = direction == Direction.LEFT ? -1 : direction == Direction.RIGHT ? 1 : 0;
            fill(g2, sx, sy, HOLE, 6 + look, 3, 2, 2);
            fill(g2, sx, sy, HOLE, 9 + look, 3, 2, 2);
            fill(g2, sx, sy, EYE_GLOW, 6 + look, 3, 1, 1);
            fill(g2, sx, sy, EYE_GLOW, 9 + look, 3, 1, 1);
        }
    }

}