package com.paths.entity.monster;

import com.paths.core.GamePanel;
import com.paths.core.World;

/** Dev 4. Skeleton: 50 máu, nhanh gấp đôi Slime, chạm vào mất 12 máu. */
public class Skeleton extends Monster {

    public Skeleton(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 50, 2, 12);
    }

    @Override
    public String getKind() {
        return "SKELETON";
    }
}
