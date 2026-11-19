package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.Direction;
import com.paths.entity.Entity;
import com.paths.entity.monster.ai.AIState;
import com.paths.entity.monster.ai.PatrolBehavior;
import com.paths.world.Camera;
import java.awt.AlphaComposite;
import java.awt.Composite;
import com.paths.entity.player.Player;
import com.paths.ui.HealthBar;

/**
 * Dev 4. Lớp cha của mọi quái: chạy AI, di chuyển, tấn công, chết.
 * Lớp con chỉ định nghĩa phần riêng như tên loại và hình dáng.
 */
public abstract class Monster extends Entity {
    private static final int ATTACK_COOLDOWN = 45; // Cắn tối đa 1 lần mỗi 0.75 giây
    private static final int DEATH_FRAMES = 30;
    protected final World world;
    protected AIState aiState = new PatrolBehavior();
    protected int contactDamage;

    protected int detectionRange = GamePanel.TILE_SIZE * 4; // Thấy Player trong 4 ô thì đuổi (Giai đoạn 2)
    protected int loseRange = GamePanel.TILE_SIZE * 7;      // Player chạy xa quá 7 ô thì bỏ cuộc

    protected final double homeX; // Chỗ quái xuất hiện, tuần tra quanh đây
    protected final double homeY;
    protected int animCounter = 0; // Tăng mỗi frame, class con dùng để làm hoạt ảnh
    protected boolean walking = false;
    protected int attackCooldown = 0;
    protected int deathTimer = 0;


    protected Monster(World world, double x, double y, int width, int height,
                      int maxHp, int speed, int contactDamage) {
        super(x, y, width, height, maxHp, speed);
        this.world = world;
        this.contactDamage = contactDamage;
        this.homeX = x;
        this.homeY = y;
    }

    public abstract String getKind();

    /** Vẽ hình dáng riêng của từng loại quái tại tọa độ màn hình (sx, sy) */
    protected abstract void drawBody(Graphics2D g2, int sx, int sy);

    @Override
    public void update() {
        animCounter++;
        updateCommonTimers(world.getCollisionChecker());
        if (!alive) {
            deathTimer++;
            return;
        }
        if (attackCooldown > 0) {
            attackCooldown--;
        }
        walking = false;
        aiState.execute(this); // Giao cho trạng thái AI hiện tại quyết định làm gì

        Player player = world.getPlayer();
        if (!player.isDead() && world.getCollisionChecker().intersects(this, player)) {
            attack();
        }
    }

    /** Cắn Player khi chạm vào. Chỉ đẩy lùi nếu đòn thật sự trúng (Player không đang bất tử) */
    @Override
    public void attack() {
        if (attackCooldown > 0) {
            return;
        }
        Player player = world.getPlayer();
        int hpBefore = player.getCurrentHp();
        player.takeDamage(contactDamage);
        if (player.getCurrentHp() < hpBefore) {
            player.applyKnockback(this, 6);
        }
        attackCooldown = ATTACK_COOLDOWN;
    }
    /** Entity gọi hàm này đúng 1 lần khi máu về 0 */
    @Override
    protected void onDeath() {
        world.onMonsterKilled(this);
    }
    @Override
    public void draw(Graphics2D g2, Camera camera) {
        int sx = screenX(camera);
        int sy = screenY(camera);

        if (isDead()) {
            drawDeath(g2, sx, sy);
            return;
        }
        g2.setColor(new Color(0, 0, 0, 70)); // Bóng
        g2.fillOval(sx + width / 6, sy + height * 13 / 16, width * 2 / 3, height / 6);
        drawBody(g2, sx, sy);

        if (currentHp < maxHp) {
            HealthBar.drawSmall(g2, sx + 6, sy - 8, width - 12, this);
        }
    }
    /** Thân quái mờ dần + các vòng khói xám nở ra */
    private void drawDeath(Graphics2D g2, int sx, int sy) {
        float t = Math.min(1f, deathTimer / (float) DEATH_FRAMES);
        Composite saved = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - t));
        drawBody(g2, sx, sy);
        g2.setComposite(saved);

        int alpha = (int) (160 * (1 - t));
        g2.setColor(new Color(220, 220, 230, alpha));
        int r = (int) (width * 0.3 + width * 0.5 * t);
        int cx = sx + width / 2;
        int cy = sy + height / 2;
        for (int i = 0; i < 6; i++) {
            double a = i * Math.PI / 3 + t;
            int px = cx + (int) (Math.cos(a) * r);
            int py = cy + (int) (Math.sin(a) * r);
            int s = Math.max(2, (int) (width / 6.0 * (1 - t)));
            g2.fillOval(px - s, py - s, s * 2, s * 2);
        }
    }
    /**
     * Đi về phía điểm (targetX, targetY) - tọa độ tâm. speedFactor = 1 là tốc độ bình thường.
     * Các AIState (Patrol, Chase) gọi hàm này. Tự cập nhật hướng nhìn theo trục đi nhiều hơn.
     */
    public void moveToward(double targetX, double targetY, double speedFactor) {
        double dx = targetX - getCenterX();
        double dy = targetY - getCenterY();
        double dist = Math.hypot(dx, dy);
        if (dist < 1) {
            return;
        }
        double step = Math.min(dist, speed * speedFactor);
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx < 0 ? Direction.LEFT : Direction.RIGHT;
        } else {
            direction = dy < 0 ? Direction.UP : Direction.DOWN;
        }
        walking = true;
        moveWithCollision(dx / dist * step, dy / dist * step, world.getCollisionChecker());
    }

    public double distanceToPlayer() {
        return distanceTo(world.getPlayer());
    }

    /** Hitbox nhỏ hơn hình vẽ một chút để va chạm "công bằng" với người chơi */
    @Override
    public Rectangle getBounds() {
        int mx = width / 6;
        int top = height / 3;
        return new Rectangle((int) x + mx, (int) y + top, width - 2 * mx, height - top - height / 12);
    }

    public boolean isRemovable() {
        return isDead() && deathTimer > DEATH_FRAMES;
    }

    public void setAIState(AIState newState) {
        this.aiState = newState;
    }
    public AIState getAIState() { return aiState; }

    public World getWorld() { return world; }
    public double getHomeX() { return homeX + width / 2.0; }
    public double getHomeY() { return homeY + height / 2.0; }
    public int getDetectionRange() { return detectionRange; }
    public int getLoseRange() { return loseRange; }

    /** Tiện ích vẽ pixel-art: tô 1 khối trên lưới 16x16 phủ kín kích thước quái */
    protected void fill(Graphics2D g2, int sx, int sy, Color c, double gx, double gy, double gw, double gh) {
        double ux = width / 16.0;
        double uy = height / 16.0;
        g2.setColor(isHurt() ? Color.WHITE : c); // Trúng đòn thì nháy trắng
        int x0 = sx + (int) Math.round(gx * ux);
        int y0 = sy + (int) Math.round(gy * uy);
        int x1 = sx + (int) Math.round((gx + gw) * ux);
        int y1 = sy + (int) Math.round((gy + gh) * uy);
        g2.fillRect(x0, y0, x1 - x0, y1 - y0);
    }
}