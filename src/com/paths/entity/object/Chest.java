package com.paths.entity.object;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Rương kho báu: đứng yên trên map, khi Player bấm E để mở thì "nhả" ra danh sách
 * Item bên trong (loot). Cũng implements IInteractable giống Item, nhưng hành vi
 * onInteract() hoàn toàn khác (mở rương thay vì nhặt trực tiếp) - MINH HỌA ĐA HÌNH:
 * PlayState xử lý Item và Chest bằng CÙNG một đoạn code (ép kiểu IInteractable rồi
 * gọi onInteract()), nhưng kết quả thực thi khác nhau tùy loại object.
 *
 * TODO (Dev 4):
 *  1. Đổi sprite khi isOpen = true (rương mở nắp).
 *  2. onInteract(): nếu chưa mở thì set isOpen = true, sinh ngẫu nhiên loot rồi
 *     tạo ra các Item mới trên map xung quanh rương (phối hợp Dev 1 để thêm vào danh sách
 *     item đang active trong PlayState).
 */
public class Chest extends GameObject implements IInteractable {

    private boolean isOpen = false;
    private final List<Item> loot = new ArrayList<>();

    public Chest(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        // Rương đứng yên, không cần cập nhật logic mỗi frame.
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        int screenX = (int) x - camera.getX();
        int screenY = (int) y - camera.getY();
        if (sprite != null) {
            g2.drawImage(sprite, screenX, screenY, width, height, null);
        }
    }

    @Override
    public void onInteract(Player player) {
        if (isOpen) return;
        isOpen = true;
        // TODO (Dev 4): sinh ngẫu nhiên vật phẩm vào danh sách loot rồi báo cho
        // PlayState (Dev 1) biết để "spawn" các Item đó ra xung quanh vị trí rương.
    }

    @Override
    public String getInteractionPrompt() {
        return isOpen ? "" : "Nhan [E] de mo ruong";
    }

    public boolean isOpen() {
        return isOpen;
    }
}
