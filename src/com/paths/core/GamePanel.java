package com.paths.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

import com.paths.core.state.GameStateManager;

/**
 * Dev 1. Vòng lặp game 60 FPS trên luồng riêng: mỗi frame update() rồi repaint().
 * Không tự biết đang ở màn hình nào, mọi việc giao cho GameStateManager.
 * frameLock ngăn luồng game và luồng vẽ của Swing cùng đụng vào danh sách quái/vật phẩm.
 */
public class GamePanel extends JPanel implements Runnable {

    // ====== HẰNG SỐ DÙNG CHUNG CHO CẢ NHÓM (không ai được tự đổi) ======
    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 48 px

    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;   // 768 px
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;  // 576 px

    public static final int FPS = 60;

    private Thread gameThread;
    private final Object frameLock = new Object();

    private final KeyHandler keyHandler = new KeyHandler();
    private final GameStateManager stateManager;

    private int currentFps = 0; // FPS đo được thật, Leader hiển thị trong chế độ debug (F3)

    public GamePanel() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(keyHandler);
        setFocusable(true);

        stateManager = new GameStateManager(this);
    }

    public void startGameThread() {
        gameThread = new Thread(this, "GameLoop");
        gameThread.start();
    }

    /** Game Loop kiểu delta time: luôn giữ 60 lần update mỗi giây, máy mạnh hay yếu đều vậy */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long fpsTimer = 0;
        int frames = 0;

        while (gameThread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;
            fpsTimer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                synchronized (frameLock) {
                    update();
                }
                repaint();
                delta--;
                frames++;
            } else {
                try {
                    Thread.sleep(1); // Nghỉ 1ms cho CPU đỡ chạy 100%
                } catch (InterruptedException e) {
                    return;
                }
            }

            if (fpsTimer >= 1_000_000_000L) {
                currentFps = frames;
                frames = 0;
                fpsTimer = 0;
            }
        }
    }

    public void update() {
        stateManager.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Tạo bản sao Graphics để tự dispose(), không dispose bản gốc của Swing
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR); // Ảnh pixel phóng to vẫn sắc nét
        synchronized (frameLock) {
            stateManager.render(g2);
        }
        g2.dispose();
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public GameStateManager getStateManager() {
        return stateManager;
    }

    public int getCurrentFps() {
        return currentFps;
    }
}
