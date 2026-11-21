package com.paths.entity.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.monster.Monster;
import com.paths.entity.monster.Slime;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 2 — BẢN GIAI ĐOẠN 2: thử chém quái và bị đánh.
 * Chuột phải vào file -> Run 'PlayerTestMain.main()'
 *
 * Bản đồ và Slime đã có từ Giai đoạn 1 (Leader đã ghép), nên ở đây dùng luôn.
 *   SPACE: chém (vùng chém hiện khung vàng)     K: tự nhận 10 sát thương (thử bất tử + nháy)
 * Số trên đầu Slime là máu còn lại (Slime 30 máu, mỗi nhát chém 10 -> 3 nhát chết).
 */
public class PlayerTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(6), World.tile(11));
        world.addMonster(new Slime(world, World.tile(9), World.tile(11)));
        world.addMonster(new Slime(world, World.tile(4), World.tile(12)));
        world.addMonster(new Slime(world, World.tile(7), World.tile(9)));

        TestWindow.open("Player - chiến đấu", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_K)) player.takeDamage(10);
                    world.update();
                },
                g2 -> {
                    world.render(g2);
                    Camera cam = world.getCamera();
                    g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                    for (Monster m : world.getMonsters()) {
                        g2.setColor(Color.WHITE);
                        g2.drawString(m.getCurrentHp() + " HP", m.screenX(cam) + 8, m.screenY(cam) - 4);
                    }
                    Rectangle box = player.getAttackBox();
                    if (!box.isEmpty()) {
                        g2.setColor(Color.YELLOW);
                        g2.drawRect(box.x - cam.getX(), box.y - cam.getY(), box.width, box.height);
                    }
                    drawInfo(g2, player);
                });
    }

    private static void drawInfo(Graphics2D g2, Player p) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, GamePanel.SCREEN_HEIGHT - 26, GamePanel.SCREEN_WIDTH, 26);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);
        g2.drawString("SPACE: chém   K: tự nhận 10 sát thương   |   HP " + p.getCurrentHp()
                + (p.isInvincible() ? "  (đang bất tử)" : ""), 10, GamePanel.SCREEN_HEIGHT - 8);
    }
}