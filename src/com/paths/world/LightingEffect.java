package com.paths.world;

import java.awt.Graphics2D;
import com.paths.entity.GameObject;

/** Dev 3. Bóng tối trong hầm ngục: phủ lớp đen chuyển dần theo hình tròn, sáng quanh nhân vật. */
public class LightingEffect {

    protected final TileManager tileManager;

    public LightingEffect(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public void draw(Graphics2D g2, GameObject target, Camera camera) {
    }
}
