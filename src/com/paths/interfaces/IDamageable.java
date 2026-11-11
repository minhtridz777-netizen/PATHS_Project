package com.paths.interfaces;

/**
 * Dev 1. Bất cứ thứ gì có máu và bị đánh được (Player, quái, Boss).
 * Code gây sát thương và thanh máu chỉ cần biết interface này.
 */
public interface IDamageable {

    /** Nhận sát thương. Mỗi class tự quyết định cách trừ máu (vd Player có thời gian bất tử) */
    void takeDamage(int amount);

    boolean isDead();

    int getCurrentHp();

    int getMaxHp();
}
