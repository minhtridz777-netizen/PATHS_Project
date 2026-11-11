package com.paths.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Dev 1. Cửa sổ thử để mỗi thành viên chạy riêng phần mình (dùng trong các file XxxTestMain):
 * truyền vào hàm update và hàm vẽ, TestWindow gọi cả hai khoảng 60 lần mỗi giây.
 */
public final class TestWindow {

    private TestWindow() {
    }

    public static void open(String title, KeyHandler keys, Runnable update, Consumer<Graphics2D> render) {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    render.accept(g2);
                    g2.dispose();
                }
            };
            panel.setPreferredSize(new Dimension(GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT));
            panel.setBackground(Color.BLACK);
            panel.setFocusable(true);
            panel.addKeyListener(keys);

            JFrame frame = new JFrame("[TEST] " + title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();

            // Timer của Swing gọi update + vẽ mỗi ~16ms (khoảng 60 FPS), đủ để test
            new Timer(1000 / GamePanel.FPS, e -> {
                update.run();
                panel.repaint();
            }).start();
        });
    }
}
