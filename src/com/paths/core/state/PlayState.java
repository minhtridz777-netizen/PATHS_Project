package com.paths.core.state;

import java.awt.Graphics2D;
import com.paths.core.GamePanel;

/**
 * PHỤ TRÁCH: Dev 1 (Leader) - đây là state "ráp nối" toàn bộ sản phẩm của Dev 2,3,4,5.
 *
 * State chính khi người chơi đang thật sự chơi game (không phải đang ở menu).
 * Đây là nơi Dev 1 gọi update()/draw() của Player (Dev2), TileManager (Dev3),
 * Monster/Item/Chest (Dev4), UIManager (Dev5) theo đúng thứ tự layer.
 */
public class PlayState implements GameState {

    private final GamePanel gamePanel;

    public PlayState(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void onEnter() {
        // TODO (Dev 1): Load map đầu tiên, spawn player tại điểm bắt đầu, spawn quái theo map data.
    }

    @Override
    public void update() {
        // TODO (Dev 1): gọi lần lượt:
        // gamePanel.getPlayer().update();
        // gamePanel.getMonsterManager().updateAll(gamePanel.getPlayer());
        // gamePanel.getCollisionChecker().checkAll(...);
        // gamePanel.getCamera().update(gamePanel.getPlayer());
        // gamePanel.getQuestManager().checkProgress();
    }

    @Override
    public void render(Graphics2D g2) {
        // TODO (Dev 1): vẽ theo đúng thứ tự layer đã mô tả trong GamePanel.paintComponent()
    }

    @Override
    public void onExit() {
        // Dọn dẹp nếu cần khi rời khỏi PlayState (vd tạm dừng nhạc nền)
    }
}
