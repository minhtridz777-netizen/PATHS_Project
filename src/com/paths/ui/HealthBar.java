package com.paths.ui;

import java.awt.Graphics2D;
import com.paths.interfaces.IDamageable;

/** Dev 5. Thanh máu cho bất kỳ IDamageable nào. drawSmall() là bản mini cho quái. */
public class HealthBar {

    protected final int x, y, width, height;

    public HealthBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2, IDamageable target) {
    }

    /** Thanh máu mini (cao 5px) tại tọa độ màn hình (x, y) */
    public static void drawSmall(Graphics2D g2, int x, int y, int width, IDamageable target) {
    }
}
