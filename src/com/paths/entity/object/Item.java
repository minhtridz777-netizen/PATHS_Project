package com.paths.entity.object;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.inventory.Inventory;
import com.paths.ui.UIManager;
import com.paths.world.Camera;

/** Dev 4. Vật phẩm trên bản đồ, chạm vào là nhặt. drawIcon() cũng được dùng để vẽ trong túi đồ. */
public class Item extends GameObject implements IInteractable {

    public static final int SIZE = 32;
    private static final int POTION_HEAL_TEXT = 30;

    private final ItemType type;
    private boolean pickedUp = false;
    private int spawnDelay = 20;
    private int bobCounter;

    public Item(double x, double y, ItemType type) {
        super(x, y, SIZE, SIZE);
        this.type = type;
        this.bobCounter = (int) (x + y); // Mỗi vật phẩm nhấp nhô lệch nhịp nhau cho tự nhiên
    }

    @Override
    public void update() {
        bobCounter++;
        if (spawnDelay > 0) {
            spawnDelay--;
        }
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        if (pickedUp) {
            return;
        }
        int sx = screenX(camera);
        int sy = screenY(camera);
        int bob = (int) Math.round(Math.sin(bobCounter * 0.1) * 3); // Nhấp nhô lên xuống 3px

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(sx + 6, sy + height - 6, width - 12, 6);
        drawIcon(g2, sx, sy - 4 + bob, width);

        if (type == ItemType.GEM && (bobCounter / 10) % 3 == 0) { // Ngọc lấp lánh
            g2.setColor(Color.WHITE);
            g2.fillRect(sx + width - 8, sy + bob, 3, 3);
        }
    }

    /** Vẽ biểu tượng pixel-art trên lưới 16x16 tại (x, y), cỡ size x size */
    public void drawIcon(Graphics2D g2, int x, int y, int size) {
        double u = size / 16.0;
        switch (type) {
            case POTION -> {
                px(g2, x, y, u, new Color(120, 80, 50), 6, 1, 4, 2);     // nút bần
                px(g2, x, y, u, new Color(200, 214, 230), 6, 3, 4, 3);   // cổ chai
                px(g2, x, y, u, new Color(200, 40, 60), 3, 6, 10, 8);    // thân chai
                px(g2, x, y, u, new Color(200, 40, 60), 4, 5, 8, 10);
                px(g2, x, y, u, new Color(240, 90, 110), 5, 7, 2, 3);    // vệt sáng
            }
            case GOLD -> {
                px(g2, x, y, u, new Color(150, 104, 56), 4, 6, 8, 8);    // túi
                px(g2, x, y, u, new Color(150, 104, 56), 3, 8, 10, 5);
                px(g2, x, y, u, new Color(110, 72, 40), 6, 4, 4, 2);     // miệng túi buộc dây
                px(g2, x, y, u, new Color(250, 214, 92), 6, 9, 4, 4);    // đồng xu in trên túi
                px(g2, x, y, u, new Color(214, 160, 40), 7, 10, 2, 2);
            }
            case GEM -> {
                Color light = new Color(130, 230, 255);
                Color mid = new Color(60, 170, 230);
                Color dark = new Color(30, 110, 180);
                px(g2, x, y, u, light, 5, 3, 6, 2);                      // mặt trên
                px(g2, x, y, u, mid, 3, 5, 10, 2);
                px(g2, x, y, u, mid, 4, 7, 8, 2);                        // thu nhỏ dần thành mũi kim cương
                px(g2, x, y, u, dark, 5, 9, 6, 2);
                px(g2, x, y, u, dark, 6, 11, 4, 2);
                px(g2, x, y, u, dark, 7, 13, 2, 1);
                px(g2, x, y, u, Color.WHITE, 6, 3, 2, 1);
            }
            case SWORD -> {
                Color blade = new Color(222, 230, 240);
                for (int i = 0; i < 7; i++) {
                    px(g2, x, y, u, blade, 12 - i, 2 + i, 2, 2);         // lưỡi kiếm chéo
                }
                px(g2, x, y, u, new Color(232, 186, 64), 3, 9, 5, 2);    // chắn tay vàng
                px(g2, x, y, u, new Color(232, 186, 64), 5, 7, 2, 5);
                px(g2, x, y, u, new Color(120, 80, 50), 2, 12, 2, 2);    // chuôi
            }
        }
    }

    private static void px(Graphics2D g2, int x, int y, double u, Color c, double gx, double gy, double gw, double gh) {
        g2.setColor(c);
        int x0 = x + (int) Math.round(gx * u);
        int y0 = y + (int) Math.round(gy * u);
        g2.fillRect(x0, y0, x + (int) Math.round((gx + gw) * u) - x0, y + (int) Math.round((gy + gh) * u) - y0);
    }

    @Override
    public void onInteract(Player player) {
        if (!canInteract()) {
            return;
        }
        UIManager ui = player.getWorld().getUIManager();
        Inventory inventory = player.getInventory();

        switch (type) {
            case GOLD -> {
                player.addGold(10);
                ui.showMessage("+10 vàng");
            }
            case GEM -> {
                player.addGold(50);
                ui.showMessage("Ngọc quý! +50 vàng");
            }
            case POTION -> {
                if (!inventory.addItem(this)) {
                    ui.showMessage("Túi đồ đã đầy!");
                    return; // Không đánh dấu pickedUp -> vật phẩm vẫn nằm trên đất
                }
                ui.showMessage("Nhặt Bình máu (+" + POTION_HEAL_TEXT + " máu, nhấn H để uống)");
            }
            case SWORD -> {
                if (!inventory.addItem(this)) {
                    ui.showMessage("Túi đồ đã đầy!");
                    return;
                }
                player.addAttackBonus(5);
                ui.showMessage("Kiếm sắt! Sức tấn công +5");
            }
        }
        pickedUp = true;
    }

    @Override
    public String getInteractionPrompt() {
        return "Nhặt " + type.getDisplayName();
    }

    @Override
    public boolean canInteract() {
        return !pickedUp && spawnDelay == 0;
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