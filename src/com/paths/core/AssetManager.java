package com.paths.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Dev 1. Load và cache ảnh (Singleton). Ảnh là tùy chọn: không có file thì getImage() trả về null
 * và nơi gọi tự vẽ bằng hình khối. Đường dẫn tính từ res/, ví dụ "/tiles/grass.png".
 */
public class AssetManager {

    private static AssetManager instance;

    private final Map<String, BufferedImage> imageCache = new HashMap<>();
    private final Set<String> missing = new HashSet<>(); // Nhớ các file không tồn tại để khỏi tìm lại mỗi frame

    private AssetManager() {
    }

    public static synchronized AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    /** Trả về ảnh đã cache, hoặc null nếu không có file đó */
    public synchronized BufferedImage getImage(String resourcePath) {
        BufferedImage cached = imageCache.get(resourcePath);
        if (cached != null || missing.contains(resourcePath)) {
            return cached;
        }
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                missing.add(resourcePath);
                return null;
            }
            BufferedImage image = ImageIO.read(is);
            imageCache.put(resourcePath, image);
            return image;
        } catch (IOException e) {
            System.err.println("[AssetManager] Lỗi khi đọc ảnh: " + resourcePath);
            missing.add(resourcePath);
            return null;
        }
    }
}
