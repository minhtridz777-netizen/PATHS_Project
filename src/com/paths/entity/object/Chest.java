package com.paths.entity.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.GameObject;
import com.paths.entity.player.Player;
import com.paths.interfaces.IInteractable;
import com.paths.world.Camera;

/** Dev 4. Rương kho báu: bấm E để mở, vật phẩm văng ra xung quanh. Rương vàng (golden) chắc chắn có Kiếm sắt. */
public class Chest extends GameObject implements IInteractable {

    private static final Color WOOD = new Color(150, 100, 48);
    private static final Color WOOD_DARK = new Color(104, 66, 30);
    private static final Color METAL = new Color(120, 124, 136);
    private static final Color GOLD_TRIM = new Color(232, 186, 64);

    protected final World world;
    private final boolean golden;
    private final LootTable loot;
    private boolean open = false;
    private int openTimer = 0;

    public Chest(World world, double x, double y, boolean golden) {
        super(x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
        this.world = world;
        this.golden = golden;
        if (golden) {
            loot = new LootTable().add(ItemType.SWORD, 1).add(ItemType.GEM, 1).add(ItemType.POTION, 1);
        } else {
            loot = new LootTable().add(ItemType.GOLD, 1).add(ItemType.GOLD, 0.5)
                    .add(ItemType.POTION, 0.7).add(ItemType.GEM, 0.25);
        }
    }

    @Override
    public void update() {
        if (open && openTimer < 40) {
            openTimer++;
        }
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        int sx = screenX(camera);
        int sy = screenY(camera);
        double u = width / 16.0;
        Color trim = golden ? GOLD_TRIM : METAL;

        // Ánh sáng tỏa ra khi vừa mở, mờ dần trong 40 frame
        if (open && openTimer < 40) {
            int alpha = (int) (150 * (1 - openTimer / 40.0));
            g2.setColor(new Color(255, 230, 140, alpha));
            int r = (int) (width * 0.4 + openTimer);
            g2.fillOval(sx + width / 2 - r, sy + height / 2 - r, r * 2, r * 2);
        }

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(sx + 4, sy + height - 8, width - 8, 8);

        px(g2, sx, sy, u, WOOD_DARK, 2, 7, 12, 8);   // thân rương
        px(g2, sx, sy, u, WOOD, 3, 8, 10, 6);
        px(g2, sx, sy, u, trim, 2, 10, 12, 1);       // đai kim loại
        px(g2, sx, sy, u, trim, 7, 9, 2, 3);         // ổ khóa

        if (!open) {
            px(g2, sx, sy, u, WOOD_DARK, 2, 4, 12, 4); // nắp đóng
            px(g2, sx, sy, u, WOOD, 3, 4, 10, 2);
            px(g2, sx, sy, u, trim, 2, 6, 12, 1);
        } else {
            px(g2, sx, sy, u, new Color(40, 24, 12), 3, 7, 10, 2); // lòng rương tối
            px(g2, sx, sy, u, WOOD_DARK, 2, 2, 12, 3);              // nắp bật ngửa ra sau
            px(g2, sx, sy, u, trim, 2, 4, 12, 1);
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
        if (open) {
            return;
        }
        open = true;

        // Vật phẩm xếp hình rẻ quạt phía dưới rương
        List<ItemType> drops = loot.roll();
        for (int i = 0; i < drops.size(); i++) {
            double spread = drops.size() == 1 ? 0 : (i / (double) (drops.size() - 1) - 0.5) * 2; // -1 .. 1
            double ix = getCenterX() - Item.SIZE / 2.0 + spread * 34;
            double iy = y + height + 4 - Math.abs(spread) * 10;
            world.addItem(new Item(ix, iy, drops.get(i)));
        }
        world.onChestOpened(this);
        world.getUIManager().showMessage(golden ? "Rương vàng đã mở!" : "Đã mở rương!");
    }

    @Override
    public String getInteractionPrompt() {
        return golden ? "Nhấn [E] để mở rương vàng" : "Nhấn [E] để mở rương";
    }

    @Override
    public boolean canInteract() {
        return !open;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isGolden() {
        return golden;
    }
}