package com.paths.entity.monster;

import com.paths.core.GamePanel;
import com.paths.core.World;

/** Dev 4. Boss "Chúa tể Bóng tối" ở cuối hầm ngục: 400 máu, to 2x2 ô, có chiêu thức riêng. */
public class Boss extends Monster {

    public Boss(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE * 2, 400, 2, 20);
    }

    @Override
    public String getKind() {
        return "BOSS";
    }

    /** true khi Boss đã phát hiện và đang đánh nhau với Player */
    public boolean isEngaged() {
        return false; // TODO (Dev 4, Giai đoạn 2)
    }

    public String getDisplayName() {
        return "Chúa tể Bóng tối";
    }

    /** Giai đoạn chiến đấu của Boss: 1 = bình thường, 2 = nổi điên khi còn dưới 50% máu */
    public int getPhase() {
        return 1; // TODO (Dev 4, Giai đoạn 4)
    }
}
