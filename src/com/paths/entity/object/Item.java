package com.paths.entity.object;

import java.awt.Graphics2D;
import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Vật phẩm nằm trên bản đồ (rớt ra từ quái, hoặc đặt sẵn để người chơi nhặt).
 * implements IInteractable vì người chơi "tương tác" (đi qua/bấm E) để nhặt nó lên
 * - đây là ví dụ khác của TRỪU TƯỢNG: PlayState không cần biết Item hay Chest,
 * chỉ cần biết object đó IInteractable là gọi được onInteract().
 *
 * TODO (Dev 4):
 *  1. Thêm enum ItemType (WEAPON, POTION, KEY, TREASURE...) và field type.
 *  2. onInteract(): thêm item này vào player.getInventory() (phối hợp Dev 5), sau đó
 *     đánh dấu item này "đã nhặt" để PlayState loại khỏi danh sách vẽ trên map.
 */
public class Item extends GameObject implements IInteractable {

    protected String name;
    protected String description;
    private boolean pickedUp = false;

    public Item(double x, double y, int width, int height, String name, String description) {
        super(x, y, width, height);
        this.name = name;
        this.description = description;
    }

    @Override
    public void update() {
        // Đa số Item đứng yên trên map nên không cần logic gì đặc biệt.
        // TODO (Dev 4, tùy chọn nâng cao): thêm hiệu ứng nhấp nháy/bồng bềnh (bobbing) cho đẹp mắt.
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        if (pickedUp) return;
        int screenX = (int) x - camera.getX();
        int screenY = (int) y - camera.getY();
        if (sprite != null) {
            g2.drawImage(sprite, screenX, screenY, width, height, null);
        }
    }

    @Override
    public void onInteract(Player player) {
        // TODO (Dev 4, phối hợp Dev 5): player.getInventory().addItem(this);
        pickedUp = true;
    }

    @Override
    public String getInteractionPrompt() {
        return "Nhan [E] de nhat " + name;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public String getName() {
        return name;
    }
}
