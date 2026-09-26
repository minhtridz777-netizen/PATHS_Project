package com.paths.ui;

import java.awt.Graphics2D;
import com.paths.entity.player.Player;

/**
 * PHỤ TRÁCH: Dev 5
 *
 * Vẽ bản đồ thu nhỏ ở góc màn hình, hiển thị vị trí Player (và có thể cả quái/kho báu
 * quan trọng) dưới dạng các chấm nhỏ.
 *
 * TODO (Dev 5):
 *  1. Vẽ khung nền mini-map (hình chữ nhật bán trong suốt ở góc trên phải).
 *  2. Tính tỉ lệ thu nhỏ = kích thước mini-map / kích thước bản đồ thật (px),
 *     rồi quy đổi tọa độ world của Player sang tọa độ trên mini-map.
 *  3. (Nâng cao) nhận thêm danh sách vị trí quái/rương từ Dev 4 để vẽ thêm chấm khác màu.
 */
public class MiniMap {

    private final int x, y, width, height;

    public MiniMap(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2, Player player) {
        // TODO (Dev 5): implement như mô tả ở trên.
    }
}
