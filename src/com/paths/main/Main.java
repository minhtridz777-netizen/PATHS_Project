package com.paths.main;

import javax.swing.JFrame;
import com.paths.core.GamePanel;

/** Dev 1. Điểm khởi đầu: mở cửa sổ, gắn GamePanel và bật vòng lặp game. */
public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("PATHS - 2D Top-down Action RPG");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); // Cửa sổ tự co vừa đúng kích thước GamePanel đã khai báo

        window.setLocationRelativeTo(null); // Canh giữa màn hình
        window.setVisible(true);

        gamePanel.requestFocusInWindow(); // Để bàn phím vào thẳng GamePanel
        gamePanel.startGameThread();
    }
}
