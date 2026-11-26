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

import java.util.HashSet;
import java.util.Set;
import com.paths.entity.monster.Monster;
import com.paths.world.CollisionChecker;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import com.paths.entity.object.Item;
import com.paths.interfaces.IInteractable;

/** Dev 2. Nhân vật chính: di chuyển, chém, bị đánh, tương tác, nhặt đồ, lướt. */
public class Player extends Entity {

    public static final int BASE_DAMAGE = 10;
    private static final int ATTACK_DURATION = 14;
    private static final int ATTACK_COOLDOWN = 22;
    private static final int INVINCIBLE_FRAMES = 50;
    private static final int ATTACK_REACH = 40;
    private static final double INTERACT_RANGE = GamePanel.TILE_SIZE * 1.3;

    private final World world;
    private final KeyHandler keys;
    private final Inventory inventory = new Inventory(20);
    private final PlayerAnimator animator = new PlayerAnimator();

    private int gold = 0;
    private int attackBonus = 0;
    private boolean moving = false;
    private int attackTimer = 0;
    private int attackCooldown = 0;
    private int invincibleTimer = 0;
    private final Set<Monster> hitThisSwing = new HashSet<>();

    public Player(World world, KeyHandler keys) {
        super(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, 100, 4);
        this.world = world;
        this.keys = keys;
    }

    @Override
    public void update() {
        updateCommonTimers(world.getCollisionChecker()); // Luôn gọi đầu tiên (hiệu ứng trúng đòn, đẩy lùi)
        if (!alive) {
            moving = false;
            return;
        }
        if (invincibleTimer > 0) invincibleTimer--;
        if (attackCooldown > 0) attackCooldown--;

        if (attackTimer > 0) {
            attackTimer--;
            applyAttackHits(); // Đang chém thì đứng yên, chỉ kiểm tra trúng quái
        } else {
            handleMovement();
            if (keys.attackPressed && attackCooldown == 0) {
                attack();
            }
        }
        handleInteraction();
        pickUpItems();
        animator.update(moving);
    }

    /** Bấm E: tương tác với rương / NPC gần nhất */
    private void handleInteraction() {
        if (keys.consumePress(KeyEvent.VK_E)) {
            IInteractable target = world.findNearestInteractable(this, INTERACT_RANGE);
            if (target != null) {
                target.onInteract(this);
            }
        }
    }

    /** Chạm vào vật phẩm là nhặt. Duyệt trên bản sao vì danh sách có thể thay đổi trong lúc nhặt */
    private void pickUpItems() {
        CollisionChecker cc = world.getCollisionChecker();
        for (Item item : new ArrayList<>(world.getItems())) {
            if (item.canInteract() && cc.intersects(this, item)) {
                item.onInteract(this);
            }
        }
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

    /** Bắt đầu 1 nhát chém */
    @Override
    public void attack() {
        attackTimer = ATTACK_DURATION;
        attackCooldown = ATTACK_COOLDOWN;
        hitThisSwing.clear();
        moving = false;
    }

    /** Quái nào nằm trong vùng chém và chưa trúng nhát này thì mất máu + bị đẩy lùi */
    private void applyAttackHits() {
        Rectangle box = getAttackBox();
        CollisionChecker cc = world.getCollisionChecker();
        for (Monster m : world.getMonsters()) {
            if (m.isDead() || hitThisSwing.contains(m)) {
                continue;
            }
            if (cc.intersects(box, m)) {
                m.takeDamage(getAttackPower());
                m.applyKnockback(this, 7);
                hitThisSwing.add(m);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        // Nhấp nháy khi bất tử: cứ 4 frame ẩn, 4 frame hiện
        if (alive && invincibleTimer > 0 && (invincibleTimer / 4) % 2 == 0) {
            return;
        }
        double attackProgress = isAttacking() ? 1.0 - (double) attackTimer / ATTACK_DURATION : -1;
        animator.draw(g2, screenX(camera), screenY(camera), width, direction, isHurt(), attackProgress,
                !alive, attackBonus > 0);
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

    /**
     * Vùng chém: hình chữ nhật dài ATTACK_REACH px nằm trước mặt, theo hướng đang nhìn.
     * Tính từ hitbox thân (getBounds) để vùng chém luôn sát người.
     */
    public Rectangle getAttackBox() {
        if (!isAttacking()) {
            return new Rectangle();
        }
        Rectangle b = getBounds();
        int cx = b.x + b.width / 2;
        int cy = b.y + b.height / 2;
        int r = ATTACK_REACH;
        return switch (direction) {
            case UP -> new Rectangle(cx - 24, b.y - r, 48, r + 4);
            case DOWN -> new Rectangle(cx - 24, b.y + b.height - 4, 48, r + 4);
            case LEFT -> new Rectangle(b.x - r, cy - 24, r + 4, 48);
            case RIGHT -> new Rectangle(b.x + b.width - 4, cy - 24, r + 4, 48);
        };
    }

    @Override
    public void takeDamage(int amount) {
        if (invincibleTimer > 0 || !alive) {
            return;
        }
        super.takeDamage(amount); // Dùng lại logic trừ máu của Entity
        invincibleTimer = INVINCIBLE_FRAMES;
        world.getCamera().shake(12, 5);
    }

    public boolean isAttacking() {
        return attackTimer > 0;
    }

    public boolean isInvincible() {
        return invincibleTimer > 0;
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