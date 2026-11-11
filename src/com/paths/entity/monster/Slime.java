package com.paths.entity.monster;

import com.paths.core.GamePanel;
import com.paths.core.World;

/** Dev 4. Slime: 30 máu, chậm, chạm vào mất 8 máu. */
public class Slime extends Monster {

    public Slime(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 30, 1, 8);
    }

    @Override
    public String getKind() {
        return "SLIME";
    }
}
