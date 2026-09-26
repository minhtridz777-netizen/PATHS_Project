package com.paths.core.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.paths.core.GamePanel;
import com.paths.entity.Entity;
import com.paths.entity.monster.Monster;
import com.paths.entity.player.Player;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 1 (Leader) - đây là state "ráp nối" toàn bộ sản phẩm của Dev 2,3,4,5.
 *
 * State chính khi người chơi đang thật sự chơi game (không phải đang ở menu).
 * Đây là nơi Dev 1 gọi update()/draw() của Player (Dev2), TileManager (Dev3),
 * Monster/Item/Chest (Dev4), UIManager (Dev5) theo đúng thứ tự layer.
 */
public class PlayState implements GameState {

    private final GamePanel gamePanel;

    /** Danh sách quái đang có trên map (Dev 4 thêm quái vào đây qua getMonsters()) */
    private final List<Monster> monsters = new ArrayList<>();

    public PlayState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void onEnter() {
        // Map đã được TileManager (Dev 3) load ngay trong constructor của nó.
        // TODO (Dev 1, Sprint 1-2): spawn quái theo map, ví dụ:
        // monsters.add(new Monster(10 * GamePanel.TILE_SIZE, 5 * GamePanel.TILE_SIZE,
        //         GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 30, 1));
    }

    /** Thứ tự cập nhật: Player -> Quái -> Camera (camera phải chạy SAU khi player đã di chuyển) */
    @Override
    public void update() {
        Player player = gamePanel.getPlayer();

        player.update();                       // Dev 2
        for (Monster m : monsters) {           // Dev 4
            m.update();
        }
        // TODO (Dev 1, Sprint 2): monsters.removeIf(Monster::isDead); sau khi có loot drop
        gamePanel.getCamera().update(player);  // Dev 3
        // TODO (Dev 1, Sprint 3): gamePanel.getUIManager().getQuestManager().checkProgress();
    }

    /**
     * Thứ tự vẽ (layer) - vẽ sau đè lên vẽ trước:
     *   1. Tilemap -> 2. Item/Chest (Sprint 3) -> 3. Quái -> 4. Player -> 5. UI
     */
    @Override
    public void render(Graphics2D g2) {
        Camera camera = gamePanel.getCamera();
        Player player = gamePanel.getPlayer();

        gamePanel.getTileManager().draw(g2, camera);   // 1. Dev 3

        // 2. TODO (Dev 1, Sprint 3): vẽ Item và Chest ở đây

        for (Monster m : monsters) {                    // 3. Dev 4
            m.draw(g2, camera);
            drawPlaceholderIfNoSprite(g2, camera, m, new Color(220, 70, 60));
        }

        player.draw(g2, camera);                        // 4. Dev 2
        drawPlaceholderIfNoSprite(g2, camera, player, new Color(60, 200, 140));

        gamePanel.getUIManager().draw(g2);              // 5. Dev 5 (không trừ camera)
    }

    @Override
    public void onExit() {
        // Dọn dẹp nếu cần khi rời khỏi PlayState (vd tạm dừng nhạc nền)
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Khi Dev 2 / Dev 4 chưa có ảnh, vẽ tạm 1 ô vuông màu để cả nhóm vẫn thấy được
     * nhân vật/quái và test di chuyển, camera, va chạm. Có ảnh thật rồi thì tự động không vẽ nữa.
     * Vạch trắng nhỏ cho biết hướng đang nhìn.
     */
    private void drawPlaceholderIfNoSprite(Graphics2D g2, Camera camera, Entity e, Color color) {
        if (e.getSprite() != null) {
            return;
        }
        int sx = (int) e.getX() - camera.getX();
        int sy = (int) e.getY() - camera.getY();
        int w = e.getWidth();
        int h = e.getHeight();

        g2.setColor(color);
        g2.fillRect(sx, sy, w, h);
        g2.setColor(Color.WHITE);
        g2.drawRect(sx, sy, w - 1, h - 1);

        int m = w / 4; // độ dày vạch hướng
        switch (e.getDirection()) {
            case UP    -> g2.fillRect(sx + m, sy + 2, w - 2 * m, m / 2);
            case DOWN  -> g2.fillRect(sx + m, sy + h - 2 - m / 2, w - 2 * m, m / 2);
            case LEFT  -> g2.fillRect(sx + 2, sy + m, m / 2, h - 2 * m);
            case RIGHT -> g2.fillRect(sx + w - 2 - m / 2, sy + m, m / 2, h - 2 * m);
        }
    }
}
