package com.paths.core.state;

import java.awt.Graphics2D;
import com.paths.core.GamePanel;

/** Dev 1. Giữ màn hình hiện tại và chuyển màn hình bằng setState(). */
public class GameStateManager {

    private final GamePanel gamePanel;
    private GameState currentState;

    public GameStateManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        // TODO (Dev 1, Giai đoạn 4): đổi thành setState(new MenuState(this)) khi đã có màn hình menu
        setState(new PlayState(this));
    }

    /** Rời state cũ cho gọn gàng rồi mới vào state mới */
    public void setState(GameState newState) {
        if (currentState != null) {
            currentState.onExit();
        }
        gamePanel.getKeyHandler().clearPresses(); // Phím bấm ở màn cũ không "dính" sang màn mới
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

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
