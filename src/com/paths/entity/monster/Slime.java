package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.object.ItemType;
import com.paths.entity.object.LootTable;
/** Dev 4. Slime: 30 máu, chậm, chạm vào mất 8 máu. */
public class Slime extends Monster {
    /** 60% rơi Túi vàng, 25% rơi Bình máu */
    private static final LootTable LOOT = new LootTable().add(ItemType.GOLD, 0.6).add(ItemType.POTION, 0.25);
    private static final Color BODY = new Color(88, 196, 106);
    private static final Color BODY_DARK = new Color(47, 122, 60);
    private static final Color SHINE = new Color(196, 244, 200);
    private static final Color EYE = new Color(24, 40, 30);

    public Slime(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 30, 1, 8);
    }
    @Override
    protected LootTable getLootTable() {
        return LOOT;
    }
    @Override
    public String getKind() {
        return "SLIME";
    }

    @Override
    protected void drawBody(Graphics2D g2, int sx, int sy) {
        // squash: 0..2 ô lưới, đi thì nhún nhanh hơn đứng yên
        double speedAnim = walking ? 0.25 : 0.1;
        int squash = (int) Math.round((Math.sin(animCounter * speedAnim) + 1)); // 0, 1 hoặc 2
        int top = 5 + squash;

        fill(g2, sx, sy, BODY_DARK, 2, top + 1, 12, 14 - top);   // viền dưới đậm
        fill(g2, sx, sy, BODY, 3, top, 10, 13 - top);             // thân
        fill(g2, sx, sy, BODY, 2, top + 2, 12, 11 - top);
        fill(g2, sx, sy, SHINE, 4, top + 1, 2, 2);                // đốm sáng

        // Mắt nhìn theo hướng đi
        int look = switch (direction) {
            case LEFT -> -1;
            case RIGHT -> 1;
            default -> 0;
        };
        fill(g2, sx, sy, EYE, 5 + look, top + 4, 2, 2);
        fill(g2, sx, sy, EYE, 9 + look, top + 4, 2, 2);
    }
}