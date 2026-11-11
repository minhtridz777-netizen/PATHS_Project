package com.paths.world;

import com.paths.entity.GameObject;

/** Dev 3. Độ lệch (x, y) giữa thế giới và màn hình: bám theo mục tiêu, làm mượt, kẹp biên, rung. */
public class Camera {

    protected int x;
    protected int y;
    protected final int screenWidth;
    protected final int screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /** Cập nhật vị trí camera để target nằm giữa màn hình, không lộ ra ngoài bản đồ */
    public void update(GameObject target, int mapWidthPx, int mapHeightPx) {
    }

    /** Rung màn hình trong frames frame, biên độ intensity pixel (vd khi bị đánh trúng) */
    public void shake(int frames, int intensity) {
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
