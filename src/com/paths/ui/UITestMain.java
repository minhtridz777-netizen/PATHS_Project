package com.paths.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 5 — chạy riêng phần giao diện với dữ liệu GIẢ.
 * Chuột phải vào file -> Run 'UITestMain.main()'
 *
 * Bấm phím để giả lập sự kiện, xem HUD phản ứng đúng chưa:
 *   K: mất 10 máu    L: hồi 10 máu    G: +10 vàng    M: hiện 1 thông báo
 */
public class UITestMain {

    private static int messageCount = 0;

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(7), World.tile(5));
        UIManager ui = world.getUIManager();

        TestWindow.open("Giao diện (HUD)", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_K)) player.takeDamage(10);
                    if (keys.consumePress(KeyEvent.VK_L)) player.heal(10);
                    if (keys.consumePress(KeyEvent.VK_G)) player.addGold(10);
                    if (keys.consumePress(KeyEvent.VK_M)) ui.showMessage("Thông báo thử số " + (++messageCount));
                    world.update();
                },
                g2 -> {
                    g2.setColor(new Color(52, 84, 58));
                    g2.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
                    world.render(g2);
                    drawHelp(g2);
                });
    }

    private static void drawHelp(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("K: -10 máu   L: +10 máu   G: +10 vàng   M: thông báo", 420, GamePanel.SCREEN_HEIGHT - 12);
    }
}