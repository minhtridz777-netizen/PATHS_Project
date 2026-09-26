package com.paths.world;

import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 3
 *
 * Camera 2D bám theo Player. Bản chất Camera KHÔNG "di chuyển" bất kỳ hình ảnh nào,
 * nó chỉ giữ 1 độ lệch (offset x, y) để MỌI class khác khi vẽ đều trừ đi offset này:
 *      screenX = worldX - camera.getX()
 *      screenY = worldY - camera.getY()
 * Nhờ vậy khi Player đi tới, "cả thế giới" trông như đang trôi ngược lại, tạo cảm
 * giác camera đang bám theo nhân vật, trong khi Player luôn được vẽ ở giữa màn hình.
 *
 * TODO (Dev 3):
 *  1. update(player): tính camera x/y sao cho player luôn ở giữa màn hình
 *     (x = player.getX() - SCREEN_WIDTH/2).
 *  2. Xử lý "kẹp biên" (clamp): không cho camera đi ra ngoài rìa bản đồ (vd khi player
 *     đứng sát góc map, camera không nên lộ ra vùng đen ngoài bản đồ).
 */
public class Camera {

    private int x;
    private int y;
    private final int screenWidth;
    private final int screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update(Player player) {
        // TODO (Dev 3): tính offset để player luôn nằm giữa màn hình, ví dụ:
        // x = (int) player.getX() - screenWidth / 2;
        // y = (int) player.getY() - screenHeight / 2;
        // TODO (Dev 3): clamp x,y trong khoảng [0, mapWidthPixels - screenWidth] và tương tự cho y.
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
