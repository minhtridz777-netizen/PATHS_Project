package com.paths.world;

import java.util.Random;
import com.paths.core.GamePanel;
import com.paths.entity.GameObject;

/** Dev 3. Độ lệch (x, y) giữa thế giới và màn hình: bám theo mục tiêu, làm mượt, kẹp biên, rung. */
public class Camera {

    protected int x;
    protected int y;
    protected final int screenWidth;
    protected final int screenHeight;

    // Biến phục vụ hiệu ứng rung màn hình
    private int shakeFrames = 0;
    private int shakeIntensity = 0;
    private final Random random = new Random();

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /** Cập nhật vị trí camera để target nằm giữa màn hình, không lộ ra ngoài bản đồ */
    public void update(GameObject target, int mapWidthPx, int mapHeightPx) {
        if (target == null) return;

        // 1. Tính tọa độ để tâm của target nằm chính giữa màn hình
        int centerX = (int) (target.getX() + GamePanel.TILE_SIZE / 2);
        int centerY = (int) (target.getY() + GamePanel.TILE_SIZE / 2);

        int targetCamX = centerX - screenWidth / 2;
        int targetCamY = centerY - screenHeight / 2;

        // 2. Kẹp biên để không lộ khoảng đen ngoài rìa bản đồ
        int maxCamX = Math.max(0, mapWidthPx - screenWidth);
        int maxCamY = Math.max(0, mapHeightPx - screenHeight);

        this.x = Math.max(0, Math.min(targetCamX, maxCamX));
        this.y = Math.max(0, Math.min(targetCamY, maxCamY));

        // 3. Áp dụng độ lệch rung màn hình nếu đang trong thời gian hiệu ứng
        if (shakeFrames > 0) {
            int offsetX = random.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            int offsetY = random.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            this.x += offsetX;
            this.y += offsetY;
            shakeFrames--;
        }
    }

    /** Rung màn hình trong frames frame, biên độ intensity pixel (vd khi bị đánh trúng) */
    public void shake(int frames, int intensity) {
        if (frames > 0 && intensity > 0) {
            this.shakeFrames = frames;
            this.shakeIntensity = intensity;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}