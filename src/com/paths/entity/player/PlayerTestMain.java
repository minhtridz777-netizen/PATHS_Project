package com.paths.entity.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;

/**
 * PHỤ TRÁCH: Dev 2 — chạy riêng phần Player, không cần chờ ai.
 * Chuột phải vào file -> Run 'PlayerTestMain.main()'
 *
 * Giai đoạn 1: kiểm tra đi 8 hướng, đi chéo không nhanh hơn đi thẳng, hoạt ảnh bước chân.
 * (Chưa có bản đồ và va chạm tường: đó là việc của Dev 3, sẽ có sau khi Leader ghép code.)
 */
public class PlayerTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(7), World.tile(5));

        TestWindow.open("Player", keys,
                world::update,
                g2 -> {
                    g2.setColor(new Color(52, 84, 58));
                    g2.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
                    world.render(g2);
                    drawInfo(g2, player);
                });
    }

    private static void drawInfo(Graphics2D g2, Player p) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("WASD / mũi tên: di chuyển", 12, GamePanel.SCREEN_HEIGHT - 32);
        g2.drawString(String.format("x = %.1f   y = %.1f   hướng = %s", p.getX(), p.getY(), p.getDirection()),
                12, GamePanel.SCREEN_HEIGHT - 12);
    }
}