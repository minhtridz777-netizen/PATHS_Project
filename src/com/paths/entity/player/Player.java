package com.paths.entity.player;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.World;
import com.paths.entity.Entity;
import com.paths.inventory.Inventory;
import com.paths.world.Camera;

/** Dev 2. Nhân vật chính: di chuyển, chém, bị đánh, tương tác, nhặt đồ, lướt. */
public class Player extends Entity {

    public static final int BASE_DAMAGE = 10;

    private final World world;
    private final KeyHandler keys;
    private final Inventory inventory = new Inventory(20); // Túi đồ (class của Dev 5)

    private int gold = 0;
    private int attackBonus = 0;

    public Player(World world, KeyHandler keys) {
        super(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 100, 4);
        this.world = world;
        this.keys = keys;
    }

    @Override
    public void update() {
        // TODO (Dev 2, Giai đoạn 1): đọc phím và di chuyển
    }

    @Override
    public void attack() {
        // TODO (Dev 2, Giai đoạn 2): vung kiếm gây sát thương lên quái
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        // Bản khung: vẽ tạm ô vuông xanh để cả nhóm thấy vị trí Player
        g2.setColor(new Color(60, 200, 140));
        g2.fillRect(screenX(camera), screenY(camera), width, height);
    }

    /** Vùng kiếm chém trúng (Giai đoạn 2). Bản khung trả về khung rỗng */
    public Rectangle getAttackBox() {
        return new Rectangle();
    }

    public boolean isAttacking() {
        return false;
    }

    /** Sức tấn công = sát thương gốc + cộng thêm từ trang bị (vd nhặt được Kiếm sắt) */
    public int getAttackPower() {
        return BASE_DAMAGE + attackBonus;
    }

    public void addAttackBonus(int amount) {
        attackBonus += amount;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public int getGold() {
        return gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public World getWorld() {
        return world;
    }

    public KeyHandler getKeys() {
        return keys;
    }
}
