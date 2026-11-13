package com.paths.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import com.paths.interfaces.IDamageable;

/** Dev 5. Thanh máu cho bất kỳ IDamageable nào. drawSmall() là bản mini cho quái. */
public class HealthBar {

    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 11);

    protected final int x, y, width, height;

    public HealthBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2, IDamageable target) {
        double ratio = ratioOf(target);
        int fillWidth = (int) Math.round(width * ratio);

        g2.setColor(new Color(60, 22, 26));                  // nền (phần máu đã mất)
        g2.fillRect(x, y, width, height);
        g2.setColor(colorFor(ratio));                        // phần máu còn lại
        g2.fillRect(x, y, fillWidth, height);
        g2.setColor(new Color(255, 255, 255, 70));           // vệt sáng phía trên cho có khối
        g2.fillRect(x, y, fillWidth, height / 3);
        g2.setColor(new Color(20, 20, 24));                  // viền
        g2.drawRect(x, y, width, height);

        // Chữ "75 / 100" ở giữa thanh
        String text = target.getCurrentHp() + " / " + target.getMaxHp();
        g2.setFont(LABEL_FONT);
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (width - fm.stringWidth(text)) / 2;
        int ty = y + (height + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(text, tx + 1, ty + 1);                 // bóng chữ
        g2.setColor(Color.WHITE);
        g2.drawString(text, tx, ty);
    }

    public static void drawSmall(Graphics2D g2, int x, int y, int width, IDamageable target) {
        // Giai đoạn 2
    }

    /** Tỉ lệ máu 0.0 -> 1.0 */
    protected static double ratioOf(IDamageable target) {
        if (target.getMaxHp() <= 0) {
            return 0;
        }
        double ratio = (double) target.getCurrentHp() / target.getMaxHp();
        return Math.max(0, Math.min(1, ratio));
    }

    /** Trên 50% xanh lá, 20-50% vàng, dưới 20% đỏ */
    protected static Color colorFor(double ratio) {
        if (ratio > 0.5) {
            return new Color(84, 196, 92);
        } else if (ratio > 0.2) {
            return new Color(236, 190, 60);
        } else {
            return new Color(222, 64, 56);
        }
    }
}