package com.paths.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Dev 1. Tiện ích cho ảnh sưu tầm: lấy ảnh đơn, cắt sprite sheet thành lưới khung hình, vẽ lật ngang.
 * Không có file thì trả về null, nơi gọi tự vẽ bằng hình khối như cũ.
 */
public final class Sprites {

    private static final Map<String, BufferedImage[][]> GRIDS = new HashMap<>();

    private Sprites() {
    }

    /** Ảnh đơn trong res/, ví dụ image("/items/potion.png"). null nếu không có file */
    public static BufferedImage image(String path) {
        return AssetManager.getInstance().getImage(path);
    }

    /**
     * Cắt sprite sheet thành lưới đều cols x rows. Kết quả: frames[hàng][cột].
     * Kích thước 1 khung tự tính = (rộng ảnh / cols) x (cao ảnh / rows). null nếu không có file.
     */
    public static synchronized BufferedImage[][] grid(String path, int cols, int rows) {
        String key = path + "#" + cols + "x" + rows;
        if (GRIDS.containsKey(key)) {
            return GRIDS.get(key);
        }
        BufferedImage sheet = image(path);
        BufferedImage[][] frames = null;
        if (sheet != null && cols > 0 && rows > 0) {
            int fw = sheet.getWidth() / cols;
            int fh = sheet.getHeight() / rows;
            frames = new BufferedImage[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    frames[r][c] = sheet.getSubimage(c * fw, r * fh, fw, fh);
                }
            }
        }
        GRIDS.put(key, frames);
        return frames;
    }

    /** Vẽ ảnh vào khung (x, y, w, h). flip = true thì lật ngang (dùng khi nhân vật quay sang trái) */
    public static void draw(Graphics2D g2, BufferedImage img, int x, int y, int w, int h, boolean flip) {
        if (flip) {
            g2.drawImage(img, x + w, y, -w, h, null);
        } else {
            g2.drawImage(img, x, y, w, h, null);
        }
    }
}
