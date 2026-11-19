package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.TestWindow;
import com.paths.core.World;
import com.paths.entity.monster.ai.ChaseBehavior;
import com.paths.entity.player.Player;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 4 — BẢN GIAI ĐOẠN 2: thử rượt đuổi, cắn, chết.
 * Chuột phải vào file -> Run 'MonsterTestMain.main()'
 *
 * Player đã đi được (Dev 2 làm ở Giai đoạn 1). Ở nhánh của Dev 4 Player CHƯA chém được
 * (Dev 2 đang làm song song), nên dùng phím K để gây sát thương cho quái gần nhất.
 *   WASD: đi lại gần quái để bị đuổi    K: đánh quái gần nhất 15 máu
 * Vòng tròn trong = tầm phát hiện, vòng ngoài = tầm bỏ cuộc. Chữ CHASE = đang rượt đuổi.
 */
public class MonsterTestMain {

    public static void main(String[] args) {
        KeyHandler keys = new KeyHandler();
        World world = new World(keys);
        Player player = world.getPlayer();
        player.setPosition(World.tile(6), World.tile(11));

        world.addMonster(new Slime(world, World.tile(12), World.tile(11)));
        world.addMonster(new Skeleton(world, World.tile(13), World.tile(16)));
        world.addMonster(new Slime(world, World.tile(3), World.tile(17)));

        TestWindow.open("Quái vật - rượt đuổi", keys,
                () -> {
                    if (keys.consumePress(KeyEvent.VK_K)) {
                        hitNearest(world, player);
                    }
                    world.update();
                },
                g2 -> {
                    world.render(g2);
                    Camera cam = world.getCamera();
                    for (Monster m : world.getMonsters()) {
                        drawRange(g2, m, cam, m.getDetectionRange(), new Color(255, 80, 80, 90));
                        drawRange(g2, m, cam, m.getLoseRange(), new Color(255, 255, 255, 40));
                        if (m.getAIState() instanceof ChaseBehavior) {
                            g2.setColor(Color.RED);
                            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                            g2.drawString("CHASE", m.screenX(cam) + 4, m.screenY(cam) - 12);
                        }
                    }
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRect(0, GamePanel.SCREEN_HEIGHT - 26, GamePanel.SCREEN_WIDTH, 26);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    g2.drawString("WASD: đi   K: đánh quái gần nhất   |   HP Player: " + player.getCurrentHp(),
                            10, GamePanel.SCREEN_HEIGHT - 8);
                });
    }

    private static void hitNearest(World world, Player player) {
        Monster nearest = null;
        for (Monster m : world.getMonsters()) {
            if (!m.isDead() && (nearest == null || m.distanceTo(player) < nearest.distanceTo(player))) {
                nearest = m;
            }
        }
        if (nearest != null) {
            nearest.takeDamage(15);
            nearest.applyKnockback(player, 7);
        }
    }

    private static void drawRange(Graphics2D g2, Monster m, Camera cam, int r, Color c) {
        g2.setColor(c);
        int cx = (int) m.getCenterX() - cam.getX();
        int cy = (int) m.getCenterY() - cam.getY();
        g2.drawOval(cx - r, cy - r, r * 2, r * 2);
    }
}