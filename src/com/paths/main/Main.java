package com.paths.main;

import javax.swing.JFrame;
import com.paths.core.GamePanel;

/**
 * PHỤ TRÁCH: Dev 1 (Leader)
 *
 * Đây là điểm khởi đầu (entry point) của toàn bộ game.
 * Nhiệm vụ của class này CHỈ là:
 *  1. Tạo cửa sổ (JFrame) để chứa game.
 *  2. Cấu hình cửa sổ (kích thước, tiêu đề, không cho resize, đóng app khi bấm X...).
 *  3. Tạo GamePanel (nơi chứa toàn bộ logic vẽ + game loop) và nhét vào JFrame.
 *  4. Gọi startGameThread() để bắt đầu vòng lặp game.
 *
 * LƯU Ý: Không viết thêm logic game ở đây. Mọi logic phải nằm trong GamePanel
 * hoặc các class con khác. Main.java chỉ đóng vai trò "bật công tắc".
 */
public class Main {

    public static void main(String[] args) {

        // Tạo cửa sổ chính của game bằng Swing JFrame
        JFrame window = new JFrame();

        // Đóng chương trình hoàn toàn khi người dùng bấm nút X
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setResizable(false); // Không cho kéo giãn cửa sổ để tránh vỡ layout game 2D
        window.setTitle("PATHS - 2D Top-down Action RPG");

        // Tạo GamePanel - đây là "trái tim" của game, chứa Game Loop, render, update
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // pack() sẽ tự động set kích thước cửa sổ vừa khít với preferredSize
        // đã khai báo bên trong GamePanel (xem GamePanel.java)
        window.pack();

        window.setLocationRelativeTo(null); // Canh cửa sổ ra giữa màn hình
        window.setVisible(true);

        // Bắt đầu Game Loop (chạy trên 1 Thread riêng để không đứng hình UI)
        // TODO (Dev 1): Sau khi các Dev khác hoàn thành module, kiểm tra lại
        // GamePanel.startGameThread() có khởi tạo đúng thứ tự Asset -> Map -> Player -> UI chưa.
        gamePanel.startGameThread();
    }
}
