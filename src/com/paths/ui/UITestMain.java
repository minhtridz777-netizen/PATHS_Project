package com.paths.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.monster.Boss;
import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 5 — BẢN GIAI ĐOẠN 2: thử bản đồ nhỏ, vệt máu trễ, thanh máu Boss.
 * Chuột phải vào file -> Run 'UITestMain.main()'
 *
 * Bản đồ, quái, Player di chuyển đã có từ Giai đoạn 1, nên gọi luôn world.spawnLevel()
 * của Leader để có đủ quái trên bản đồ nhỏ.
 *   WASD: đi (chấm trắng trên bản đồ nhỏ chạy theo)   K: -10 máu (xem vệt trễ)   L: +10 máu
 *   B: vẽ thử thanh máu Boss (bình thường chỉ hiện khi Boss đuổi theo - cần code của Dev 4)
 */
public class UITestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        world.spawnLevel();
        Player player = world.getPlayer();
        HealthBar testBossBar = new HealthBar(234, 104, 300, 14);
        boolean[] showBoss = {false}; // Mảng 1 phần tử: mẹo để sửa được biến bên trong lambda

        TestWindow.open("Giao diện - Giai đoạn 2", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_K)) player.takeDamage(10);
                    if (keys.consumePress(KeyEvent.VK_L)) player.heal(10);
                    if (keys.consumePress(KeyEvent.VK_B)) showBoss[0] = !showBoss[0];
                    world.update();
                },
                g2 -> {
                    world.render(g2);
                    Boss boss = world.getBoss();
                    if (showBoss[0] && boss != null) {
                        testBossBar.draw(g2, boss);
                    }
                    drawHelp(g2);
                });
    }

    private static void drawHelp(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("WASD: đi   K: -10 máu   L: +10 máu   B: thử thanh máu Boss",
                360, GamePanel.SCREEN_HEIGHT - 12);
    }
}