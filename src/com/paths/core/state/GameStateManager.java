package com.paths.core.state;

import java.awt.Graphics2D;
import com.paths.core.GamePanel;

/**
 * PHỤ TRÁCH: Dev 1 (Leader)
 *
 * Quản lý state hiện tại của game và chuyển đổi giữa các state.
 * GamePanel chỉ gọi duy nhất manager.update() / manager.render() mỗi frame,
 * không cần biết đang ở state nào -> giảm coupling, dễ mở rộng thêm state mới
 * (vd sau này thêm ShopState, CutsceneState... chỉ cần tạo class mới implements GameState).
 */
public class GameStateManager {

    private GameState currentState;

    public GameStateManager(GamePanel gamePanel) {
        // Sprint 1: vào thẳng màn chơi để cả nhóm test được ngay.
        // TODO (Dev 1, Sprint 4): khi MenuState xong, đổi thành setState(new MenuState(...));
        setState(new PlayState(gamePanel));
    }

    /** Chuyển sang state mới: thoát state cũ đúng cách rồi mới vào state mới */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.onExit();
        }
        currentState = newState;
        currentState.onEnter();
    }

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    public void render(Graphics2D g2) {
        if (currentState != null) {
            currentState.render(g2);
        }
    }
}
