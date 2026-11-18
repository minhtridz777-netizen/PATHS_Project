package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;

/**
 * PHỤ TRÁCH: Dev 4 — chạy riêng phần quái vật.
 * Chuột phải vào file -> Run 'MonsterTestMain.main()'
 *
 * Giai đoạn 1: kiểm tra 3 loại quái vẽ đúng, nhún nhảy / bước chân, đi tuần tra quanh chỗ xuất hiện.
 * Vòng tròn mờ = vùng tuần tra của từng con.
 */
public class MonsterTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        world.getPlayer().setPosition(-500, -500); // Đưa Player (của Dev 2) ra khỏi màn hình cho đỡ rối

        world.addMonster(new Slime(world, World.tile(3), World.tile(3)));
        world.addMonster(new Slime(world, World.tile(5), World.tile(8)));
        world.addMonster(new Skeleton(world, World.tile(11), World.tile(3)));
        world.addMonster(new Skeleton(world, World.tile(13), World.tile(8)));
        world.addMonster(new Boss(world, World.tile(7), World.tile(4)));

        TestWindow.open("Quái vật", keys,
                world::update,
                g2 -> {
                    g2.setColor(new Color(52, 84, 58));
                    g2.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
                    for (Monster m : world.getMonsters()) {
                        int r = (int) (GamePanel.TILE_SIZE * 2.5);
                        g2.setColor(new Color(255, 255, 255, 30));
                        g2.fillOval((int) m.getHomeX() - r, (int) m.getHomeY() - r, r * 2, r * 2);
                    }
                    world.render(g2);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    g2.drawString("Slime (trái) - Boss (giữa) - Skeleton (phải). Quan sát tuần tra.",
                            12, GamePanel.SCREEN_HEIGHT - 12);
                });
    }
}