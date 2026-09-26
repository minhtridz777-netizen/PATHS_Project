package com.paths.entity;

import com.paths.interfaces.IDamageable;

/**
 * PHỤ TRÁCH: Dev 1 (thiết kế nền tảng). Player (Dev 2) và Monster/Boss (Dev 4) đều kế thừa từ đây.
 *
 * Entity là class cha cho "vật thể SỐNG" trong game (có máu, có tốc độ di chuyển,
 * có hướng, có thể tấn công và có thể bị đánh) - khác với GameObject "vô tri" như
 * Item hay Tile.
 *
 * Entity extends GameObject   -> THỪA KẾ (Inheritance): tái sử dụng x, y, width, height, sprite.
 * Entity implements IDamageable -> hiện thực hóa hành vi "có thể bị đánh" dùng chung
 * cho cả Player lẫn Monster, để Weapon (Dev 2) hay AI (Dev 4) đều gây damage qua
 * đúng 1 kiểu interface duy nhất (xem thêm giải thích trong IDamageable.java).
 *
 * attack() là abstract -> đây là ĐA HÌNH (Polymorphism): Player.attack() sẽ vung vũ khí
 * theo hướng đang nhìn, Monster.attack() sẽ cắn khi player lọt vào tầm, Boss.attack()
 * (kế thừa từ Monster) sẽ override lại để tung chiêu đặc biệt - cùng là entity.attack()
 * nhưng hành vi thực thi khác nhau tùy vào object thực sự là ai lúc runtime.
 */
public abstract class Entity extends GameObject implements IDamageable {

    protected int maxHp;
    protected int currentHp;
    protected int speed; // Số pixel di chuyển mỗi frame
    protected Direction direction = Direction.DOWN;
    protected boolean alive = true;

    protected Entity(double x, double y, int width, int height, int maxHp, int speed) {
        super(x, y, width, height);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.speed = speed;
    }

    /**
     * Hành vi tấn công - MỖI class con TỰ ĐỊNH NGHĨA KHÁC NHAU (xem giải thích ở trên).
     * TODO (Dev 2 cho Player, Dev 4 cho Monster/Boss): override method này trong class con.
     */
    public abstract void attack();

    /**
     * Hiện thực hóa IDamageable.takeDamage() dùng chung cho mọi Entity.
     * Class con (Player, Monster) có thể override thêm hiệu ứng riêng (vd Player
     * override để thêm hiệu ứng chớp đỏ màn hình) bằng cách gọi super.takeDamage(amount)
     * rồi bổ sung logic riêng.
     */
    @Override
    public void takeDamage(int amount) {
        currentHp -= amount;
        if (currentHp <= 0) {
            currentHp = 0;
            alive = false;
        }
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
}
