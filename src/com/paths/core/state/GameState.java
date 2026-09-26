package com.paths.core.state;

import java.awt.Graphics2D;

/**
 * PHỤ TRÁCH: Dev 1 (Leader)
 *
 * Interface này định nghĩa "hợp đồng" cho MỌI trạng thái của game
 * (Menu, Đang chơi, Tạm dừng, Game Over, Chiến thắng...).
 *
 * ĐÂY LÀ VÍ DỤ VỀ TÍNH TRỪU TƯỢNG (Abstraction):
 * Chúng ta không cần biết bên trong MenuState hay PlayState làm gì cụ thể,
 * GameStateManager chỉ cần biết "một GameState thì phải có update() và render()".
 *
 * ĐÂY CŨNG LÀ VÍ DỤ VỀ TÍNH ĐA HÌNH (Polymorphism):
 * GameStateManager gọi state.update() -> tùy vào state đang là MenuState hay
 * PlayState mà hành vi thực thi khác nhau hoàn toàn, dù cùng 1 câu lệnh gọi.
 */
public interface GameState {

    /** Được gọi 1 lần khi state này bắt đầu được kích hoạt (vd: reset lại vị trí quái) */
    void onEnter();

    /** Cập nhật logic của riêng state này mỗi frame */
    void update();

    /** Vẽ riêng cho state này mỗi frame */
    void render(Graphics2D g2);

    /** Được gọi khi chuyển sang state khác (dọn dẹp tài nguyên nếu cần) */
    void onExit();
}
