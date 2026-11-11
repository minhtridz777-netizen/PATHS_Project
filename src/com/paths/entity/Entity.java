package com.paths.entity;

import com.paths.interfaces.IDamageable;
import com.paths.world.CollisionChecker;

/**
 * Dev 1. Vật thể sống: máu, tốc độ, hướng, bị đánh, đẩy lùi. Lớp cha của Player và mọi quái.
 * Lớp con gọi updateCommonTimers() đầu mỗi update() và dùng moveWithCollision() để đi không xuyên tường.
 */
public abstract class Entity extends GameObject implements IDamageable {

    public static final int HURT_FLASH_FRAMES = 12; // Số frame nhân vật nháy đỏ khi trúng đòn

    protected int maxHp;
    protected int currentHp;
    protected int speed; // pixel mỗi frame
    protected Direction direction = Direction.DOWN;
    protected boolean alive = true;

    protected int hurtTimer = 0;
    protected double knockX = 0;
    protected double knockY = 0;

    protected Entity(double x, double y, int width, int height, int maxHp, int speed) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.speed = speed;
    }

    /** Hành vi tấn công. Mỗi class con tự định nghĩa (ĐA HÌNH) */
    public abstract void attack();

    @Override
    public void takeDamage(int amount) {
        if (!alive || amount <= 0) {
            return;
        }
        currentHp -= amount;
        hurtTimer = HURT_FLASH_FRAMES;
        if (currentHp <= 0) {
            currentHp = 0;
            alive = false;
            onDeath();
        }
    }

    /** Được gọi đúng 1 lần lúc máu về 0. Class con override để rơi đồ, báo nhiệm vụ... */
    protected void onDeath() {
    }

    /** Hồi máu, không vượt quá máu tối đa */
    public void heal(int amount) {
        if (alive) {
            currentHp = Math.min(maxHp, currentHp + amount);
        }
    }

    /** Bị đẩy lùi theo hướng từ source -> this. force khoảng 4-8 là vừa */
    public void applyKnockback(GameObject source, double force) {
        double dx = getCenterX() - source.getCenterX();
        double dy = getCenterY() - source.getCenterY();
        double len = Math.hypot(dx, dy);
        if (len < 0.001) {
            dx = 0;
            dy = 1;
            len = 1;
        }
        knockX = dx / len * force;
        knockY = dy / len * force;
    }

    /**
     * Di chuyển thêm (dx, dy) nhưng không đi xuyên tường.
     * Xử lý riêng trục X và trục Y nên khi đi chéo vào tường vẫn trượt dọc tường được.
     */
    protected void moveWithCollision(double dx, double dy, CollisionChecker cc) {
        moveAxis(dx, true, cc);
        moveAxis(dy, false, cc);
    }

    private void moveAxis(double amount, boolean horizontal, CollisionChecker cc) {
        double step = amount;
        // Nếu bị chặn thì thử đi ngắn lại từng 1px, để nhân vật áp sát được vào tường
        while (Math.abs(step) > 0.01) {
            boolean free = horizontal ? cc.canMove(this, step, 0) : cc.canMove(this, 0, step);
            if (free) {
                if (horizontal) {
                    x += step;
                } else {
                    y += step;
                }
                return;
            }
            step = Math.abs(step) > 1 ? step - Math.signum(step) : 0;
        }
    }

    /** Gọi ở ĐẦU update() của class con: đếm lùi hiệu ứng nháy đỏ và xử lý bị đẩy lùi */
    protected void updateCommonTimers(CollisionChecker cc) {
        if (hurtTimer > 0) {
            hurtTimer--;
        }
        if (Math.abs(knockX) > 0.2 || Math.abs(knockY) > 0.2) {
            moveWithCollision(knockX, knockY, cc);
            knockX *= 0.8;
            knockY *= 0.8;
        } else {
            knockX = 0;
            knockY = 0;
        }
    }

    public boolean isHurt() {
        return hurtTimer > 0;
    }

    @Override
    public boolean isDead() {
        return !alive;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }
}
