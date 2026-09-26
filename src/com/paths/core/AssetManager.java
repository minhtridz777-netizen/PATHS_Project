package com.paths.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * PHỤ TRÁCH: Dev 1 (Leader)
 *
 * Quản lý tập trung việc load và cache toàn bộ hình ảnh (sprite) của game,
 * để KHÔNG có chuyện mỗi class (Player, Monster, Tile...) tự load ảnh riêng lẻ
 * gây tốn bộ nhớ và load chậm khi trùng lặp.
 *
 * Áp dụng SINGLETON PATTERN (chỉ có đúng 1 AssetManager tồn tại trong suốt vòng đời game)
 * - đây là ví dụ thực tế của tính ĐÓNG GÓI (Encapsulation): constructor được giấu đi
 * (private), thế giới bên ngoài chỉ có thể lấy instance duy nhất qua getInstance(),
 * không thể tự ý new AssetManager() bừa bãi ở nhiều nơi.
 */
public class AssetManager {

    private static AssetManager instance;

    // Cache toàn bộ ảnh đã load, key là đường dẫn resource, value là ảnh đã đọc
    private final Map<String, BufferedImage> imageCache = new HashMap<>();

    private AssetManager() {
        // Constructor private để không ai new() được từ bên ngoài (Singleton)
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    /**
     * Load 1 ảnh từ thư mục resources (vd "/player/player_down_1.png").
     * Nếu ảnh đã được load trước đó rồi thì lấy lại từ cache, không đọc file lại lần 2.
     *
     * TODO (Dev 2, 3, 4): Khi cần vẽ sprite trong class của mình, gọi:
     *   AssetManager.getInstance().getImage("/player/player_down_1.png");
     * thay vì tự viết ImageIO.read() lặp lại ở từng class.
     */
    public BufferedImage getImage(String resourcePath) {
        if (imageCache.containsKey(resourcePath)) {
            return imageCache.get(resourcePath);
        }

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("[AssetManager] Khong tim thay resource: " + resourcePath);
                return null;
            }
            BufferedImage image = ImageIO.read(is);
            imageCache.put(resourcePath, image);
            return image;
        } catch (IOException e) {
            System.err.println("[AssetManager] Loi khi load anh: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }
}
