package com.paths.interfaces;

/**
 * PHỤ TRÁCH: Dev 1 (thiết kế), dùng chung bởi Dev 2 (Player) và Dev 4 (Monster, Boss)
 *
 * Interface đại diện cho "bất kỳ thứ gì có thể nhận sát thương và có máu".
 *
 * ĐÂY LÀ VÍ DỤ RÕ NHẤT CỦA TÍNH TRỪU TƯỢNG (Abstraction):
 * Player, Monster, Boss là 3 class HOÀN TOÀN khác nhau về cây kế thừa, hành vi,
 * animation... nhưng chúng đều "CÓ THỂ BỊ ĐÁNH" -> nên đều implements IDamageable.
 * Nhờ vậy, class Weapon/Attack (Dev 2) khi gây sát thương chỉ cần biết:
 *
 *      void dealDamageTo(IDamageable target) { target.takeDamage(10); }
 *
 * mà KHÔNG CẦN quan tâm target là Player hay Monster hay Boss hay 1 cái bình dễ vỡ
 * nào đó sau này -> đây cũng là ĐA HÌNH (Polymorphism): cùng gọi takeDamage() nhưng
 * mỗi class implement khác nhau (Player mất máu + chớp đỏ màn hình, Monster mất máu +
 * chạy trốn, Boss mất máu + đổi phase...).
 */
public interface IDamageable {

    /** Nhận sát thương. Mỗi class tự quyết định trừ máu như thế nào, có giáp hay không... */
    void takeDamage(int amount);

    /** Kiểm tra đã chết chưa (currentHp <= 0) */
    boolean isDead();

    int getCurrentHp();

    int getMaxHp();
}
