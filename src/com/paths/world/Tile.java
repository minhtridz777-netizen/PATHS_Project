package com.paths.world;

import java.awt.image.BufferedImage;

/**
 * PHỤ TRÁCH: Dev 3
 *
 * Đại diện cho 1 LOẠI tile (vd "cỏ", "nước", "tường đá"...), KHÔNG phải 1 ô cụ thể
 * trên bản đồ. TileManager sẽ giữ 1 mảng Tile[] (bảng loại tile) và 1 ma trận số
 * nguyên mapTileNum[][] để biết ô nào trên bản đồ dùng loại tile nào (xem TileManager.java).
 *
 * Tách riêng như vậy giúp tiết kiệm bộ nhớ: 1 ảnh "cỏ" chỉ load 1 lần dù xuất hiện
 * hàng trăm lần trên bản đồ.
 */
public class Tile {
    public BufferedImage image;
    public boolean collision; // true = người chơi/quái không đi xuyên qua được (vd tường, nước sâu)
}
