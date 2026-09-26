package com.paths.entity.monster;

import java.awt.Graphics2D;
import com.paths.entity.Entity;
import com.paths.entity.monster.ai.AIState;
import com.paths.entity.monster.ai.PatrolBehavior;
import com.paths.entity.object.Item;
import com.paths.world.Camera;
import java.util.List;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Class quái vật thường. Kế thừa Entity nên có sẵn máu, tốc độ, hướng, và bị
 * đánh được qua IDamageable (Player.attack() gây damage được lên Monster mà
 * không cần Player biết chi tiết bên trong Monster - đây là lợi ích của trừu tượng hóa).
 *
 * Hành vi AI được ủy quyền (delegate) cho currentAIState (Strategy Pattern - xem AIState.java),
 * giúp Monster.update() rất gọn gàng, không có if/else phức tạp.
 *
 * TODO (Dev 4):
 *  1. Trong update(): gọi currentAIState.execute(this) để AI tự quyết định di chuyển.
 *  2. Implement attack(): quái gây damage lên Player khi lọt vào tầm đánh gần
 *     (dùng CollisionChecker của Dev 3 để kiểm tra 2 hitbox chạm nhau).
 *  3. Implement hệ thống RỚT ĐỒ (loot): khi isDead() == true, sinh ra ngẫu nhiên
 *     0-2 Item từ lootTable dựa theo tỉ lệ rớt đồ (dropRate) và thông báo cho
 *     PlayState (Dev 1) biết để thêm Item đó vào danh sách vật phẩm trên map.
 */
public class Monster extends Entity {

    protected AIState currentAIState = new PatrolBehavior(); // Mặc định bắt đầu bằng tuần tra
    protected int detectionRange = 150; // Bán kính (px) để phát hiện Player và chuyển sang rượt đuổi
    protected List<Item> lootTable; // TODO (Dev 4): danh sách vật phẩm có thể rớt kèm tỉ lệ

    public Monster(double x, double y, int width, int height, int maxHp, int speed) {
        super(x, y, width, height, maxHp, speed);
    }

    @Override
    public void update() {
        if (isDead()) {
            // TODO (Dev 4): kích hoạt logic rớt đồ 1 LẦN duy nhất rồi đánh dấu để
            // GameLoop biết loại bỏ Monster này khỏi danh sách active.
            return;
        }
        currentAIState.execute(this);
    }

    /**
     * ĐA HÌNH: override attack() của Entity - Monster tấn công bằng cách cắn/lao vào
     * Player khi ở cự ly gần, khác với Player dùng vũ khí, và khác với Boss (xem Boss.java).
     */
    @Override
    public void attack() {
        // TODO (Dev 4): gây damage lên Player nếu hitbox 2 bên chạm nhau.
    }

    @Override
    public void draw(Graphics2D g2, Camera camera) {
        int screenX = (int) x - camera.getX();
        int screenY = (int) y - camera.getY();
        if (sprite != null) {
            g2.drawImage(sprite, screenX, screenY, width, height, null);
        }
        // TODO (Dev 4): vẽ thêm thanh máu nhỏ phía trên đầu quái nếu muốn (tùy chọn nâng cao).
    }

    /** Cho phép các AIState (Patrol/Chase) đổi trạng thái AI hiện tại của quái này */
    public void setAIState(AIState newState) {
        this.currentAIState = newState;
    }

    public int getDetectionRange() {
        return detectionRange;
    }
}
