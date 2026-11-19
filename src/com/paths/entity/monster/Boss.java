package com.paths.entity.monster;

import java.awt.Color;
import java.awt.Graphics2D;

import com.paths.core.GamePanel;
import com.paths.core.World;
import com.paths.entity.GameObject;
import com.paths.entity.monster.ai.ChaseBehavior;

/** Dev 4. Boss "Chúa tể Bóng tối" ở cuối hầm ngục: 400 máu, to 2x2 ô, có chiêu thức riêng. */
public class Boss extends Monster {

    private static final Color ROBE = new Color(74, 44, 110);
    private static final Color ROBE_DARK = new Color(46, 26, 72);
    private static final Color CROWN = new Color(232, 186, 64);
    private static final Color FACE = new Color(26, 18, 34);
    private static final Color EYE = new Color(255, 70, 70);

    public Boss(World world, double x, double y) {
        super(world, x, y, GamePanel.TILE_SIZE * 2, GamePanel.TILE_SIZE * 2, 400, 2, 20);
        detectionRange = GamePanel.TILE_SIZE * 5;
        loseRange = GamePanel.TILE_SIZE * 12;
    }

    @Override
    public String getKind() {
        return "BOSS";
    }

    @Override
    protected void drawBody(Graphics2D g2, int sx, int sy) {
        // Hào quang tím nhấp nháy quanh Boss
        int pulse = (int) (40 + 30 * Math.sin(animCounter * 0.08));
        g2.setColor(new Color(150, 80, 220, pulse));
        g2.fillOval(sx - 6, sy - 6, width + 12, height + 12);

        int sway = (int) Math.round(Math.sin(animCounter * 0.06)); // Áo choàng đung đưa nhẹ

        fill(g2, sx, sy, ROBE_DARK, 3 + sway, 13, 10, 2);       // vạt áo dưới
        fill(g2, sx, sy, ROBE, 3, 6, 10, 8);                    // thân áo
        fill(g2, sx, sy, ROBE_DARK, 7, 6, 2, 8);                // nẹp giữa
        fill(g2, sx, sy, ROBE, 1, 7, 2, 5);                     // tay áo
        fill(g2, sx, sy, ROBE, 13, 7, 2, 5);

        fill(g2, sx, sy, ROBE_DARK, 5, 1, 6, 6);                // mũ trùm
        fill(g2, sx, sy, FACE, 6, 3, 4, 3);                     // khuôn mặt tối
        fill(g2, sx, sy, EYE, 6, 4, 1, 1);                      // mắt đỏ
        fill(g2, sx, sy, EYE, 9, 4, 1, 1);

        fill(g2, sx, sy, CROWN, 5, 0, 6, 1);                    // vương miện
        fill(g2, sx, sy, CROWN, 5, -1, 1, 1);
        fill(g2, sx, sy, CROWN, 7, -1, 2, 1);
        fill(g2, sx, sy, CROWN, 10, -1, 1, 1);
    }
    /**
     * OVERRIDE để Boss "lì đòn": chỉ bị đẩy lùi 25% so với quái thường.
     * Gọi super.applyKnockback() với lực nhỏ hơn -> tái sử dụng code của Entity.
     */
    @Override
    public void applyKnockback(GameObject source, double force) {
        super.applyKnockback(source, force * 0.25);
    }
    /** Boss đang đánh nhau = còn sống và đang ở trạng thái rượt đuổi */
    public boolean isEngaged() {
        return alive && aiState instanceof ChaseBehavior;
    }

    public String getDisplayName() {
        return "Chúa tể Bóng tối";
    }

    public int getPhase() {
        return 1; // Giai đoạn 4
    }
}