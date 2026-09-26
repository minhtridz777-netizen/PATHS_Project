package com.paths.entity.player;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import com.paths.core.GamePanel;
import com.paths.core.KeyHandler;
import com.paths.entity.Direction;
import com.paths.entity.Entity;
import com.paths.world.Camera;
import com.paths.world.CollisionChecker;

/**
 * PHỤ TRÁCH: Dev 2
 *
 * Class nhân vật chính do người chơi điều khiển. Kế thừa Entity nên có sẵn
 * máu, tốc độ, hướng, khả năng bị đánh (IDamageable) - Dev 2 chỉ cần lo phần
 * ĐỌC INPUT từ KeyHandler và XỬ LÝ DI CHUYỂN/TẤN CÔNG/ANIMATION.
 *
 * TODO (Dev 2) - việc cần làm tiếp theo:
 *  1. Trong update(): đọc keyHandler để đổi x/y và cập nhật direction.
 *  2. Trước khi thật sự đổi x/y, gọi collisionChecker.canMove(this, dx, dy, tileManager)
 *     (phối hợp với Dev 3) để không đi xuyên tường.
 *  3. Cập nhật PlayerAnimator mỗi frame để đổi sprite đi bộ.
 *  4. Implement attack(): tạo hitbox tấn công phía trước theo direction, quét danh sách
 *     Monster (Dev 4 cung cấp) xem có ai trong hitbox thì gọi monster.takeDamage(...).
 *  5. Thêm cooldown giữa 2 lần tấn công (vd attackCooldown đếm ngược mỗi frame).
 */
public class Player extends Entity {

    private final KeyHandler keyHandler;
    private final CollisionChecker collisionChecker;
    private final PlayerAnimator animator;

    private int attackCooldownCounter = 0;
    private static final int ATTACK_COOLDOWN_MAX = 20; // ~ 1/3 giây ở 60FPS

    public Player(GamePanel gamePanel, KeyHandler keyHandler, CollisionChecker collisionChecker) {
        // Vị trí spawn tạm thời (100,100) - TODO (Dev 2 + Dev 3): lấy điểm spawn thật từ map data
        super(100, 100, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, /*maxHp*/ 100, /*speed*/ 4);
        this.keyHandler = keyHandler;
        this.collisionChecker = collisionChecker;
        this.animator = new PlayerAnimator();
    }

    @Override
    public void update() {
        boolean isMoving = keyHandler.upPressed || keyHandler.downPressed
                || keyHandler.leftPressed || keyHandler.rightPressed;

        if (isMoving) {
            double dx = 0, dy = 0;

            if (keyHandler.upPressed)    { direction = Direction.UP;    dy = -speed; }
            if (keyHandler.downPressed)  { direction = Direction.DOWN;  dy = speed;  }
            if (keyHandler.leftPressed)  { direction = Direction.LEFT;  dx = -speed; }
            if (keyHandler.rightPressed) { direction = Direction.RIGHT; dx = speed;  }

            // TODO (Dev 2, phối hợp Dev 3): thay vì cộng thẳng x/y, phải kiểm tra va chạm trước:
            // if (collisionChecker.canMove(this, dx, dy)) { x += dx; y += dy; }
            x += dx;
            y += dy;
        }

        // Đếm ngược cooldown tấn công
        if (attackCooldownCounter > 0) {
            attackCooldownCounter--;
        }

        if (keyHandler.attackPressed && attackCooldownCounter == 0) {
            attack();
            attackCooldownCounter = ATTACK_COOLDOWN_MAX;
        }

        animator.update(isMoving, direction);
        sprite = animator.getCurrentFrame();
    }

    /**
     * ĐA HÌNH: override lại attack() của Entity - hành vi tấn công riêng của Player
     * (khác hoàn toàn với Monster.attack() ở module Dev 4), dù cùng ký hiệu phương thức.
     */
    @Override
    public void attack() {
        // TODO (Dev 2): Tạo Rectangle hitbox tấn công phía trước player theo direction, ví dụ:
        // Rectangle attackBox = switch (direction) {
        //     case UP    -> new Rectangle((int)x, (int)y - width, width, width);
        //     case DOWN  -> new Rectangle((int)x, (int)y + height, width, width);
        //     case LEFT  -> new Rectangle((int)x - width, (int)y, width, width);
        //     case RIGHT -> new Rectangle((int)x + width, (int)y, width, width);
        // };
        // TODO (phối hợp Dev 4): duyệt qua danh sách Monster đang active, nếu
        // attackBox.intersects(monster.getBounds()) thì monster.takeDamage(damage).
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
    public Rectangle getBounds() {
        // TODO (Dev 2, phối hợp Dev 3): thu nhỏ hitbox chỉ lấy phần chân nhân vật
        // để cảm giác va chạm với tường tự nhiên hơn, ví dụ chỉ lấy 1/3 dưới sprite.
        return super.getBounds();
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
}
