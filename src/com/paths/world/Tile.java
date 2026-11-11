package com.paths.world;

import java.awt.image.BufferedImage;

/** Dev 3. Một loại ô đất (không phải một ô cụ thể). Bản đồ chỉ lưu mã loại của từng ô. */
public class Tile {
    public String name;
    public BufferedImage image; // Ảnh (tùy chọn). null thì TileManager vẽ bằng hình khối
    public boolean collision;   // true = không đi xuyên qua được (tường, nước, cây)

    public Tile(String name, boolean collision) {
        this.name = name;
        this.collision = collision;
    }
}
