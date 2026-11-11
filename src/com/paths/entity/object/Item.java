package com.paths.entity.object;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/** Dev 4. Vật phẩm trên bản đồ, chạm vào là nhặt. drawIcon() cũng được dùng để vẽ trong túi đồ. */
public class Item extends GameObject implements IInteractable {

    public static final int SIZE = 32;

    private final ItemType type;
    private boolean pickedUp = false;

    public Item(double x, double y, ItemType type) {
        super(x, y, SIZE, SIZE);
        this.type = type;
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        if (!pickedUp) {
            drawIcon(g2, screenX(camera), screenY(camera), width);
        }
    }

    /** Vẽ biểu tượng vật phẩm tại tọa độ màn hình (x, y), kích thước size x size */
    public void drawIcon(Graphics2D g2, int x, int y, int size) {
        g2.setColor(new Color(230, 200, 60));
        g2.fillRect(x + size / 4, y + size / 4, size / 2, size / 2);
    }

    @Override
    public void onInteract(Player player) {
        pickedUp = true; // Bản khung: chỉ đánh dấu là đã nhặt
    }

    @Override
    public String getInteractionPrompt() {
        return "Nhặt " + type.getDisplayName();
    }

    @Override
    public boolean canInteract() {
        return !pickedUp;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public ItemType getType() {
        return type;
    }

    public String getName() {
        return type.getDisplayName();
    }
}
