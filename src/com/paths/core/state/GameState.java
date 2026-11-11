package com.paths.core.state;

import java.awt.Graphics2D;

/** Dev 1. Hợp đồng của mọi màn hình (Menu, Chơi, Tạm dừng, Thua, Thắng) theo State Pattern. */
public interface GameState {

    /** Gọi 1 lần khi bắt đầu vào màn hình này */
    void onEnter();

    /** Cập nhật logic mỗi frame */
    void update();

    /** Vẽ mỗi frame */
    void render(Graphics2D g2);

    /** Gọi 1 lần khi rời màn hình này */
    void onExit();
}
