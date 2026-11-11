package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.World;
import com.paths.entity.Entity;
import com.paths.entity.monster.ai.AIState;
import com.paths.entity.monster.ai.PatrolBehavior;
import com.paths.world.Camera;

/**
 * Dev 4. Lớp cha của mọi quái: chạy AI, di chuyển, tấn công, chết.
 * Lớp con chỉ định nghĩa phần riêng như tên loại và hình dáng.
 */
public abstract class Monster extends Entity {

    protected final World world;
    protected AIState aiState = new PatrolBehavior();
    protected int contactDamage; // Sát thương khi chạm vào Player

    protected Monster(World world, double x, double y, int width, int height,
                      int maxHp, int speed, int contactDamage) {
        super(x, y, width, height, maxHp, speed);
        this.world = world;
        this.contactDamage = contactDamage;
    }

    /** Tên loại quái, dùng cho nhiệm vụ: "SLIME", "SKELETON", "BOSS" */
    public abstract String getKind();

    @Override
    public void update() {
        // TODO (Dev 4, Giai đoạn 1): chạy AI
    }

    @Override
    public void attack() {
        // TODO (Dev 4, Giai đoạn 2): gây sát thương khi chạm Player
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        // Bản khung: ô vuông đỏ tạm thời
        if (!isDead()) {
            g2.setColor(new Color(220, 70, 60));
            g2.fillRect(screenX(camera), screenY(camera), width, height);
        }
    }

    /** true khi quái đã chết xong và World được phép xóa nó khỏi bản đồ */
    public boolean isRemovable() {
        return isDead();
    }

    public void setAIState(AIState newState) {
        this.aiState = newState;
    }

    public World getWorld() {
        return world;
    }
}
