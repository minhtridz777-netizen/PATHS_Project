package com.paths.entity.player;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.core.World;
import com.paths.entity.Direction;
import com.paths.entity.Entity;
import com.paths.inventory.Inventory;
import com.paths.world.Camera;

/** Dev 2. Nhân vật chính: di chuyển, chém, bị đánh, tương tác, nhặt đồ, lướt. */
public class Player extends Entity {

    public static final int BASE_DAMAGE = 10;

    private final World world;
    private final KeyHandler keys;
    private final Inventory inventory = new Inventory(20);
    private final PlayerAnimator animator = new PlayerAnimator();

    private int gold = 0;
    private int attackBonus = 0;
    private boolean moving = false;

    public Player(World world, KeyHandler keys) {
        super(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 100, 4);
        this.world = world;
        this.keys = keys;
    }

    @Override
    public void update() {
        updateCommonTimers(world.getCollisionChecker()); // Luôn gọi đầu tiên (hiệu ứng trúng đòn, đẩy lùi)
        handleMovement();
        animator.update(moving);
    }

    /** Đọc phím WASD, tính hướng đi rồi di chuyển có kiểm tra va chạm */
    private void handleMovement() {
        double dx = 0;
        double dy = 0;
        if (keys.upPressed) dy -= 1;
        if (keys.downPressed) dy += 1;
        if (keys.leftPressed) dx -= 1;
        if (keys.rightPressed) dx += 1;

        moving = dx != 0 || dy != 0;
        if (!moving) {
            return;
        }

        // Hướng nhìn: đi chéo thì ưu tiên hướng ngang (giống đa số game top-down)
        if (dy < 0) direction = Direction.UP;
        if (dy > 0) direction = Direction.DOWN;
        if (dx < 0) direction = Direction.LEFT;
        if (dx > 0) direction = Direction.RIGHT;

        double length = Math.hypot(dx, dy);
        dx = dx / length * speed;
        dy = dy / length * speed;

        moveWithCollision(dx, dy, world.getCollisionChecker());
    }

    @Override
    public void attack() {
        // Giai đoạn 2
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        animator.draw(g2, screenX(camera), screenY(camera), width, direction, isHurt());
    }

    /**
     * Hitbox nhỏ hơn hình vẽ: chỉ lấy phần thân dưới (24 x 26 px).
     * Nhờ vậy đầu nhân vật "chồng" lên tường phía trên một chút, nhìn tự nhiên hơn,
     * và dễ đi lọt qua lối đi rộng đúng 1 ô.
     */
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x + 12, (int) y + 20, width - 24, height - 22);
    }

    public Rectangle getAttackBox() {
        return new Rectangle();
    }

    public boolean isAttacking() {
        return false;
    }

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