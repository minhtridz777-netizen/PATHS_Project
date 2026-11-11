package com.paths.core.state;

import java.awt.Graphics2D;
import com.paths.core.World;

/** Dev 1. Màn hình đang chơi: tạo World khi bắt đầu ván rồi giao cho World update/vẽ mỗi frame. */
public class PlayState implements GameState {

    private final GameStateManager manager;
    private World world;

    public PlayState(GameStateManager manager) {
        this.manager = manager;
    }

    @Override
    public void onEnter() {
        if (world == null) { // Chỉ tạo World ở lần vào đầu tiên, quay lại từ Pause thì giữ nguyên
            world = new World(manager.getGamePanel().getKeyHandler());
            world.spawnLevel();
        }
    }

    @Override
    public void update() {
        world.update();
    }

    @Override
    public void render(Graphics2D g2) {
        world.render(g2);
    }

    @Override
    public void onExit() {
    }

    public World getWorld() {
        return world;
    }
}
