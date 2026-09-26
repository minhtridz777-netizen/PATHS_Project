package com.paths.entity.monster;

/**
 * PHỤ TRÁCH: Dev 4
 *
 * Boss KẾ THỪA từ Monster (Boss IS-A Monster: có đầy đủ máu, AI, hitbox... của quái
 * thường) nhưng OVERRIDE lại attack() để có chiêu thức mạnh/đặc biệt hơn, và có thể
 * thêm khái niệm "phase" (giai đoạn) - khi máu xuống dưới 50% thì đổi sang bộ đòn khác.
 *
 * ĐÂY LÀ MINH HỌA RÕ NHẤT CHO CẢ THỪA KẾ + ĐA HÌNH TRONG 1 CLASS:
 *  - THỪA KẾ: Boss tái sử dụng 100% field/method của Monster (x,y,hp,speed,AIState...),
 *    không phải viết lại từ đầu.
 *  - ĐA HÌNH: khi code ở PlayState/CollisionChecker chỉ cầm 1 biến kiểu Monster
 *    (vd for (Monster m : monsterList)) và gọi m.attack(), nếu object thực sự là
 *    Boss thì Java tự động chạy đúng Boss.attack() thay vì Monster.attack() -
 *    đây gọi là "runtime polymorphism" / dynamic dispatch.
 *
 * TODO (Dev 4):
 *  1. Thêm field currentPhase (vd enum Phase { PHASE_1, PHASE_2 }).
 *  2. Override attack() để tung chiêu khác nhau tùy currentPhase (vd PHASE_1 đánh cận
 *     chiến bình thường, PHASE_2 bắn thêm đạn/triệu hồi thêm quái nhỏ).
 *  3. Override takeDamage() để kiểm tra ngưỡng đổi phase (gọi super.takeDamage() trước,
 *     sau đó so sánh currentHp với maxHp * 0.5).
 */
public class Boss extends Monster {

    public Boss(double x, double y, int width, int height, int maxHp, int speed) {
        super(x, y, width, height, maxHp, speed);
        this.detectionRange = 300; // Boss thường phát hiện Player từ xa hơn quái thường
    }

    @Override
    public void attack() {
        // TODO (Dev 4): implement chiêu thức đặc biệt của Boss, có thể khác nhau theo currentPhase.
        super.attack(); // Có thể giữ lại đòn đánh cơ bản của Monster làm nền, rồi bổ sung thêm
    }
}
